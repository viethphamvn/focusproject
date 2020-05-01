package com.example.focusproject

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.focusproject.adapters.ViewPagerAdapter
import com.example.focusproject.fragments.CountdownFragment
import com.example.focusproject.fragments.ExcerciseProgressFragment
import com.example.focusproject.fragments.ImageViewerFragment
import com.example.focusproject.fragments.VideoViewerFragment
import com.example.focusproject.models.Excercise
import com.example.focusproject.tools.OnSwipeTouchListener
import kotlinx.android.synthetic.main.activity_routine_edit.*
import kotlinx.android.synthetic.main.activity_start_routine.*
import org.w3c.dom.Text


class StartRoutineActivity : AppCompatActivity() {

    private lateinit var controlLayout: TextView
    private lateinit var ActiveRoutineList: ArrayList<Excercise>
    private var selectedDate = 0
    private var currentWorkoutPosition : Int = 0
    private var fragmentHolder : ArrayList<Fragment> = ArrayList()
    private lateinit var progressBar : ProgressBar
    private lateinit var excerciseNameTextView : TextView
    private var totalSetFinished : Int = 0
    private var totalSetRequired: Int = 0
    private var isPaused : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_routine)

        //Get the data from caller
        ActiveRoutineList = intent.getSerializableExtra("routine") as ArrayList<Excercise>
        selectedDate = intent.getIntExtra("date", 2)

        setUpRoutineList()

        progressBar = findViewById(R.id.progress_horizontal)
        progressBar.max = ActiveRoutineList.size
        progressBar.min = 0

        excerciseNameTextView = findViewById(R.id.excerciseNameTextView)

        setUpFragmentPager()
        startWorkout(currentWorkoutPosition)

        controlLayout = findViewById(R.id.controlBar)
        controlLayout.setOnTouchListener(object :OnSwipeTouchListener(this) {
            override fun onSwipeTop() {
                isPaused = false

                findViewById<TextView>(R.id.controlBar).apply {
                    setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
                    text = "PAUSE"
                }

                if (ActiveRoutineList.get(currentWorkoutPosition).vidUrl != "") {
                    (fragmentHolder.get(currentWorkoutPosition) as VideoViewerFragment).playVideo()
                }
                var fragment =
                    supportFragmentManager.findFragmentByTag("countDownFragment") as CountdownFragment
                if (fragment != null) {
                    fragment.startTimer()
                }
            }

            override fun onSwipeRight() {
                nextExcercise()
            }

            override fun onSwipeLeft() {
//                decreaseCurrentWorkoutPosition()
//                excerciseMediaViewPager.currentItem = currentWorkoutPosition
//                updateProgressBar(currentWorkoutPosition)
            }

            override fun onSwipeBottom() {
                    isPaused = true

                    findViewById<TextView>(R.id.controlBar).apply {
                        setBackgroundColor(resources.getColor(R.color.darkgrey))
                        text = "RESUME"
                    }

                    if (ActiveRoutineList.get(currentWorkoutPosition).vidUrl != "") {
                        (fragmentHolder.get(currentWorkoutPosition) as VideoViewerFragment).pauseVideo()
                    }
                    var fragment =
                        supportFragmentManager.findFragmentByTag("countDownFragment") as CountdownFragment
                    if (fragment != null) {
                        fragment.pauseTimer()
                    }
            }
        })
    }

    private fun startWorkout(position: Int){
       updateProgressBar(position)
        ActiveRoutineList.get(position).apply {
            excerciseNameTextView.text = this.name

            totalSetRequired = this.set

            //If excercise has vid --> Play
            if (this.vidUrl != ""){
                (fragmentHolder.get(position) as VideoViewerFragment).playVideo()
            }
            //Start timer if it's timed workout
            var fragment = supportFragmentManager.findFragmentByTag("countDownFragment")
            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
            }
            if (this.isTimed){
                supportFragmentManager.beginTransaction()
                    .add(R.id.clockContainer, CountdownFragment.newInstance(this.duration, this.isRestTime),"countDownFragment")
                    .commit()
            }
            fragment = supportFragmentManager.findFragmentByTag("progressFragment")
            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
            }
            if (this.set > 0){
                supportFragmentManager.beginTransaction()
                    .add(R.id.progressContainer, ExcerciseProgressFragment.newInstance(totalSetFinished+1, totalSetRequired, 12),"progressFragment")
                    .commit()
            }
        }
    }

    fun nextExcercise(){
        if (totalSetFinished+1 < totalSetRequired){
            totalSetFinished++
            //Call nextExcercise when clock hits 0
            startWorkout(currentWorkoutPosition)
        } else {
            totalSetFinished = 0
            //Pause or Stop video for previous fragment
            if (ActiveRoutineList.get(currentWorkoutPosition).vidUrl != "") {
                (fragmentHolder.get(currentWorkoutPosition) as VideoViewerFragment).pauseVideo()
            }
            increaseCurrentWorkoutPosition()
            excerciseMediaViewPager.currentItem = currentWorkoutPosition
            startWorkout(currentWorkoutPosition)
        }
    }

    fun playVideo(){
        if (ActiveRoutineList.get(currentWorkoutPosition).vidUrl != ""){
            (fragmentHolder.get(currentWorkoutPosition) as VideoViewerFragment).playVideo()
        }
    }

    private fun setUpFragmentPager(){
        var viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        ActiveRoutineList.forEach {
            if (it.img != "" ){
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

    private fun decreaseCurrentWorkoutPosition(){
        if (currentWorkoutPosition > 0){
            --currentWorkoutPosition
        }
    }

    private fun increaseCurrentWorkoutPosition(){
        if (currentWorkoutPosition < ActiveRoutineList.size){
            ++currentWorkoutPosition
        }
    }

    private fun updateProgressBar(progress: Int){
        progressBar.setProgress(progress+1)
    }

    private fun setUpRoutineList(){
        var tempList : ArrayList<Excercise> = ArrayList()

        ActiveRoutineList.forEach {
            if (it.isRestTime){
                tempList.add(it)
            } else {
                var readyTime = Excercise("GET READY","",10,it.img,it.vidUrl,false,true,0)
                tempList.add(readyTime)
                tempList.add(it)
            }
        }
        ActiveRoutineList = tempList
    }

}
