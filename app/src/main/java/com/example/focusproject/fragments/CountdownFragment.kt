package com.example.focusproject.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.focusproject.R
import com.example.focusproject.StartRoutineActivity
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class CountdownFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var duration: Long = 0
    private var countdownTextView : TextView? = null
    private var countdownTimer : CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_countdown, container, false)
        countdownTextView = view.findViewById(R.id.countDownTextView)
        return view
    }

     fun startTimer(){
         countdownTimer = object : CountDownTimer(duration, 1000) {
             override fun onFinish() {
                 countdownTextView!!.setBackgroundColor(Color.parseColor("#C62828"))
                 if (activity != null) {
                     (activity as StartRoutineActivity).nextExcercise()
                 }
             }

             override fun onTick(millisUntilFinished: Long) {
                 updateTextView(millisUntilFinished)
                 duration = millisUntilFinished
             }

         }
         if (activity != null && !(activity as StartRoutineActivity).isPaused) {
             countdownTimer?.start()
         }
    }

    fun startTimerForImage(){
        countdownTimer = object : CountDownTimer(duration, 1000) {
            override fun onFinish() {
                countdownTextView!!.setBackgroundColor(Color.parseColor("#C62828"))
                if (activity != null) {
                    (activity as StartRoutineActivity).nextExcercise()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                updateTextView(millisUntilFinished)
                duration = millisUntilFinished
            }

        }
        countdownTimer?.start()
    }

    fun updateTextView(current: Long, duration: Long){
        var remainingTimeInMillis = duration - current
        val minutes: Long = (remainingTimeInMillis / (60 * 1000))
        val seconds: Long = (remainingTimeInMillis / 1000 % 60)
        if (countdownTextView != null) {
            countdownTextView!!.text =
                String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
    }

    fun setTimer(duration: Long){
        this.duration = duration * 1000
    }

    fun pauseTimer(){
        if (countdownTimer != null) {
            countdownTimer?.cancel()
        }
        countdownTextView!!.text = "PAUSED"
    }

    private fun updateTextView(remainingTimeInMillis : Long){
        var minutes = (remainingTimeInMillis / 1000) / 60
        var seconds = (remainingTimeInMillis / 1000) % 60
        var displayString = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds)
        countdownTextView!!.text = displayString

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CountdownFragment()
    }

    override fun onPause() {
        super.onPause()
        if (countdownTimer != null) {
            countdownTimer?.cancel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownTimer = null
    }
}
