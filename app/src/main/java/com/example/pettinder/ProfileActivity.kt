package com.example.pettinder


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.pettinder.MainActivity
import com.example.pettinder.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ProfileActivity : AppCompatActivity() {


    var imageuri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profileactivity)

        //Button to go back to the petfinderactivity

        val backbutton = findViewById<Button>(R.id.backbtn)
        backbutton.setOnClickListener{view ->
            finish()
        }



        //signs the user out of the user activity and puts them back to the main activty
        val Logout = findViewById<Button>(R.id.logout)



        Logout.setOnClickListener { view ->
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val ProfilePicture = findViewById<ImageView>(R.id.Profilepic)

    //Activty result for the gallery to choose a picture
        val galleryActivityResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            //Sets the image to the profile imageview, throws an exception if the image is not found
            try {
                imageuri = uri
                ProfilePicture.setImageURI(uri)
            } catch (e:Exception) {
                Log.d("Error", "error Loading Image: ${e.localizedMessage}")
            }
        }

//Opens the phones gallery and allows user to pick picture by using an intent
        ProfilePicture.setOnClickListener{ view ->
            galleryActivityResult.launch("image/*")
        }

  //Button to upload the profile picture url to the firebase storage
        val uploadButton = findViewById<Button>(R.id.buttonUpload)

        uploadButton.setOnClickListener { view ->
            if (imageuri != null){
                uploadimage()
            } else {
                Toast.makeText(this@ProfileActivity," Please select an Image", Toast.LENGTH_SHORT).show()
            }
        }


        //Allows us to save the image even if we leave the activty
        val profilepicturereference = FirebaseDatabase.getInstance().getReference("user" + FirebaseAuth.getInstance().uid + "/profilePicture")



            profilepicturereference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val imageUrl = snapshot.getValue(String::class.java)

                    if (imageUrl != null){
                        Glide.with(this@ProfileActivity)
                            .load(imageUrl)
                            .into(ProfilePicture)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Profile Picture error", "could not retrieve the profile image: $error")
                }


            })


    }

    fun  uploadimage() {
        //progress bar
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        // Check if the ProgressBar view was found
        if (progressBar == null) {
            Log.e("Error", "ProgressBar not found in layout")
            return
        }
        //Upload the image to firebase storage using the image uri (URL)
        val imageref = FirebaseStorage.getInstance().getReference("images/" + UUID.randomUUID().toString())

        imageref.putFile(imageuri!!).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                // Save the URL in a variable or pass it to a function
                val downloadUrl = uri.toString()
                //Updates the firebase storage with the users current profile picture
                FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().uid + "/profilePicture").setValue(downloadUrl)
            }

            //Image was successfully uploaded
            progressBar.visibility =View.INVISIBLE
            Toast.makeText(this@ProfileActivity," Image uploaded sucessfully", Toast.LENGTH_SHORT).show()

        }// Adds a on failure listener incase image fails to upload

            .addOnFailureListener { e ->
                progressBar.visibility = View.INVISIBLE
                Log.d("Error", "Error uploading image: ${e.localizedMessage}")
                Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
            }
    }
}

