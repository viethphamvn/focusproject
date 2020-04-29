package com.example.focusproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.focusproject.fragments.routine_activity_fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var firebaseUser = FirebaseAuth.getInstance().currentUser

    override fun onStart() {
        super.onStart()

        if (firebaseUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addRoutineFragment()

        //BottomNavigation Handle
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_routine -> {
                    addRoutineFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_excercise -> {
                    //Implement Excercise Fragment
                }
            }
            false
        })

        //Setup Views
        findViewById<FloatingActionButton>(R.id.floatingActionButton_addAction).setOnClickListener {
            startActivity(Intent(this, ExcercisePickerActivity::class.java))
        }
        findViewById<CircleImageView>(R.id.user_profile_button).setOnClickListener{
            startActivity(Intent(this, UserProfileActivity::class.java))
        }
    }

    private fun addRoutineFragment(){
        var calendar = Calendar.getInstance()
        var todayDate = calendar.get(Calendar.DAY_OF_WEEK)
        var routineFragment = supportFragmentManager.findFragmentByTag("routineFragment")
        if (routineFragment != null){
            supportFragmentManager.beginTransaction().replace(R.id.container, routineFragment,"routineFragment")
                .commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.container, routine_activity_fragment.newInstance(todayDate),"routineFragment")
                .commit()
        }
    }
}
