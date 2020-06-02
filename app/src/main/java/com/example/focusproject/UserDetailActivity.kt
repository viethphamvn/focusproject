package com.example.focusproject

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.focusproject.adapters.ViewPagerAdapter
import com.example.focusproject.fragments.RoutineListFragment
import com.example.focusproject.models.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_detail.*

private const val routineListFragmentTag = "routineListFragmentInUserDetailActivity"
class UserDetailActivity : AppCompatActivity() {
    private lateinit var user : User
    private val tabTitle = arrayOf("User's Routines", "Saved Routines")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        user = intent.getSerializableExtra("user") as User

        findViewById<TextView>(R.id.username_textview).text = user.username
        var url = if (user.profilePictureUri != ""){
            user.profilePictureUri
        } else {
            R.drawable.ic_asset_3
        }
        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(findViewById(R.id.user_profile_picture))

        findViewById<TextView>(R.id.followedTextView).text = user.follower.size.toString()
        findViewById<TextView>(R.id.followingTextView).text = user.following.size.toString()

        var followBtn = findViewById<Button>(R.id.followButton)
        if (user.follower.contains(User.currentUser.id)){
            followBtn.apply {
                text = "unfollow"
                setBackgroundColor(getColor(R.color.darkgrey))
            }
        } else {
            if (user.id == User.currentUser.id){
                followBtn.visibility = View.GONE
            } else {
                followBtn.apply {
                    text = "follow"
                    setBackgroundColor(getColor(R.color.colorPrimaryDark))
                }
            }
        }

        followBtn.setOnClickListener {
                if (user.follower.contains(User.currentUser.id)) {
                    FirebaseFirestore.getInstance().collection("Users")
                        .document(User.currentUser.id)
                        .update("following", FieldValue.arrayRemove(user.id))
                        .addOnSuccessListener {
                            User.currentUser.following.remove(user.id)

                            FirebaseFirestore.getInstance().collection("Users")
                                .document(user.id)
                                .update("follower", FieldValue.arrayRemove(User.currentUser.id))
                                .addOnSuccessListener {
                                    user.follower.remove(User.currentUser.id)
                                    followBtn.apply {
                                        text = "follow"
                                        setBackgroundColor(getColor(R.color.colorPrimaryDark))
                                    }
                                    followedTextView.text =
                                        "${followedTextView.text.toString().toLong() - 1}"
                                }
                        }
                } else {
                    FirebaseFirestore.getInstance().collection("Users")
                        .document(User.currentUser.id)
                        .update("following", FieldValue.arrayUnion(user.id))
                        .addOnSuccessListener {
                            FirebaseFirestore.getInstance().collection("Users")
                                .document(user.id)
                                .update(
                                    "follower",
                                    FieldValue.arrayUnion(User.currentUser.id)
                                )
                                .addOnSuccessListener {
                                    User.currentUser.following.add(user.id)

                                    user.follower.add(User.currentUser.id)
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


        //Set Up View Pager and Tab Layout
        var fragments = ArrayList<Fragment>()
        fragments.add(RoutineListFragment.newInstance(user))
        fragments.add(RoutineListFragment.newInstance(user, "savedRoutines"))

        var viewpager = findViewById<ViewPager2>(R.id.viewpager)
        viewpager.adapter = ViewPagerAdapter(2, fragments, this)

        var tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }
}
