package com.example.focusproject.models

import java.io.Serializable

class User : Serializable{
    var username: String = ""
    var profilePicture: String = ""
    var follower = ArrayList<String>()
    var following = ArrayList<String>()
    var id: String = ""


    companion object {
        lateinit var currentUser : User
    }
}