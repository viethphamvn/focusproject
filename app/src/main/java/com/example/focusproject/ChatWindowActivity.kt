package com.example.focusproject

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.focusproject.models.Chat
import com.example.focusproject.models.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class ChatWindowActivity : AppCompatActivity() {
    private var recipient : User? = null
    private var chatList = ArrayList<Chat>()
    lateinit var chatListener : ChildEventListener
    val realtimeDatabase = FirebaseDatabase.getInstance()
    private lateinit var conversationRecyclerView : RecyclerView
    private lateinit var conversationRecyclerViewAdapter: GroupAdapter<GroupieViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_window)

        conversationRecyclerViewAdapter = GroupAdapter<GroupieViewHolder>()
        conversationRecyclerView = findViewById(R.id.conversation_list_recycler_view)
        conversationRecyclerView.adapter = conversationRecyclerViewAdapter
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        conversationRecyclerView.layoutManager = linearLayoutManager

        val messageEditText = findViewById<EditText>(R.id.messageEditText)



        //Get User Info from intent
        if (intent.getSerializableExtra("user") != null){
            recipient = intent.getSerializableExtra("user") as User
        }

        //Change TextView to display user's name
        if (recipient != null){
            findViewById<TextView>(R.id.username_textview).text = recipient!!.username

            //Get chat
            fetchChat()
        }

        //handle send button
        findViewById<ImageButton>(R.id.sendButton).setOnClickListener {
            //Get sent message
            val message = Chat(System.currentTimeMillis(), User.currentUser.id, recipient!!.id, messageEditText.text.toString())
            //clear text in edittext
            messageEditText.text.clear()
            //send message to database
            realtimeDatabase.getReference("/inbox/${recipient!!.id}/${User.currentUser.id}").push().setValue(message)
            realtimeDatabase.getReference("/inbox/${User.currentUser.id}/${recipient!!.id}").push().setValue(message)
        }


    }

    private fun fetchChat() {
        //Fetch chat
        chatListener = realtimeDatabase.getReference("/inbox/${User.currentUser.id}/${recipient!!.id}")
            .addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                println("here")
                val chat = p0.getValue(Chat::class.java)
                if (chat != null) {
                    chatList.add(chat)
                    if (chat.sender == recipient!!.id){
                        conversationRecyclerViewAdapter.add(ChatInItem(recipient!!.profilePictureUri, this@ChatWindowActivity, chat))
                    } else {
                        conversationRecyclerViewAdapter.add(ChatOutItem(User.currentUser.profilePictureUri, this@ChatWindowActivity, chat))
                    }
                    conversationRecyclerView.smoothScrollToPosition(chatList.size)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onStop() {
        super.onStop()
        realtimeDatabase.getReference("/inbox/${User.currentUser.id}/${recipient!!.id}").removeEventListener(chatListener)
    }
}

class ChatInItem(private val imageUrl: String, val context: Context, val chat : Chat) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.inchat
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Glide.with(context)
            .load(imageUrl)
            .centerCrop()
            .into(viewHolder.itemView.findViewById<CircleImageView>(R.id.user_profile_picture))

        viewHolder.itemView.findViewById<TextView>(R.id.chatTextView).text = chat.content

    }

}

class ChatOutItem(private val imageUrl: String, val context: Context, val chat : Chat) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.outchat
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Glide.with(context)
            .load(imageUrl)
            .centerCrop()
            .into(viewHolder.itemView.findViewById<CircleImageView>(R.id.user_profile_picture))

        viewHolder.itemView.findViewById<TextView>(R.id.chatTextView).text = chat.content

    }

}