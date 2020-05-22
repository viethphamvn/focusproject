package com.example.focusproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.focusproject.adapters.FeedRecyclerViewAdapter
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.models.Routine
import com.example.focusproject.models.User
import com.example.focusproject.tools.CreateRoutine
import com.example.focusproject.tools.FireStore
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text

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
            routineRecyclerViewAdapter = FeedRecyclerViewAdapter(routineList)
            adapter = routineRecyclerViewAdapter
        }
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
