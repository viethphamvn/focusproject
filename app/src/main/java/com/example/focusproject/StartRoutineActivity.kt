package com.example.focusproject

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.focusproject.adapters.ViewPagerAdapter
import com.example.focusproject.fragments.CountdownFragment
import com.example.focusproject.fragments.ExcerciseProgressFragment
import com.example.focusproject.fragments.ImageViewerFragment
import com.example.focusproject.fragments.VideoViewerFragment
import com.example.focusproject.models.Exercise
import com.example.focusproject.tools.OnSwipeTouchListener
import kotlinx.android.synthetic.main.activity_start_routine.*


class StartRoutineActivity : AppCompatActivity() {

    private lateinit var controlLayout: TextView
    private lateinit var activeRoutineList: ArrayList<Exercise>
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
        activeRoutineList = intent.getSerializableExtra("routine") as ArrayList<Exercise>
        selectedDate = intent.getIntExtra("date", 2)

        setUpRoutineList()

        progressBar = findViewById(R.id.progress_horizontal)
        progressBar.max = activeRoutineList.size
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

                if (activeRoutineList.get(currentWorkoutPosition).vidId != "") {
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

                    if (activeRoutineList.get(currentWorkoutPosition).vidId != "") {
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

    private fun startWorkout(position: Int){ //Need to rework this section since set is removed
       updateProgressBar(position)
        activeRoutineList.get(position).apply {
            excerciseNameTextView.text = this.name

            //If excercise has vid --> Play
            if (this.vidId != ""){
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
            if (this.rep > 0 || this.weight > 0){
                supportFragmentManager.beginTransaction()
                    .add(R.id.progressContainer, ExcerciseProgressFragment.newInstance(this.weight, this.rep),"progressFragment")
                    .commit()
            }
        }
    }

    fun nextExcercise(){
        //Pause or Stop video for previous fragment
        if (activeRoutineList.get(currentWorkoutPosition).vidId != "") {
            (fragmentHolder.get(currentWorkoutPosition) as VideoViewerFragment).pauseVideo()
        }
        increaseCurrentWorkoutPosition()
        excerciseMediaViewPager.currentItem = currentWorkoutPosition
        startWorkout(currentWorkoutPosition)

    }

    fun playVideo(){
        if (activeRoutineList.get(currentWorkoutPosition).vidId != ""){
            (fragmentHolder.get(currentWorkoutPosition) as VideoViewerFragment).playVideo()
        }
    }

    private fun setUpFragmentPager(){
        var viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        activeRoutineList.forEach {
            if (it.img != "" ){
                fragmentHolder.add(ImageViewerFragment.newInstance(it.img))
            } else {
                fragmentHolder.add(VideoViewerFragment.newInstance(it.vidId))
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
        if (currentWorkoutPosition < activeRoutineList.size){
            ++currentWorkoutPosition
        }
    }

    private fun updateProgressBar(progress: Int){
        progressBar.progress = progress+1
    }

    private fun setUpRoutineList(){
        var tempList : ArrayList<Exercise> = ArrayList()

        activeRoutineList.forEach {
            if (it.isRestTime){
                tempList.add(it)
            } else {
                var readyTime = Exercise("GET READY","","",10,it.img,it.vidId,false,true,it.rep, false, it.weight, it.createdBy)
                tempList.add(readyTime)
                tempList.add(it)
            }
        }
        activeRoutineList = tempList
    }

}
