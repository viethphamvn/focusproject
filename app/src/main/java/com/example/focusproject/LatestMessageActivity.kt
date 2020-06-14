package com.example.focusproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.focusproject.models.Chat
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class LatestMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)

        findViewById<ImageButton>(R.id.searchFriendsButton).setOnClickListener {
            startActivity(Intent(this, ChatUserListActivity::class.java))
        }

        getChat()

    }

    private fun getChat() {
        FirebaseFirestore.getInstance().collection("Chats")
    }
}



class myChat(val chat : Chat) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.outchat
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

}

class theirChat(val chat : Chat) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.inchat
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

}