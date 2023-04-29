package com.example.pettinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import okio.IOException
import org.json.JSONObject
import kotlin.random.Random


class PetfindingActivity : AppCompatActivity() {

    private lateinit var rvPets: RecyclerView
    private lateinit var petAdapter: PetAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petfinding)

        Log.d("Testing bro", "this activity works dog")
        //Will be used in the fetchdata function
        val petNames = mutableListOf<String>()
        val petAges = mutableListOf<String>()
        val petImageUrls = mutableListOf<String>()
        val petGenders = mutableListOf<String>()
        val petLocations = mutableListOf<String>()
        val petDescriptions = mutableListOf<String>()


        fun fetchPetData(zipCode: String) {
            val url = "https://api.petfinder.com/v2/oauth2/token"
            val client = OkHttpClient()
            val apiKey = "lbKxLWq9bW0kOjhS7of8lQFhlSQhD9sNLnwR068tWRUC415ez9"
            val apiSecret = "JYAdq5WuhMEP583e9NKc7WZ2euGOuREk1dmL8yWp"

            val formBody = FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", apiKey)
                .add("client_secret", apiSecret)
                .build()

            val request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("Access Token Failed", "Failed to get the access token")
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()
                    val jsonObject = JSONObject(responseBody)
                    val accessToken = jsonObject.getString("access_token")
                    Log.d("Access token found", "Access token was retrived now use it")

                    val url = "https://api.petfinder.com/v2/animals?location=$zipCode"
                    val request = Request.Builder()
                        .url(url)
                        .header("Authorization", "Bearer $accessToken")
                        .build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("Pet data failed", "Failed to retrieve pet data")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val responseBody = response.body?.string()
                            val jsonObject = responseBody?.let { JSONObject(it) }
                            Log.d("Mission Complete", "You got the pet data bro :) here: $jsonObject")

                            //for loop to interate through the pet JSON array inreder to get the specifc pieces of data for the Pet data class


                            val petList = mutableListOf<Pet>()


                            val petJsonArray = jsonObject?.optJSONArray("animals")



                            if (petJsonArray != null) {
                                for (i in 0 until petJsonArray.length()) {
                                    val petJsonObject = petJsonArray.getJSONObject(i)

                                    val name = petJsonObject.getString("name")
                                    val age = petJsonObject.getString("age")
                                    val gender = petJsonObject.getString("gender")


                                    val addressJsonObject = petJsonObject.getJSONObject("contact").getJSONObject("address")
                                    val city = addressJsonObject.getString("city")
                                    val state = addressJsonObject.getString("state")
                                    val location = "$city, $state"
                                    val description = petJsonObject.optString("description", "No description available")

                                    val petPhotosJsonArray = petJsonObject.getJSONArray("photos")

                                    var petImageUrl: String? = null

                                    if (petPhotosJsonArray.length() > 0) {
                                        val randomIndex = Random.nextInt(petPhotosJsonArray.length()) // Get a random index
                                        petImageUrl = petPhotosJsonArray.getJSONObject(randomIndex).getString("full")
                                            .toString()
                                    }

                                    petNames.add(name)
                                    petAges.add(age)
                                    petImageUrls.add(petImageUrl ?: "")
                                    petGenders.add(gender)
                                    petLocations.add(location)
                                    if (description.isNullOrEmpty()) {
                                        petDescriptions.add("No description available")
                                    } else {
                                        petDescriptions.add(description)
                                    }


                                }
                            }
                            runOnUiThread {
                                petAdapter.notifyDataSetChanged()

                            }




                        }
                    })
                }
            })
        }


        //Initialize the pet adapter and recycler view
        rvPets = findViewById(R.id.rvPets)
        petAdapter = PetAdapter(petNames, petAges, petImageUrls, petGenders, petLocations, petDescriptions)


        //Set RecyclerView layout manager and adpater
        rvPets.layoutManager = LinearLayoutManager(this)
        rvPets.adapter = petAdapter
        rvPets.setBackgroundResource(R.drawable.roundedrecycler)


        //Set up the button onclick lister for the
        val buttonSearch = findViewById<Button>(R.id.btnSearch)
        val zipsearch = findViewById<EditText>(R.id.findzipcode)

        buttonSearch.setOnClickListener { view ->
            val zipcode = zipsearch.text.toString()
            fetchPetData(zipcode)

        }
    }



    //Profile Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile_menu_item -> {
                // Navigate to ProfileActivity when the menu item is clicked
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}