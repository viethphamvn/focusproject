package com.example.focusproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.focusproject.models.Chat
import com.example.focusproject.models.User
import com.example.focusproject.tools.CreateUser
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class LatestMessageActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private lateinit var dataListener : ChildEventListener
    private var latestChatItemList = ArrayList<ChatItem>()
    private lateinit var recentChatRecyclerView : RecyclerView
    private lateinit var recentChatRecyclerViewAdapter : GroupAdapter<GroupieViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)


        recentChatRecyclerView = findViewById(R.id.latest_chat_recycler_view)
        recentChatRecyclerView.layoutManager = LinearLayoutManager(this)
        recentChatRecyclerViewAdapter = GroupAdapter()
        recentChatRecyclerView.adapter = recentChatRecyclerViewAdapter

        recentChatRecyclerViewAdapter.setOnItemClickListener { item, view ->
            var intent = Intent(view.context, ChatWindowActivity::class.java)
            intent.putExtra("user", (item as ChatItem).user)
            startActivity(intent)
        }


        findViewById<ExtendedFloatingActionButton>(R.id.newChatButton).setOnClickListener {
            startActivity(Intent(this, ChatUserListActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        getLatestChat()
    }

    private fun getLatestChat() {
        latestChatItemList.clear()

        dataListener = database.getReference("/latest/${User.currentUser.id}").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chat = p0.getValue(Chat::class.java)
                for (item in latestChatItemList){
                    if (item.userId == chat!!.sender || item.userId == chat.recipient){
                        if (chat!!.sender == User.currentUser.id && chat.content.startsWith(Chat.ROUTINE_TAG)){
                            item.viewHolder.itemView.findViewById<TextView>(R.id.messageTextView).text = "You sent a routine"
                        } else if (chat!!.sender == User.currentUser.id && chat.content.startsWith(Chat.EXERCISE_TAG)){
                            item.viewHolder.itemView.findViewById<TextView>(R.id.messageTextView).text = "You sent an exercise"
                        } else if (chat.content.startsWith(Chat.EXERCISE_TAG)){
                            item.viewHolder.itemView.findViewById<TextView>(R.id.messageTextView).text = "You received an exercise"
                        } else if (chat.content.startsWith(Chat.ROUTINE_TAG)){
                            item.viewHolder.itemView.findViewById<TextView>(R.id.messageTextView).text = "You received a routine"
                        } else {
                            item.viewHolder.itemView.findViewById<TextView>(R.id.messageTextView).text = chat.content
                        }
                        item.chat.recipient = chat.recipient
                        item.chat.sender = chat.sender
                        item.chat.timeStamp = chat.timeStamp
                        break;
                    }
                }
                latestChatItemList = ArrayList(latestChatItemList.sortedDescending().toList())
                recentChatRecyclerViewAdapter.update(latestChatItemList, true)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chat = p0.getValue(Chat::class.java)
                if (chat != null) {

                    if (chat!!.sender == User.currentUser.id && chat.content.startsWith(Chat.ROUTINE_TAG)){
                        chat.content = "You sent a routine"
                    } else if (chat!!.sender == User.currentUser.id && chat.content.startsWith(Chat.EXERCISE_TAG)){
                        chat.content = "You sent an exercise"
                    } else if (chat.content.startsWith(Chat.EXERCISE_TAG)){
                        chat.content = "You received an exercise"
                    } else if (chat.content.startsWith(Chat.ROUTINE_TAG)){
                        chat.content = "You received a routine"
                    }

                    latestChatItemList.add(ChatItem(chat, this@LatestMessageActivity))
                    latestChatItemList = ArrayList(latestChatItemList.sortedDescending().toList())
                    recentChatRecyclerViewAdapter.update(latestChatItemList, true)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onStop() {
        super.onStop()
        database.getReference("/latest/${User.currentUser.id}").removeEventListener(dataListener)
    }
}



class ChatItem(val chat : Chat, val context : Context) : Item<GroupieViewHolder>(), Comparable<ChatItem>{
    lateinit var user : User
    lateinit var viewHolder : GroupieViewHolder
    var userId = ""

    override fun getLayout(): Int {
        return R.layout.recent_chat_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        this.viewHolder = viewHolder
        userId = if (chat.recipient == User.currentUser.id){
            chat.sender
        } else {
            chat.recipient
        }
        FirebaseFirestore.getInstance().collection("Users").document(userId)
            .get()
            .addOnSuccessListener {
                if (it.data != null){
                    user = CreateUser.createUser(it)
                    if (user != null) {
                        Glide.with(context)
                            .load(user!!.profilePictureUri)
                            .centerCrop()
                            .into(viewHolder.itemView.findViewById<CircleImageView>(R.id.user_profile_picture))

                        viewHolder.itemView.findViewById<TextView>(R.id.username_textview).text = user!!.username
                        viewHolder.itemView.findViewById<TextView>(R.id.messageTextView).text = chat.content
                    }
                }
            }

    }

    override fun compareTo(other: ChatItem): Int {
        return COMPARATOR.compare(this, other)
    }

    companion object {
        var COMPARATOR: Comparator<ChatItem> =
            Comparator.comparingLong{it.chat.timeStamp}
    }

}