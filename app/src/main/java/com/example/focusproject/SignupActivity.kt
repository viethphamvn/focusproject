package com.example.focusproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //Setup Views
        val username_edittext = findViewById<TextInputLayout>(R.id.username_edittext)
        val email_edittext = findViewById<TextInputLayout>(R.id.email_edittext)
        val password_edittext = findViewById<TextInputLayout>(R.id.password_edittext)

        signUpButton.setOnClickListener {
            //Check inputs condition
            val username = username_edittext.editText?.text.toString()
            val email = email_edittext.editText?.text.toString()
            val password = password_edittext.editText?.text.toString()
            signUpandIn(username, email, password)

            println("$username $email $password")
        }
    }

    private fun signUpandIn(username:String, email:String, password:String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val firebaseUser = firebaseAuth.currentUser
                    val userId = firebaseUser!!.uid
                    val newUser : HashMap<String,String> = HashMap()
                    newUser["id"] = userId
                    newUser["username"] = username
                    newUser["email"] = email
                    //Update user profile from Firebase
                    val profileChangeRequest = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build()
                    firebaseUser.updateProfile(profileChangeRequest)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                //TODO Add message for user
                            } else {

                            }
                        }

                    //Update user data to Firestore
                    firestore.collection("Users").document(firebaseUser.uid)
                        .set(newUser).addOnSuccessListener {
                            //Start MainActivity
                            startWelcomeActivity()
                            finish()
                        }
                        .addOnFailureListener {
                            //Add message
                        }
                } else {
                    //Add message
                    println("failed")
                }
            }
    }

    private fun startWelcomeActivity(){
        val intent = Intent(this, UserInfoActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
