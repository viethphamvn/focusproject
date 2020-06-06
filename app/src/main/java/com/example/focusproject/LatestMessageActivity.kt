package com.example.focusproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class LatestMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)

        findViewById<ImageButton>(R.id.searchFriendsButton).setOnClickListener {
            startActivity(Intent(this, ChatUserListActivity::class.java))
        }

    }
}