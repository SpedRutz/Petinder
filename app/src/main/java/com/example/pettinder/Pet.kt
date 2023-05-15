package com.example.pettinder


//Data class to represent the data from the arry of the pet finder api

// UPDATE 5/12/2023: We don't use this data class
data class Pet(
    val name: String,
    val age: String,
    val petimageURL: String,
    val gender: String,
    val location: String,
    val description: String
)
