package com.example.focusproject

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.example.focusproject.adapters.ExerciseRecyclerViewAdapter
import com.example.focusproject.adapters.FeedRecyclerViewAdapter
import com.example.focusproject.models.Exercise
import com.example.focusproject.models.Routine
import com.example.focusproject.models.User
import com.example.focusproject.tools.CreateExercise
import com.example.focusproject.tools.CreateRoutine
import com.example.focusproject.tools.FireStore
import com.google.firebase.firestore.FieldValue
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : AppCompatActivity() {
    private lateinit var routineRecyclerViewAdapter : FeedRecyclerViewAdapter
    private var routineList = ArrayList<Routine>()
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        user = intent.getSerializableExtra("user") as User

        findViewById<TextView>(R.id.username_textview).text = user.username
        var url = ""
        url = if (user.profilePicture != ""){
            user.profilePicture
        } else {
            //use default profile picture
            "https://cdn.vox-cdn.com/thumbor/vbxRVJGeYs4rAJp_dlN2Swx3eKg=/1400x1400/filters:format(png)/cdn.vox-cdn.com/uploads/chorus_asset/file/19921093/mgidarccontentnick.comc008fa9d_d.png"
        }
        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(findViewById(R.id.user_profile_picture))

        findViewById<TextView>(R.id.followedTextView).text = user.follower.size.toString()
        findViewById<TextView>(R.id.followingTextView).text = user.following.size.toString()

        findViewById<RecyclerView>(R.id.routine_recycler_list_view).apply {
            layoutManager = LinearLayoutManager(context)
            routineRecyclerViewAdapter = FeedRecyclerViewAdapter(routineList){item -> doClick(item)}
            adapter = routineRecyclerViewAdapter
        }

        var followBtn = findViewById<Button>(R.id.followButton)
        if (user.follower.contains(FireStore.currentUser!!.uid)){
            followBtn.apply {
                text = "unfollow"
                setBackgroundColor(getColor(R.color.darkgrey))
            }
        } else {
            if (user.id == FireStore.currentUser!!.uid){
                followBtn.visibility = View.GONE
            } else {
                followBtn.apply {
                    text = "follow"
                    setBackgroundColor(getColor(R.color.colorPrimaryDark))
                }
            }
        }

        followBtn.setOnClickListener {
                if (user.follower.contains(FireStore.currentUser!!.uid)) {
                    FireStore.fireStore.collection("Users")
                        .document(FireStore.currentUser!!.uid)
                        .update("following", FieldValue.arrayRemove(user.id))
                        .addOnSuccessListener {
                            FireStore.fireStore.collection("Users")
                                .document(user.id)
                                .update("follower", FieldValue.arrayRemove(FireStore.currentUser!!.uid))
                                .addOnSuccessListener {
                                    user.follower.remove(FireStore.currentUser!!.uid)
                                    followBtn.apply {
                                        text = "follow"
                                        setBackgroundColor(getColor(R.color.colorPrimaryDark))
                                    }
                                    followedTextView.text =
                                        "${followedTextView.text.toString().toLong() - 1}"
                                }
                        }
                } else {
                    FireStore.fireStore.collection("Users")
                        .document(FireStore.currentUser!!.uid)
                        .update("following", FieldValue.arrayUnion(user.id))
                        .addOnSuccessListener {
                            FireStore.fireStore.collection("Users")
                                .document(user.id)
                                .update(
                                    "follower",
                                    FieldValue.arrayUnion(FireStore.currentUser!!.uid)
                                )
                                .addOnSuccessListener {
                                    user.follower.add(FireStore.currentUser!!.uid)
                                    followBtn.apply {
                                        text = "unfollow"
                                        setBackgroundColor(getColor(R.color.darkgrey))
                                        followedTextView.text =
                                            "${followedTextView.text.toString().toLong() + 1}"
                                    }
                                }
                        }
                }
        }
    }

    private fun doClick(item: Routine) {
        var intent = Intent(this, RoutineDetailActivity::class.java)
        intent.putExtra("routine", item)
        startActivity(intent)
    }

    private fun exerciseOnClick(item: Exercise) {

    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData(){
        FireStore.fireStore.collection("Routines")
            .get()
            .addOnSuccessListener { result ->
                var tempArray = ArrayList<Routine>()
                routineRecyclerViewAdapter.notifyDataSetChanged()
                for (routine in result) {
                    //Check if routine is belong to followed users
                    if (routine.get("createdBy").toString() == user.id) {
                        tempArray.add(CreateRoutine.createRoutine(routine))
                    }
                }
                if (tempArray.size > 0) {
                    routineList = ArrayList(tempArray.sortedDescending().toList())
                    routineRecyclerViewAdapter.setNewData(routineList)
                }
            }
    }
}
