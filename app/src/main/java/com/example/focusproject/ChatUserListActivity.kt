package com.example.focusproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.focusproject.models.User
import com.example.focusproject.tools.CreateUser
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class ChatUserListActivity : AppCompatActivity() {

    private lateinit var userListRecyclerView : RecyclerView
    private lateinit var userListRecyclerViewAdapter : GroupAdapter<GroupieViewHolder>
    private var friendList = ArrayList<UserItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_user_list)

        userListRecyclerView = findViewById<RecyclerView>(R.id.user_list_recycler_view)
        userListRecyclerViewAdapter = GroupAdapter<GroupieViewHolder>()
        userListRecyclerView.adapter = userListRecyclerViewAdapter
        userListRecyclerViewAdapter.setOnItemClickListener { item, view ->
            var intent = Intent(view.context, ChatWindowActivity::class.java)
            intent.putExtra("user", (item as UserItem).user)
            startActivity(intent)
        }

        getFriends()


        val searchBtn = findViewById<ImageButton>(R.id.searchbtn)
        val searchText = findViewById<EditText>(R.id.searchEditText)

        searchBtn.setOnClickListener {
            findFriends((searchText as TextView).text.toString())
        }

        searchText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == ""){
                    getFriends()
                } else {
                    findFriends(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })


    }

    private fun findFriends(searchText: String){
        var tempList = ArrayList<UserItem>()

        if (searchText != "") {
            for (userItem in friendList) {
                if (userItem.user.username.contains(searchText)) {
                    tempList.add(userItem)
                }
            }

            if (tempList.size > 0) {
                userListRecyclerViewAdapter.clear()
                userListRecyclerViewAdapter.addAll(tempList)
            } else {
                userListRecyclerViewAdapter.clear()
                //Toast.makeText(this, getString(R.string.noresultfound), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFriends() {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Users").document(User.currentUser.id)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.get("following") != null){
                    val tempList = (snapshot.get("following") as ArrayList<String>)
                    if (tempList.size > 0){
                        friendList.clear()
                        userListRecyclerViewAdapter.clear()
                        for (userId in tempList) {
                            getUserInfo(userId)
                        }
                    } else {
                        friendList.clear()
                        userListRecyclerViewAdapter.clear()
                    }
                }
            }
    }

    private fun getUserInfo(id: String){
        FirebaseFirestore.getInstance().collection("Users").document(id)
            .get()
            .addOnSuccessListener {
                friendList.add(UserItem(CreateUser.createUser(it), this))
                userListRecyclerViewAdapter.add(friendList[friendList.size-1])
            }
    }
}

class UserItem(val user: User, val context: Context) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.user_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Glide.with(context)
            .load(user.profilePictureUri)
            .centerCrop()
            .into(viewHolder.itemView.findViewById<CircleImageView>(R.id.user_profile_picture))

        viewHolder.itemView.findViewById<TextView>(R.id.username_textview).text = user.username
    }

}