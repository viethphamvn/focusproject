package com.example.focusproject.fragments

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.focusproject.CreateRoutineActivity
import com.example.focusproject.R
import com.example.focusproject.RoutineEditActivity
import com.example.focusproject.models.Exercise
import com.example.focusproject.models.User
import com.example.focusproject.tools.YouTubeHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.ikovac.timepickerwithseconds.MyTimePickerDialog
import kotlinx.android.synthetic.main.fragment_new_workout_edit.view.*
import org.json.JSONObject
import java.time.Duration

private const val API_KEY = "AIzaSyDGVmEwQlfqzbybEhpkyXTfI2L0uSlU1-s"

class NewWorkoutEditFragment : Fragment() {
    private var hours : Long = 0
    private var minutes: Long = 0
    private var seconds: Long = 0
    private lateinit var fragmentView : View
    private lateinit var typeSpinner : Spinner
    private lateinit var currentDurationTextView : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_new_workout_edit, container, false)

        setUpSpinner()

        currentDurationTextView = fragmentView.currentDurationTextView

        fragmentView.changeDurationButton?.setOnClickListener {
            MyTimePickerDialog(context,
                MyTimePickerDialog.OnTimeSetListener {_, hourOfDay, minute, seconds ->
                    run {
                        setUpDuration(hourOfDay.toLong(), minute.toLong(), seconds.toLong())
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
                val youtubeUrl = YouTubeHelper.extractVideoIdFromUrl(workoutVideoUrlEditText.editText?.text.toString())
                var rep = 0L
                if (repTextView.text.toString() != "") {
                    rep = repTextView.text.toString().toLong()
                }
                val weight = 0L
                if (weightTextView.text.toString() != "") {
                    var weight = weightTextView.text.toString().toLong()
                }

                val duration = (hours * 3600 + minutes * 60 + seconds)

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
                    exercise["isTimed"] = if (youtubeUrl != ""){
                        true
                    } else {
                        duration > 0
                    }
                    exercise["weight"] = weight
                    exercise["duration"] = duration
                    exercise["createdBy"] = User.currentUser.id
                    exercise["uid"] = ref.id

                    ref.set(exercise).addOnSuccessListener {
                        Toast.makeText(context, "Exercise Created", Toast.LENGTH_SHORT).show()
                        exerciseCreatedCallBack(exercise)
                    }.addOnFailureListener {
                        Toast.makeText(context, "Error Occur", Toast.LENGTH_SHORT).show()
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
                    val vidId = YouTubeHelper.extractVideoIdFromUrl(s.toString())
                    val url = "https://www.googleapis.com/youtube/v3/videos?id=$vidId&part=contentDetails&key=$API_KEY"
                    // Instantiate the RequestQueue.
                    val queue = Volley.newRequestQueue(context)

                    // Request a string response from the provided URL.
                    val stringRequest = StringRequest(
                        Request.Method.GET, url,
                        Response.Listener { response ->
                            val resultJson = JSONObject(response)
                            if (resultJson.get("items").toString() != "[]"){
                                val dur = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    Duration.parse(resultJson.getJSONArray("items").getJSONObject(0).getJSONObject("contentDetails").getString("duration"))
                                } else {
                                    TODO("VERSION.SDK_INT < O")
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    setUpDuration(dur.seconds)
                                }
                            }
                        },
                        Response.ErrorListener { textView.text = "That didn't work!" })

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest)
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
            context?.let { ArrayAdapter(it, R.layout.center_textview_for_spinner,resources.getStringArray(R.array.type_array)) }
        //Set Up Default Option
        val defaultPosition = 0

        typeSpinner.setSelection(defaultPosition)
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

    private fun setUpDuration(hourOfDay: Long, minute: Long, seconds: Long){
        currentDurationTextView.text =
            String.format("%02d:%02d:%02d", hourOfDay, minute, seconds)
        hours = hourOfDay
        minutes = minute
        this.seconds = seconds
    }

    private fun setUpDuration(duration: Long){
        val hourOfDay = duration / 3600
        val minute = (duration - (hourOfDay * 3600)) / 60
        val seconds = (duration - minute*60 - hourOfDay*3600)

        currentDurationTextView.text =
            String.format("%02d:%02d:%02d", hourOfDay, minute, seconds)
        hours = hourOfDay
        minutes = minute
        this.seconds = seconds
    }

    companion object {
        @JvmStatic
        fun newInstance() = NewWorkoutEditFragment()
    }
}
