package com.example.focusproject

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.focusproject.fragments.NewFeedFragment
import com.example.focusproject.fragments.DailyRoutinesListFragment
import com.example.focusproject.fragments.ImageViewerFragment
import com.example.focusproject.models.User
import com.example.focusproject.tools.CreateUser
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var firebaseUser = FirebaseAuth.getInstance().currentUser
    private lateinit var profilePictureView : CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (firebaseUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        profilePictureView = findViewById(R.id.user_profile_picture)

        //Set Up Current User Located in models/User
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { user ->
                if (user != null) {
                    loadingTextView.visibility = View.GONE
                    User.currentUser = CreateUser.createUser(user)

                    if (user["profilePictureUri"] != null) {
                        var uri = user["profilePictureUri"] as String;
                        if (uri != "") {
                            setProfileImage(Uri.parse(uri))
                        }
                    }

                    addWeeklyRoutineFragment()
                    //BottomNavigation Handle
                    bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.action_routine -> {
                                addWeeklyRoutineFragment()
                                return@OnNavigationItemSelectedListener true
                            }
                            R.id.action_new_feed -> {
                                //Implement New Feed Fragment
                                addNewFeedFragment()
                                return@OnNavigationItemSelectedListener true
                            }
                        }
                        false
                    })

                    //Setup Views
                    findViewById<FloatingActionButton>(R.id.floatingActionButton_addAction).setOnClickListener {
                        startActivity(Intent(this, CreateRoutineActivity::class.java))
                    }
                    profilePictureView.setOnClickListener {
                        startActivity(Intent(this, UserProfileActivity::class.java))
                    }
                    findViewById<CircleImageView>(R.id.friendsButton).setOnClickListener {
                        startActivity(Intent(this, UserBrowsingActivity::class.java))
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        setProfileImage(Uri.parse(User.currentUser.profilePictureUri))
    }

    private fun setProfileImage(parse: Uri?) {
        Glide.with(this)
            .load(parse)
            .centerCrop()
            .into(profilePictureView)
    }

    private fun addNewFeedFragment() {
        var newFeedFragment = supportFragmentManager.findFragmentByTag(getString(R.string.newFeedFragmentTag))

        var routineFragment = supportFragmentManager.findFragmentByTag("routineFragment")
        if (routineFragment != null){
            supportFragmentManager.beginTransaction().remove(routineFragment)
                .commit()
        }

//        if (newFeedFragment != null){
//            supportFragmentManager.beginTransaction().remove(newFeedFragment)
//                .commit()
//        }

        if (newFeedFragment == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                NewFeedFragment.newInstance(),
                getString(R.string.newFeedFragmentTag)
            )
                .commit()
        }
    }

    private fun addWeeklyRoutineFragment(){
        var newFeedFragment = supportFragmentManager.findFragmentByTag(getString(R.string.newFeedFragmentTag))
        if (newFeedFragment != null){
            if (newFeedFragment != null){
                supportFragmentManager.beginTransaction().remove(newFeedFragment)
                    .commit()
            }
        }

        var routineFragment = supportFragmentManager.findFragmentByTag("routineFragment")

        if (routineFragment == null) {

            var calendar = Calendar.getInstance()
            var todayDate = calendar.get(Calendar.DAY_OF_WEEK)
            if (routineFragment != null) {
                supportFragmentManager.beginTransaction().remove(routineFragment)
                    .commit()
            }
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                DailyRoutinesListFragment.newInstance(todayDate),
                "routineFragment"
            )
                .commit()
        }
    }
}
