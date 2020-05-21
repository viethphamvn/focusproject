package com.example.focusproject

import android.os.Bundle
import android.provider.MediaStore
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
import com.example.focusproject.tools.CreateExercise
import com.example.focusproject.tools.OnSwipeTouchListener
import kotlinx.android.synthetic.main.activity_start_routine.*
import java.time.Duration


class StartRoutineActivity : AppCompatActivity() {

    private lateinit var controlLayout: TextView
    private lateinit var activeRoutineList: ArrayList<Exercise>
    private var selectedDate = 0
    private var currentWorkoutPosition : Int = 0
    private lateinit var progressBar : ProgressBar
    private lateinit var exerciseNameTextView : TextView
    var isPaused : Boolean = false
    var isImage: Boolean = false
    private var fragmentCountDown: Fragment? = null
    private var fragmentVideoPlayer: Fragment? = null
    private var fragmentImageViewer: Fragment? = null

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

        exerciseNameTextView = findViewById(R.id.excerciseNameTextView)

        controlLayout = findViewById(R.id.controlBar)
        controlLayout.setOnTouchListener(object :OnSwipeTouchListener(this) {
            override fun onSwipeTop() {
                isPaused = false

                findViewById<TextView>(R.id.controlBar).apply {
                    setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
                    text = "PAUSE"
                }

                playVideo()
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
                    pauseVideo()
            }
        })

        startWorkout(currentWorkoutPosition)
    }

    private fun startWorkout(position: Int){

        updateProgressBar(position)

        fragmentVideoPlayer = supportFragmentManager.findFragmentByTag("videofragment")
        if (fragmentVideoPlayer != null){
            supportFragmentManager.beginTransaction()
                .remove(fragmentVideoPlayer as VideoViewerFragment)
                .commit()
        }
        fragmentImageViewer = supportFragmentManager.findFragmentByTag("imagefragment")
        if (fragmentImageViewer != null){
            supportFragmentManager.beginTransaction()
                .remove(fragmentImageViewer as ImageViewerFragment)
                .commit()
        }

        if (activeRoutineList[position].vidId != "") {
            fragmentVideoPlayer = VideoViewerFragment.newInstance(activeRoutineList[position].vidId)
            supportFragmentManager.beginTransaction()
                .add(R.id.excerciseMediaContainer, fragmentVideoPlayer as VideoViewerFragment, "videofragment")
                .commit()
            isImage = false
        } else {
            fragmentImageViewer = ImageViewerFragment.newInstance(activeRoutineList[position].img)
            supportFragmentManager.beginTransaction()
                .add(R.id.excerciseMediaContainer, fragmentImageViewer as ImageViewerFragment, "imagefragment")
                .commit()
            isImage = true
            print("Is Image")
        }

        activeRoutineList.get(position).apply {
            excerciseNameTextView.text = this.name

            //Start timer if it's timed workout
            fragmentCountDown = supportFragmentManager.findFragmentByTag("countDownFragment")
            if (fragmentCountDown != null) {
                supportFragmentManager.beginTransaction()
                    .remove(fragmentCountDown!!)
                    .commit()
            }
            if (this.isTimed){
                fragmentCountDown = CountdownFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.clockContainer, fragmentCountDown as CountdownFragment,"countDownFragment")
                    .commit()
                (fragmentCountDown as CountdownFragment).setTimer(activeRoutineList[position].duration)
                println("Set timer to ${activeRoutineList[position].duration}")
                if (!isImage){(fragmentCountDown as CountdownFragment).startTimer()} else {(fragmentCountDown as CountdownFragment).startTimerForImage()}
            }

            var fragmentProgress = supportFragmentManager.findFragmentByTag("progressFragment")
            if (fragmentProgress != null) {
                supportFragmentManager.beginTransaction()
                    .remove(fragmentProgress)
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
        increaseCurrentWorkoutPosition()
        startWorkout(currentWorkoutPosition)
    }

    fun playVideo(){
        fragmentCountDown =
            supportFragmentManager.findFragmentByTag("countDownFragment")
        if (fragmentCountDown != null && activeRoutineList.get(currentWorkoutPosition).isRestTime) {
            (fragmentCountDown as CountdownFragment).startTimer()
        }

        if (activeRoutineList.get(currentWorkoutPosition).vidId != ""){
            (fragmentVideoPlayer as VideoViewerFragment).playVideo()
        }
    }

    fun videoIsPlaying(currentSecond: Long, duration: Long){
        if (fragmentCountDown != null && !activeRoutineList[currentWorkoutPosition].isRestTime) {
            (fragmentCountDown as CountdownFragment).updateTextView(currentSecond, duration)
        }
    }

    fun pauseVideo(){
        if (activeRoutineList.get(currentWorkoutPosition).vidId != ""){
            (fragmentVideoPlayer as VideoViewerFragment).pauseVideo()
        }
        if (fragmentCountDown as CountdownFragment != null) {
            (fragmentCountDown as CountdownFragment).pauseTimer()
        }
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
                if (it.img == "") {
                    it.img =
                        "https://68.media.tumblr.com/6e42c179cefc75b1cc8e0cc52a374b84/tumblr_oqstb6MdLH1w9ge4xo1_400.gif"
                }
                tempList.add(it)
            } else {
                var readyTime = CreateExercise.createReadyExercise(10, it)
                tempList.add(readyTime)
                tempList.add(it)
            }
        }
        activeRoutineList = tempList
    }

}
