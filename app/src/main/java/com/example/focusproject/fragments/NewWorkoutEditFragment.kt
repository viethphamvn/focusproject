package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.focusproject.CreateExcerciseActivity

import com.example.focusproject.R
import com.example.focusproject.RoutineEditActivity
import com.example.focusproject.models.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.ikovac.timepickerwithseconds.MyTimePickerDialog
import kotlinx.android.synthetic.main.fragment_new_workout_edit.*
import kotlinx.android.synthetic.main.fragment_new_workout_edit.view.*
import kotlinx.android.synthetic.main.fragment_new_workout_edit.view.isRestTimeCheckBox

class NewWorkoutEditFragment : Fragment() {
    var hours : Int = 0
    var minutes: Int = 0
    var seconds: Int = 0
    lateinit var fragmentView : View
    lateinit var typeSpinner : Spinner
    var selectedType : String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_new_workout_edit, container, false)
        var currentDurationTextView = fragmentView.currentDurationTextView

        setUpSpinner()

        fragmentView?.changeDurationButton?.setOnClickListener {
            MyTimePickerDialog(context,
                MyTimePickerDialog.OnTimeSetListener {_, hourOfDay, minute, seconds ->
                    run {
                        currentDurationTextView.text =
                            String.format("%02d:%02d:%02d", hourOfDay, minute, seconds)
                        hours = hourOfDay
                        minutes = minute
                        this.seconds = seconds
                    }
                }, 0, 0, 45, true).show()
        }

        fragmentView.apply {
            saveButton.setOnClickListener {
                var isRestTime = isRestTimeCheckBox.isChecked
                var exerciseName = workoutNameEditText.editText?.text.toString()
                var equipmentNeeded = needEquipmentCheckBox.isChecked
                var exerciseDesc = workoutDescEditText.editText?.text.toString()
                var type = spinnerExerciseType.selectedItem.toString()
                var gifUrl = workoutImgUrlEditText.editText?.text.toString()
                var youtubeUrl = workoutVideoUrlEditText.editText?.text.toString()
                var rep = repTextView.text.toString().toLong()
                var weight = weightTextView.text.toString().toLong()
                var duration = (hours * 3600 + minutes * 60 + seconds).toLong()
                var firestore = FirebaseFirestore.getInstance()
                var ref = firestore.collection("Exercise").document()

                var exercise : HashMap<String, Any> = HashMap()
                exercise.put("isRestTime", isRestTime)
                exercise.put("name", exerciseName)
                exercise.put("equipmentNeeded", equipmentNeeded)
                exercise.put("desc", exerciseDesc)
                exercise.put("type", type)
                exercise.put("img", gifUrl)
                exercise.put("vidId", youtubeUrl)
                exercise.put("rep", rep)
                exercise.put("isTimed",duration > 0)
                exercise.put("weight", weight)
                exercise.put("duration", duration)
                exercise.put("createdBy", FirebaseAuth.getInstance().currentUser?.uid!!)
                exercise.put("uid",ref.id)

                ref.set(exercise).addOnSuccessListener {
                    Toast.makeText(context, "Exercise Created", Toast.LENGTH_SHORT).show()
                }
                exerciseCreatedCallBack(exercise)
            }
        }

        return fragmentView
    }

    private fun exerciseCreatedCallBack(exercise: HashMap<String, Any>){
        var newExercise = Exercise(
            exercise.get("name") as String,
            exercise.get("type") as String,
            exercise.get("uid") as String,
            exercise.get("duration") as Long,
            exercise.get("img") as String,
            exercise.get("vidId") as String,
            exercise.get("isRestTime") as Boolean,
            exercise.get("isTimed") as Boolean,
            exercise.get("rep") as Long,
            exercise.get("equipmentNeeded") as Boolean,
            exercise.get("weight") as Long,
            exercise.get("createdBy") as String,
            exercise.get("desc") as String)

        if (activity is RoutineEditActivity){
            (activity as RoutineEditActivity).onItemClick(newExercise)
        } else if (activity is CreateExcerciseActivity){
            (activity as CreateExcerciseActivity).onItemClick(newExercise)
        }
    }

    private fun setUpSpinner(){
        typeSpinner = fragmentView.spinnerExerciseType
        //Set up options for Spinner
        typeSpinner.adapter =
            context?.let { ArrayAdapter<String>(it, R.layout.center_textview_for_spinner,resources.getStringArray(R.array.type_array)) }
        //Set Up Default Option
        var defaultPosition = 0

        typeSpinner.setSelection(defaultPosition)
        //TODO Center the options https://stackoverflow.com/questions/7511049/set-view-text-align-at-center-in-spinner-in-android

        //TODO Set spinner first option to be the passed in date
        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }
        }


    }

    companion object {
        @JvmStatic
        fun newInstance() = NewWorkoutEditFragment()
    }
}
