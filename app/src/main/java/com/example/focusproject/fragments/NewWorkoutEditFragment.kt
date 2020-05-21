package com.example.focusproject.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import com.example.focusproject.CreateRoutineActivity

import com.example.focusproject.R
import com.example.focusproject.RoutineEditActivity
import com.example.focusproject.models.Exercise
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ikovac.timepickerwithseconds.MyTimePickerDialog
import kotlinx.android.synthetic.main.fragment_new_workout_edit.view.*
import kotlinx.android.synthetic.main.fragment_new_workout_edit.view.isRestTimeCheckBox
import org.w3c.dom.Text
import java.time.Duration

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
        val currentDurationTextView = fragmentView.currentDurationTextView

        setUpSpinner()

        fragmentView.changeDurationButton?.setOnClickListener {
            MyTimePickerDialog(context,
                MyTimePickerDialog.OnTimeSetListener {_, hourOfDay, minute, seconds ->
                    run {
                        currentDurationTextView.text =
                            String.format("%02d:%02d:%02d", hourOfDay, minute, seconds)
                        hours = hourOfDay
                        minutes = minute
                        this.seconds = seconds
                    }
                }, 0, 0, 0, true).show()
        }

        fragmentView.apply {
            saveButton.setOnClickListener {
                val isRestTime = isRestTimeCheckBox.isChecked
                val exerciseName = workoutNameEditText.editText?.text.toString()
                val equipmentNeeded = needEquipmentCheckBox.isChecked
                val exerciseDesc = workoutDescEditText.editText?.text.toString()
                val type = spinnerExerciseType.selectedItem.toString()
                val gifUrl = workoutImgUrlEditText.editText?.text.toString()
                val youtubeUrl = workoutVideoUrlEditText.editText?.text.toString()
                var rep = 0L
                if (repTextView.text.toString() != "") {
                    rep = repTextView.text.toString().toLong()
                }
                var weight = 0L
                if (weightTextView.text.toString() != "") {
                    var weight = weightTextView.text.toString().toLong()
                }
                val duration = (hours * 3600 + minutes * 60 + seconds).toLong()

                if (validateInfo(isRestTime, exerciseName, type, gifUrl, youtubeUrl, duration)) { //Check information before submit
                    val firestore = FirebaseFirestore.getInstance()
                    val ref = firestore.collection("Exercise").document()

                    val exercise: HashMap<String, Any> = HashMap()
                    exercise["isRestTime"] = isRestTime
                    exercise["name"] = exerciseName
                    exercise["equipmentNeeded"] = equipmentNeeded
                    exercise["desc"] = exerciseDesc
                    exercise["type"] = type
                    exercise["img"] = gifUrl
                    exercise["vidId"] = youtubeUrl
                    exercise["rep"] = rep
                    exercise["isTimed"] = duration > 0
                    exercise["weight"] = weight
                    exercise["duration"] = duration
                    exercise["createdBy"] = FirebaseAuth.getInstance().currentUser?.uid!!
                    exercise["uid"] = ref.id

                    ref.set(exercise).addOnSuccessListener {
                        Toast.makeText(context, "Exercise Created", Toast.LENGTH_SHORT).show()
                        exerciseCreatedCallBack(exercise)
                    }.addOnFailureListener {
                        Toast.makeText(context, "Error Occured", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Info missing", Toast.LENGTH_SHORT).show()
                    //Handle error
                }
            }

            isRestTimeCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    fragmentView.workoutDescEditText.visibility = View.GONE
                    fragmentView.workoutNameEditText.visibility = View.GONE
                    fragmentView.needEquipmentCheckBox.visibility = View.GONE
                    fragmentView.needEquipmentTextView.visibility = View.GONE
                    fragmentView.spinnerExerciseType.visibility = View.GONE
                    fragmentView.repTextView.visibility = View.GONE
                    fragmentView.decreaseRepButton.visibility = View.GONE
                    fragmentView.increaseRepButton.visibility = View.GONE
                    fragmentView.increaseWeightButton.visibility = View.GONE
                    fragmentView.decreaseWeightButton.visibility = View.GONE
                    fragmentView.weightTextView.visibility = View.GONE
                } else {
                    fragmentView.workoutDescEditText.visibility = View.VISIBLE
                    fragmentView.workoutNameEditText.visibility = View.VISIBLE
                    fragmentView.needEquipmentCheckBox.visibility = View.VISIBLE
                    fragmentView.needEquipmentTextView.visibility = View.VISIBLE
                    fragmentView.spinnerExerciseType.visibility = View.VISIBLE
                    fragmentView.repTextView.visibility = View.VISIBLE
                    fragmentView.decreaseRepButton.visibility = View.VISIBLE
                    fragmentView.increaseRepButton.visibility = View.VISIBLE
                    fragmentView.increaseWeightButton.visibility = View.VISIBLE
                    fragmentView.decreaseWeightButton.visibility = View.VISIBLE
                    fragmentView.weightTextView.visibility = View.VISIBLE
                }
            }

            imageInput.addTextChangedListener(object: TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    workoutVideoUrlEditText.isEnabled = s.toString() == ""
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            })

            videoInput.addTextChangedListener(object: TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    workoutImgUrlEditText.isEnabled = s.toString() == ""
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            })

            increaseRepButton.setOnClickListener {
                if ((repTextView as TextView).text.toString() != "") {
                    (repTextView as TextView).text = ((repTextView as TextView).text.toString().toLong() + 1).toString()
                } else {
                    (repTextView as TextView).text = "0"
                }
            }

            decreaseRepButton.setOnClickListener {
                if ((repTextView as TextView).text.toString() != "" && (repTextView as TextView).text.toString().toLong() > 0) {
                    (repTextView as TextView).text = ((repTextView as TextView).text.toString().toLong() - 1).toString()
                } else {
                    (repTextView as TextView).text = "0"
                }
            }

            increaseWeightButton.setOnClickListener {
                if ((weightTextView as TextView).text.toString() != "") {
                    (weightTextView as TextView).text = ((weightTextView as TextView).text.toString().toLong() + 1).toString()
                } else {
                    (weightTextView as TextView).text = "0"
                }
            }

            decreaseWeightButton.setOnClickListener {
                if ((weightTextView as TextView).text.toString() != "" && (weightTextView as TextView).text.toString().toLong() > 0) {
                    (weightTextView as TextView).text = ((weightTextView as TextView).text.toString().toLong() - 1).toString()
                } else {
                    (weightTextView as TextView).text = "0"
                }
            }
        }

        return fragmentView
    }

    private fun validateInfo(isRestTime: Boolean, name: String, type: String, img: String, vid: String, duration: Long): Boolean {
        if (isRestTime){
            if (img != "" || vid != ""){
                if (duration > 0L){
                    return true
                }
            }
        } else {
            if (name != "" && type != getString(R.string.typeNotSet)){
                if (img != "" || vid != ""){
                    return true
                }
            }
        }
        return false
    }

    private fun exerciseCreatedCallBack(exercise: HashMap<String, Any>){
        val newExercise = Exercise(
            exercise["name"] as String,
            exercise["type"] as String,
            exercise["uid"] as String,
            exercise["duration"] as Long,
            exercise["img"] as String,
            exercise["vidId"] as String,
            exercise["isRestTime"] as Boolean,
            exercise["isTimed"] as Boolean,
            exercise["rep"] as Long,
            exercise["equipmentNeeded"] as Boolean,
            exercise["weight"] as Long,
            exercise["createdBy"] as String,
            exercise["desc"] as String)

        if (activity is RoutineEditActivity){
            (activity as RoutineEditActivity).onItemClick(newExercise)
        } else if (activity is CreateRoutineActivity){
            (activity as CreateRoutineActivity).onItemClick(newExercise)
        }
    }

    private fun setUpSpinner(){
        typeSpinner = fragmentView.spinnerExerciseType
        //Set up options for Spinner
        typeSpinner.adapter =
            context?.let { ArrayAdapter<String>(it, R.layout.center_textview_for_spinner,resources.getStringArray(R.array.type_array)) }
        //Set Up Default Option
        val defaultPosition = 0

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
