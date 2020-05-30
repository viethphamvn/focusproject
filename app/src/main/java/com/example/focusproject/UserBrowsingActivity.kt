package com.example.focusproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focusproject.adapters.UserRecyclerViewAdapter
import com.example.focusproject.models.User
import com.example.focusproject.tools.CreateUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserBrowsingActivity : AppCompatActivity() {
    private lateinit var userRecyclerViewAdapter : UserRecyclerViewAdapter
    private var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_browsing)

        findViewById<RecyclerView>(R.id.user_list_recycler_view).apply {
            layoutManager = GridLayoutManager(this@UserBrowsingActivity, 3)
            userRecyclerViewAdapter = UserRecyclerViewAdapter(userList){item -> onUserClick(item)}
            adapter = userRecyclerViewAdapter
        }

        var searchBtn = findViewById<ImageButton>(R.id.searchbtn)
        var searchText = findViewById<EditText>(R.id.searchEditText)

        searchBtn.setOnClickListener {
            findFriends((searchText as TextView).text.toString())
        }

        searchText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == ""){
                    getFriends()
                } //else {
//                    findFriends(s.toString())
//                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        getFriends()
    }

    private fun onUserClick(item: User) {
        var intent = Intent(this, UserDetailActivity::class.java)
        intent.putExtra("user", item)
        startActivity(intent)
    }

    private fun findFriends(searchText: String){
        if (searchText != ""){
            FirebaseFirestore.getInstance().collection("Users")
                .get()
                .addOnSuccessListener { result ->
                    var tempList = ArrayList<String>()
                    for (userDoc in result){
                        var name = userDoc.get("username").toString()
                        if (name.contains(searchText)){
                            tempList.add(userDoc.get("id").toString())
                        }
                    }
                    if (tempList.size > 0) {
                        userList.clear()
                        userRecyclerViewAdapter.notifyDataSetChanged()
                        for (id in tempList) {
                            getUserInfo(id)
                        }
                    }
                }
        } else {

        }
    }

    private fun getFriends() {
        var fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.get("following") != null){
                    var tempList = (snapshot.get("following") as ArrayList<String>)
                    if (tempList.size > 0){
                        userList.clear()
                        userRecyclerViewAdapter.notifyDataSetChanged()
                        for (userId in tempList) {
                           getUserInfo(userId)
                        }
                    }
                }
            }
    }

    private fun getUserInfo(id: String){
        FirebaseFirestore.getInstance().collection("Users").document(id)
            .get()
            .addOnSuccessListener {
                userList.add(CreateUser.createUser(it))
                userRecyclerViewAdapter.notifyItemInserted(userList.size - 1)
            }
    }
}
