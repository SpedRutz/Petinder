package com.example.pettinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ChattingActivity : AppCompatActivity() {

    private lateinit var  ChattingRV: RecyclerView
    private lateinit var  chattingprogressbar: ProgressBar
    private lateinit var ChattingAdapter: MessageAdapter

    var friendUsername: String = ""
    var friendEmail: String =""
    var friendProfilepic: String =""
    var myprofilePic: String = ""
    var friendID: String = ""
    var chatRoomId: String = ""
    var myusername: String = ""



    //Max and Alex chatting
    //id of the chat room for max and alex
    val chattingroomid = ""

    val Messages = arrayListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)


        //Creating the chatting recycler view and adapter
        ChattingRV = findViewById(R.id.ChattingMessageRV)
        chattingprogressbar = findViewById(R.id.Chattingprogressbar)

        val EditChat = findViewById<EditText>(R.id.Typermessage)
        val SendingButton = findViewById<ImageView>(R.id.Sendmessagebtn)
        val friendchattingname = findViewById<TextView>(R.id.ChatFriendName)
        val friendChattingprofilepic = findViewById<ImageView>(R.id.ChatFriendimage)


        val myImageUrl = intent.getStringExtra("My_Image_Url") ?: ""
        val userImageUrl = intent.getStringExtra("User_image") ?: ""

        ChattingAdapter = MessageAdapter(Messages,myImageUrl,userImageUrl)
        ChattingRV.layoutManager = LinearLayoutManager(this)
        ChattingRV.adapter = ChattingAdapter

        SendingButton.setOnClickListener { view ->
            val messageText = EditChat.text.toString()
            if (messageText.isNotEmpty()) {
                //Calling the message class
                val message = Message(
                    Content = messageText,
                    Sender = FirebaseAuth.getInstance().currentUser?.email ?: "",
                    Receiver = friendEmail
                    // Add other fields as necessary
                )
                FirebaseDatabase.getInstance().getReference("messages/" + chatRoomId).push().setValue(message)
                EditChat.text.clear()
            }
        }



        //Get Friend infromation from the Freidnsactivity
        friendUsername = intent.getStringExtra("user_name") ?: ""
        friendEmail = intent.getStringExtra("user_Email") ?: ""
        friendProfilepic = intent.getStringExtra("User_image")?: ""
        myprofilePic = intent.getStringExtra("My_Image_Url")?: ""
        friendID = intent.getStringExtra("User_id")?: ""
        myusername = intent.getStringExtra("my_displayname")?: ""


        friendchattingname.text = friendUsername




        Glide.with(this@ChattingActivity)
                .load(intent.getStringExtra(friendProfilepic))
                .placeholder(R.drawable.account_circle)
                .error(R.drawable.account_circle)
                .into(friendChattingprofilepic)

        createChatroom()

    }

    // Function to crate a unique chatroom id by combining the usernames of two users of two users
    fun createChatroom(){
        FirebaseDatabase.getInstance().getReference("user" + FirebaseAuth.getInstance().uid).addListenerForSingleValueEvent( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val usernames = listOf(friendUsername, myusername).sorted()
                chatRoomId = usernames[0] + usernames[1]


                attachmessagelistener(chatRoomId)
            }

            override fun onCancelled(error: DatabaseError) {
               Log.d("Chatroom ID Error", "Failed to create the chatroom ID, $error")
            }


        })
    }

    //Puts the messages in the unique chatroomids in the firebase database
    fun attachmessagelistener(ChatroomId: String){


        FirebaseDatabase.getInstance().getReference("messages/$ChatroomId").addValueEventListener( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Messages.clear()
                snapshot.children.forEach { snapshot ->
                    val message = snapshot.getValue(Message::class.java)
                    if (message != null) {
                        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

                        Messages.add(snapshot.getValue(Message::class.java)!!)
                    }
                }
                //Will make sure that the Chatting Recycler view will always be at the bottom (last message sent)
                ChattingAdapter.notifyDataSetChanged()
                ChattingRV.scrollToPosition(Messages.size - 1)
                ChattingRV.visibility = View.VISIBLE
                chattingprogressbar.visibility = View.GONE

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Message Listener Error", "Could not put the messages into the firebase database: $error")
            }


        })
    }
}