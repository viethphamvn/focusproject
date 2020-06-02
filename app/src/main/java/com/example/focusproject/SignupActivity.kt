package com.example.focusproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*
import kotlin.math.sign

class SignupActivity : AppCompatActivity() {

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //Setup Views
        var username_edittext = findViewById<TextInputLayout>(R.id.username_edittext)
        var email_edittext = findViewById<TextInputLayout>(R.id.email_edittext)
        var password_edittext = findViewById<TextInputLayout>(R.id.password_edittext)

        signUpButton.setOnClickListener {
            //Check inputs condition
            var username = username_edittext.editText?.text.toString()
            var email = email_edittext.editText?.text.toString()
            var password = password_edittext.editText?.text.toString()
            signUpandIn(username, email, password)

            println("$username $email $password")
        }
    }

    private fun signUpandIn(username:String, email:String, password:String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    var firebaseUser = firebaseAuth.currentUser
                    val userId = firebaseUser!!.uid
                    var newUser : HashMap<String,String> = HashMap<String, String>()
                    newUser.put("id", userId)
                    newUser.put("username",username)
                    newUser.put("email",email)
                    //Update user profile from Firebase
                    var profileChangeRequest = UserProfileChangeRequest.Builder()
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
        var intent = Intent(this, UserInfoActivity::class.java)
        startActivity(intent)
        finish()
    }
}
