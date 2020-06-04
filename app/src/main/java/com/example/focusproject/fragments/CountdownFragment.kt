package com.example.focusproject.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.focusproject.R
import com.example.focusproject.StartRoutineActivity
import java.util.*


class CountdownFragment : Fragment() {
    private var duration: Long = 0
    private var countdownTextView : TextView? = null
    private var countdownTimer : CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_countdown, container, false)
        countdownTextView = view.findViewById(R.id.countDownTextView)
        return view
    }

     fun startTimer(){
         createCountDownTimer()
         if (activity != null && !(activity as StartRoutineActivity).isPaused) {
             countdownTimer?.start()
         }
    }

    private fun createCountDownTimer(){
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
    }

    fun startTimerForImage(){
        createCountDownTimer()
        countdownTimer?.start()
    }

    fun updateTextView(current: Long, duration: Long){
        val remainingTimeInMillis = duration - current
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
        countdownTextView!!.text = getString(R.string.paused)
    }

    private fun updateTextView(remainingTimeInMillis : Long){
        val minutes = (remainingTimeInMillis / 1000) / 60
        val seconds = (remainingTimeInMillis / 1000) % 60
        val displayString = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds)
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
