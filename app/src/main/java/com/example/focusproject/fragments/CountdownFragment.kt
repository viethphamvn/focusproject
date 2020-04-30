package com.example.focusproject.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import com.example.focusproject.R
import com.example.focusproject.StartRoutineActivity
import com.google.common.base.Stopwatch
import kotlinx.android.synthetic.main.fragment_countdown.*
import java.time.Duration
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val DURATION = "param1"
private const val COUNT_UP = "param2"

class CountdownFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var duration: Long = 0
    private lateinit var chronometer : Chronometer
    private lateinit var countdownTextView : TextView
    var countUp: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            duration = it.getLong(DURATION)
            countUp = it.getBoolean(COUNT_UP)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_countdown, container, false)
        chronometer = view.findViewById(R.id.chronometer)
        countdownTextView = view.findViewById(R.id.countDownTextView)
        return view
    }

    override fun onStart() {
        super.onStart()
        startTimer()
    }

    fun startTimer(){
        var countdownTimer = object : CountDownTimer(duration, 1000){
            override fun onFinish() {
                if (countUp) {
                    chronometer.visibility = View.VISIBLE
                    countdownTextView.visibility = View.INVISIBLE
                    chronometer.base = SystemClock.elapsedRealtime()
                    chronometer.isCountDown = true
                    chronometer.setBackgroundColor(Color.parseColor("#C62828"))
                    chronometer.start()
                } else {
                    countdownTextView.setBackgroundColor(Color.parseColor("#C62828"))
                    (activity as StartRoutineActivity).nextExcercise()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                updateTextView(millisUntilFinished)
            }

        }
        countdownTimer.start()
    }

    fun setTimer(duration: Long){
        this.duration = duration
    }

    private fun updateTextView(remainingTimeInMillis : Long){
        chronometer.visibility = View.INVISIBLE
        countdownTextView.visibility = View.VISIBLE
        var minutes = (remainingTimeInMillis / 1000) / 60
        var seconds = (remainingTimeInMillis / 1000) % 60
        var displayString = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds)
        countdownTextView.text = displayString

    }


    companion object {
        @JvmStatic
        fun newInstance(duration: Long, countUp: Boolean) =
            CountdownFragment().apply {
                arguments = Bundle().apply {
                    putLong(DURATION, duration)
                    putBoolean(COUNT_UP, countUp)
                }
            }
    }
}
