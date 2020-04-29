package com.example.focusproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_start_routine.*

class StartRoutineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_routine)

        val url = "https://compote.slate.com/images/697b023b-64a5-49a0-8059-27b963453fb1.gif"
        Glide.with(this)  //2
            .load(url) //3
            .centerCrop() //4
            .into(findViewById(R.id.extraImageView)) //8
    }
}
