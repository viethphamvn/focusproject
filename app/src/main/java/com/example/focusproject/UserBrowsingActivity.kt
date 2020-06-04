package com.example.focusproject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focusproject.adapters.UserRecyclerViewAdapter
import com.example.focusproject.models.User
import com.example.focusproject.tools.CreateUser
import com.google.firebase.firestore.FirebaseFirestore

class UserBrowsingActivity : AppCompatActivity() {
    private lateinit var userRecyclerViewAdapter : UserRecyclerViewAdapter
    private var userList = ArrayList<User>()
    private lateinit var emptyBannerLayout : LinearLayout
    private lateinit var resultBannerTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_browsing)

        findViewById<RecyclerView>(R.id.user_list_recycler_view).apply {
            layoutManager = GridLayoutManager(this@UserBrowsingActivity, 3)
            userRecyclerViewAdapter = UserRecyclerViewAdapter(userList){item -> onUserClick(item)}
            adapter = userRecyclerViewAdapter
        }

        emptyBannerLayout = findViewById(R.id.emptyLayout)
        resultBannerTextView = findViewById(R.id.TextView)

        val searchBtn = findViewById<ImageButton>(R.id.searchbtn)
        val searchText = findViewById<EditText>(R.id.searchEditText)

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

    }

    override fun onResume() {
        super.onResume()

        getFriends()
    }

    private fun onUserClick(item: User) {
        val intent = Intent(this, UserDetailActivity::class.java)
        intent.putExtra("user", item)
        startActivity(intent)
    }



    private fun findFriends(searchText: String){
        if (searchText != ""){
            FirebaseFirestore.getInstance().collection("Users")
                .get()
                .addOnSuccessListener { result ->
                    val tempList = ArrayList<String>()
                    for (userDoc in result){
                        val name = userDoc.get("username").toString()
                        if (name.contains(searchText)){
                            tempList.add(userDoc.get("id").toString())
                        }
                    }
                    if (tempList.size > 0) {
                        emptyBannerLayout.visibility = View.GONE
                        resultBannerTextView.text = getString(R.string.searchresult)

                        userList.clear()
                        userRecyclerViewAdapter.notifyDataSetChanged()
                        for (id in tempList) {
                            getUserInfo(id)
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.noresultfound), Toast.LENGTH_SHORT).show()
                    }
                }
        } else {

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
                        emptyBannerLayout.visibility = View.GONE
                        resultBannerTextView.text = getString(R.string.yourfriends)

                        userList.clear()
                        userRecyclerViewAdapter.notifyDataSetChanged()
                        for (userId in tempList) {
                           getUserInfo(userId)
                        }
                    } else {
                        userList.clear()
                        userRecyclerViewAdapter.notifyDataSetChanged()
                        emptyBannerLayout.visibility = View.VISIBLE
                        resultBannerTextView.text = ""
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
