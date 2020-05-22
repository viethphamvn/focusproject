package com.example.focusproject.tools

import com.example.focusproject.models.User
import com.google.firebase.firestore.DocumentSnapshot

class CreateUser {
    companion object {
        fun createUser(it: DocumentSnapshot): User {
            var user = User()
            user.follower = if (it.get("follower") != null) {it.get("follower") as ArrayList<String>} else {ArrayList<String>()}
            user.following = if (it.get("following") != null) {it.get("following") as ArrayList<String>} else {ArrayList<String>()}
            user.id = if (it.get("id") != null) {it.get("id").toString()} else {""}
            user.profilePicture = if (it.get("profilePicture") != null) {it.get("profilePicture").toString()} else {""}
            user.username = if (it.get("username") != null) {it.get("username").toString()} else {""}
            return user
        }
    }
}