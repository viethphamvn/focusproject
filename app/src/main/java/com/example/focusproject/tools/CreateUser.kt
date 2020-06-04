package com.example.focusproject.tools

import com.example.focusproject.models.User
import com.google.firebase.firestore.DocumentSnapshot

class CreateUser {
    companion object {
        fun createUser(it: DocumentSnapshot): User {
            val user = User()
            user.follower = if (it.get("follower") != null) {it.get("follower") as ArrayList<String>} else {ArrayList()}
            user.following = if (it.get("following") != null) {it.get("following") as ArrayList<String>} else {ArrayList()}
            user.id = if (it.get("id") != null) {it.get("id").toString()} else {""}
            user.profilePictureUri = if (it.get("profilePictureUri") != null) {it.get("profilePictureUri").toString()} else {""}
            user.username = if (it.get("username") != null) {it.get("username").toString()} else {""}
            return user
        }
    }
}