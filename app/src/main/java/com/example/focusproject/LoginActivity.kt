package com.example.focusproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Get Views from Layout
        val emailTextEdit : TextInputLayout = findViewById(R.id.email_edittext)
        val passwordTextEdit : TextInputLayout = findViewById(R.id.password_edittext)
        val loginBtn : Button = findViewById(R.id.loginButton)
        val signupBtn : Button = findViewById(R.id.switch_to_signup_activity_button)

        //Set OnClickListener for each buttons
        loginBtn.setOnClickListener{
            //Firebase stuff
        }



        signupBtn.setOnClickListener {
            val signupIntent = Intent(this, SignupActivity::class.java)

            /**
             * If email is filled then add bundle
             */

            startActivity(signupIntent)
        }


    }
}
