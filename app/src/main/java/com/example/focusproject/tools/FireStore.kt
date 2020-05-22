package com.example.focusproject.tools

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FireStore {
    companion object {
        var fireStore = FirebaseFirestore.getInstance()
        var currentUser = FirebaseAuth.getInstance().currentUser
    }
}