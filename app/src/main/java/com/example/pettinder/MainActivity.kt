package com.example.pettinder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    //Will be used to determine if user is signed up or not
    private var isSignedUp: Boolean = true




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Login screen elements

        //edittext (email, user, pass)
        val username = findViewById<EditText>(R.id.Username)
        val email = findViewById<EditText>(R.id.Email)
        val password = findViewById<EditText>(R.id.Password)

        //submit/sign-up button and text to click on when already have an account
        val submit = findViewById<Button>(R.id.submit)
        val logintext = findViewById<TextView>(R.id.signed)


        if (FirebaseAuth.getInstance().currentUser != null){
            //friend activity
            val intent = Intent(this,FriendsActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Handles the signup with firebase authentication, creates users email an password. Will make a toast message to make the user go to the message
        fun handlesignup() {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(
                    OnCompleteListener { task: Task<AuthResult> ->
                        //every user is unique
                        if (task.isSuccessful) {

                            //Get the user's unique id and display name
                            val user = FirebaseAuth.getInstance().currentUser
                            val uid = user?.uid ?: ""
                            val displayName = username.text.toString()
                            val emailName =email.text.toString()


                            //Create the user object with the user information
                            val userProfile = User(uid,displayName,emailName,"")

                            //Saves the user profile to the database
                            FirebaseDatabase.getInstance().getReference("user/").child(uid).setValue(userProfile)

                            Toast.makeText(this@MainActivity, "Sign-up complete", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,FriendsActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@MainActivity, "Signup failed", Toast.LENGTH_SHORT)
                                .show()
                            Toast.makeText(
                                this@MainActivity,
                                task.exception?.localizedMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
        }

        // Handles login with firebase
        fun handleLogin() {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        //going to start the pet finding activity

                        Toast.makeText(this@MainActivity, "Login Success", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this,FriendsActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MainActivity, "Login failed", Toast.LENGTH_SHORT).show()
                        Toast.makeText(
                            this@MainActivity,
                            task.exception?.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
        }

        submit.setOnClickListener { view ->
            if (email.text.toString().isEmpty() || password.text.toString().isEmpty() || (isSignedUp && username.text.toString().isEmpty())) {
                Toast.makeText(this@MainActivity, "Invalid input", Toast.LENGTH_SHORT).show()
            } else {
                if (isSignedUp) {
                    handlesignup()
                } else {
                    handleLogin()
                }
            }
        }


        logintext.setOnClickListener { view ->
            if (isSignedUp) {
                isSignedUp = false
                username.visibility = View.GONE

                submit.text = "Log in"
                logintext.text = "Don't have an account? Sign up"
            } else {
                isSignedUp = true
                username.visibility  = View.VISIBLE
                submit.text = "Sign-up"
                logintext.text = "Already have an account? Log in"
            }
        }
    }

}