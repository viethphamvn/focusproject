package com.example.focusproject.models

import java.io.Serializable

class User : Serializable{
    var username: String = ""
    var follower = ArrayList<String>()
    var following = ArrayList<String>()
    var id: String = ""
    var profilePictureUri: String = ""


    companion object {
        var currentUser : User = User()
    }
}