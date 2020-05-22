package com.example.focusproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.focusproject.fragments.NewFeedFragment
import com.example.focusproject.fragments.RoutinesListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var firebaseUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (firebaseUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
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
        findViewById<CircleImageView>(R.id.user_profile_button).setOnClickListener{
            startActivity(Intent(this, UserProfileActivity::class.java))
        }
        findViewById<CircleImageView>(R.id.friendsButton).setOnClickListener{
            startActivity(Intent(this, UserBrowsingActivity::class.java))
        }
    }

    private fun addNewFeedFragment() {
        var newFeedFragment = supportFragmentManager.findFragmentByTag(getString(R.string.newFeedFragmentTag))
        if (newFeedFragment != null){
            supportFragmentManager.beginTransaction().remove(newFeedFragment)
                .commit()
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, NewFeedFragment.newInstance(),getString(R.string.newFeedFragmentTag))
            .commit()
    }

    private fun addWeeklyRoutineFragment(){
        var calendar = Calendar.getInstance()
        var todayDate = calendar.get(Calendar.DAY_OF_WEEK)
        var routineFragment = supportFragmentManager.findFragmentByTag("routineFragment")
        if (routineFragment != null){
            supportFragmentManager.beginTransaction().remove(routineFragment)
                .commit()
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, RoutinesListFragment.newInstance(todayDate),"routineFragment")
            .commit()
    }
}
