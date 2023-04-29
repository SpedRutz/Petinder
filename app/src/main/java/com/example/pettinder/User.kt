package com.example.pettinder


data class User(
    //Data class to represent the user profile with unique id, name,email, and profile picture
    //Firebase needs for the elements of the constructor to be empty
    var uid: String = "",
    var displayName: String = "",
    var email: String = "",
    var ProfilePicture: String = ""

)
