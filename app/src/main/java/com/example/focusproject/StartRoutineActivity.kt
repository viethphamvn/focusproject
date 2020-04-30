package com.example.focusproject

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.focusproject.adapters.ViewPagerAdapter
import com.example.focusproject.fragments.CountdownFragment
import com.example.focusproject.fragments.ImageViewerFragment
import com.example.focusproject.fragments.VideoViewerFragment
import com.example.focusproject.models.Excercise
import com.example.focusproject.tools.OnSwipeTouchListener
import kotlinx.android.synthetic.main.activity_routine_edit.*
import kotlinx.android.synthetic.main.activity_start_routine.*


class StartRoutineActivity : AppCompatActivity() {

    private lateinit var controlLayout: FrameLayout
    private lateinit var ActiveRoutineList: ArrayList<Excercise>
    private var selectedDate = 0
    private var currentWorkoutPosition : Int = 0
    private var fragmentHolder : ArrayList<Fragment> = ArrayList()
    private lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_routine)

        //Get the data from caller
        ActiveRoutineList = intent.getSerializableExtra("routine") as ArrayList<Excercise>
        selectedDate = intent.getIntExtra("date", 2)

        progressBar = findViewById(R.id.progress_horizontal)
        progressBar.max = ActiveRoutineList.size
        progressBar.min = 0

        setUpFragmentPager()
        startWorkout(currentWorkoutPosition)

        controlLayout = findViewById(R.id.controlBar)
        controlLayout.setOnTouchListener(object :OnSwipeTouchListener(this) {
            override fun onSwipeTop() {
            }

            override fun onSwipeRight() {
                nextExcercise()
            }

            override fun onSwipeLeft() {
                excerciseMediaViewPager.setCurrentItem(--currentWorkoutPosition)
                progressBar.setProgress(currentWorkoutPosition)
            }

            override fun onSwipeBottom() {
            }

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return super.onTouch(v, event)
            }
        })
    }

    private fun startWorkout(position: Int){
        progressBar.setProgress(position+1, true)

        ActiveRoutineList.get(position).apply {
            //If excercise has vid --> Play
            if (this.vidUrl != ""){
                //(fragmentHolder.get(position) as VideoViewerFragment).playVideo()
            }
            //Start timer if it's timed workout
            if (this.isTimed){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.multi_purposeInfoContainer, CountdownFragment.newInstance(this.duration, this.isRestTime),"countDownFragment")
                    .commit()
            } else {
                var fragment = supportFragmentManager.findFragmentByTag("countDownFragment")
                if (fragment != null) {
                    supportFragmentManager.beginTransaction()
                        .remove(fragment)
                        .commit()
                }

            }
        }
    }

    fun nextExcercise(){
        //Pause or Stop video for previous fragment
        if (ActiveRoutineList.get(currentWorkoutPosition).vidUrl != ""){
            (fragmentHolder.get(currentWorkoutPosition) as VideoViewerFragment).pauseVideo()
        }
        excerciseMediaViewPager.currentItem = ++currentWorkoutPosition
        startWorkout(currentWorkoutPosition)
    }

    fun playVideo(){
        if (ActiveRoutineList.get(currentWorkoutPosition).vidUrl != ""){
            (fragmentHolder.get(currentWorkoutPosition) as VideoViewerFragment).playVideo()
        }
    }

    private fun setUpFragmentPager(){
        var viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        ActiveRoutineList.forEach {
            if (it.img != ""){
                fragmentHolder.add(ImageViewerFragment.newInstance(it.img))
            } else {
                fragmentHolder.add(VideoViewerFragment.newInstance(it.vidUrl))
            }
        }

        fragmentHolder.forEach {
            viewPagerAdapter.addFragment(it, "")
        }

        excerciseMediaViewPager.adapter = viewPagerAdapter
    }

}
