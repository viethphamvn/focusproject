package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.focusproject.R
import com.ikovac.timepickerwithseconds.MyTimePickerDialog
import kotlinx.android.synthetic.main.fragment_new_workout_edit.view.*

class NewWorkoutEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_new_workout_edit, container, false)
        var currentDurationTextView = view.currentDurationTextView

        if (view != null) {
            view.changeDurationButton.setOnClickListener {
                MyTimePickerDialog(context, object :
                    MyTimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(
                        view: com.ikovac.timepickerwithseconds.TimePicker?,
                        hourOfDay: Int,
                        minute: Int,
                        seconds: Int
                    ) {
                        currentDurationTextView.text = String.format("%02d:%02d:%02d",hourOfDay, minute, seconds)
                    }
                }, 0, 0, 45, true).show()
            }
        }



        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = NewWorkoutEditFragment()
    }
}
