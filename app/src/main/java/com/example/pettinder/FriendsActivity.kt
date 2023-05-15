package com.example.pettinder


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FriendsActivity : AppCompatActivity() {

    //Initialies the recycler view, adapter, progressbar, and swipe to refresh
    private lateinit var friendRV: RecyclerView
    private lateinit var friendadapter: FriendsAdapter
    private lateinit var  friendprogressbar: ProgressBar
    private lateinit var SwipetoRefresh: SwipeRefreshLayout

    var myImageURL: String = ""
    var MyuserID: String = ""
    var mydisplayname: String = ""

    val users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        friendprogressbar = findViewById(R.id.friendsProgressbar)
        friendRV = findViewById(R.id.FriendRV)
        SwipetoRefresh = findViewById(R.id.friendsSwipeRefresh)


       SwipetoRefresh.setOnRefreshListener {
           FetchUserdata()
           SwipetoRefresh.isRefreshing = false
       }


        //sets up the on user click for the user to be sent to the chatting activity once they click on a friend item
        friendadapter = FriendsAdapter(users, object: FriendsAdapter.OnUserClickListener {
            override fun onUserClicked(user: User, position: Int) {
                try {
                    Toast.makeText(this@FriendsActivity, "The onUserclicked worked", Toast.LENGTH_SHORT).show()
                   val intent = Intent(this@FriendsActivity,ChattingActivity::class.java)
                       .putExtra("user_name", users[position].displayName)
                       .putExtra("user_Email", users[position].email)
                       .putExtra("User_image",users[position].ProfilePicture)
                       .putExtra("User_id",users[position].uid)
                       .putExtra("myuser_id",MyuserID)
                       .putExtra("My_Image_Url",myImageURL)
                       .putExtra("my_displayname", mydisplayname)




                    startActivity(intent)

                } catch (e: Exception){
                    Toast.makeText(this@FriendsActivity,"The user click did not work",Toast.LENGTH_SHORT).show()
                    Log.e("OnUserClick didn't work","The onuserclick has failed", e)
                }
            }

        })

        FetchUserdata()

        //Set the recyclerview with adapter
        friendRV.adapter = friendadapter
        friendRV.layoutManager = LinearLayoutManager(this)
        friendprogressbar.visibility = View.GONE
        friendRV.visibility = View.VISIBLE



    }

    //Gets the users from firebase database to populate the USer data class
    //Takes a "snapshot" of the firebase database and uses the information to populate the user class
    fun FetchUserdata(){
        FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
                snapshot.children.forEach { snapshot ->
                    val user = snapshot.getValue(User::class.java)
                    if (user != null && user.uid != currentUserID){
                        users.add(user)
                    }

                    //Look through the data base for the current user's (you) profile picture

                    if (snapshot.key == currentUserID ){
                            if (user != null) {
                                myImageURL = user.ProfilePicture
                                MyuserID = user.uid
                                mydisplayname = user.displayName
                            }
                        }


                }
                friendadapter.notifyDataSetChanged()// Notify/update the adapter with the correct changes
            }

            override fun onCancelled(error: DatabaseError) {
               Log.d("Could not fetch user data", "Database error: $error")
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    // Allows for the menu to be clicked in the friends activity and will send the user to the friends activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile_menu_item -> {
                // Navigate to ProfileActivity when the menu item is clicked
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true

            }

            R.id.pet_menu -> {
                //Navigate to the petfindingactivty
                val intent = Intent(this, PetfindingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }
}
