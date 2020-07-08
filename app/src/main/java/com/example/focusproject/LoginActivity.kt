package com.example.focusproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Initiate Firebase Auth
        auth = FirebaseAuth.getInstance()

        //Get Views from Layout
        val emailTextEdit : TextInputLayout = findViewById(R.id.email_edittext)
        val passwordTextEdit : TextInputLayout = findViewById(R.id.password_edittext)
        val loginBtn : Button = findViewById(R.id.loginButton)
        val signupBtn : Button = findViewById(R.id.switch_to_signup_activity_button)

        //Set OnClickListener for each buttons
        loginBtn.setOnClickListener{
            //TODO Check email/password condition
            auth.signInWithEmailAndPassword(emailTextEdit.editText?.text.toString(), passwordTextEdit.editText?.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful){
                        //Switch to MainActivity
                        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT)
                        startMainActivity()
                    } else {
                        Toast.makeText(this, "Something is wrong, please try again.", Toast.LENGTH_SHORT)
                    }
                }
        }



        signupBtn.setOnClickListener {
            val signupIntent = Intent(this, SignupActivity::class.java)

            /**
             * If email is filled then add bundle
             */

            startActivity(signupIntent)
        }


    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
