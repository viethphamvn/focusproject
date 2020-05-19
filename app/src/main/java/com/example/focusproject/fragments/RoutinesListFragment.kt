package com.example.focusproject.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.focusproject.R
import com.example.focusproject.RoutineEditActivity
import com.example.focusproject.StartRoutineActivity
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.models.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_routine_activity_fragment.view.*
import kotlinx.android.synthetic.main.fragment_routine_activity_fragment.view.mon
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class RoutinesListFragment : Fragment(), View.OnClickListener {
    private lateinit var activeRoutineList: ArrayList<Exercise>
    private var routines : HashMap<String, ArrayList<Exercise>> = HashMap()
    private var selectedDate : Int = 0
    private var selectedDateAsString: String = ""
    private val DAY_OF_WEEK = "DayOfWeek"
    private lateinit var currentButton : TextView
    private var buttonArray : HashMap<Int, TextView> = HashMap()
    private var data : Map<String, Any> = HashMap()
    private lateinit var routineRecyclerViewAdapter : RoutineRecyclerViewAdapter
    private var firestore = FirebaseFirestore.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Get Arguments
        arguments?.let {
            changeSelectedDate(it.getInt(DAY_OF_WEEK))
        }

        //Initiate the routines Hashmap with arraylists that will hold exerciseIds that will be fetched from
        //the database
        routines.put("mon", ArrayList())
        routines.put("tue", ArrayList())
        routines.put("wed", ArrayList())
        routines.put("thu", ArrayList())
        routines.put("fri", ArrayList())
        routines.put("sat", ArrayList())
        routines.put("sun", ArrayList())
    }

    private fun initiateRoutine(){
        firestore.collection("Users").document(currentUser.uid)
            .get()
            .addOnSuccessListener {
                data = it["routine"] as HashMap<String, Any>
                var tempArrayList = data.get("mon") as ArrayList<String>
                routines["mon"]?.clear()
                if (tempArrayList.size > 0) {
                    for (exerciseId in tempArrayList) {
                        if (exerciseId != "") {
                            firestore.collection("Exercise").document(exerciseId)
                                .get()
                                .addOnSuccessListener {
                                    createExercise(it, exerciseId, "mon")
                                }
                        }
                    }
                }


                tempArrayList = data.get("tue") as ArrayList<String>
                routines["tue"]?.clear()
                if (tempArrayList.size > 0) {
                    for (exerciseId in tempArrayList) {
                        if (exerciseId != "") {
                            firestore.collection("Exercise").document(exerciseId).get()
                                .addOnSuccessListener {
                                    createExercise(it, exerciseId, "tue")
                                }
                        }
                    }
                }


                tempArrayList = data.get("wed") as ArrayList<String>
                routines["wed"]?.clear()
                if (tempArrayList.size > 0) {
                    for (exerciseId in tempArrayList) {
                        if (exerciseId != "") {
                            firestore.collection("Exercise").document(exerciseId)
                                .get()
                                .addOnSuccessListener {
                                    createExercise(it, exerciseId,"wed")
                                }
                        }
                    }
                }


                tempArrayList = data.get("thu") as ArrayList<String>
                routines["thu"]?.clear()
                if (tempArrayList.size > 0) {
                    for (exerciseId in tempArrayList) {
                        if (exerciseId != "") {
                            firestore.collection("Exercise").document(exerciseId)
                                .get()
                                .addOnSuccessListener {
                                    createExercise(it, exerciseId,"thu")
                                }
                        }
                    }
                }

                tempArrayList = data.get("fri") as ArrayList<String>
                routines["fri"]?.clear()
                if (tempArrayList.size > 0) {
                    for (exerciseId in tempArrayList) {
                        if (exerciseId != "") {
                            firestore.collection("Exercise").document(exerciseId)
                                .get()
                                .addOnSuccessListener {
                                    createExercise(it, exerciseId, "fri")
                                }
                        }
                    }
                }


                tempArrayList = data.get("sat") as ArrayList<String>
                routines["sat"]?.clear()
                if (tempArrayList.size > 0) {
                    for (exerciseId in tempArrayList) {
                        if (exerciseId != "") {
                            firestore.collection("Exercise").document(exerciseId)
                                .get()
                                .addOnSuccessListener {
                                    createExercise(it, exerciseId, "sat")
                                }
                        }
                    }
                }


                tempArrayList = data.get("sun") as ArrayList<String>
                routines["sun"]?.clear()
                if (tempArrayList.size > 0) {
                    for (exerciseId in tempArrayList) {
                        if (exerciseId != "") {
                            firestore.collection("Exercise").document(exerciseId)
                                .get()
                                .addOnSuccessListener {
                                    createExercise(it, exerciseId, "sun")
                                }
                        }
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        initiateRoutine()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_routine_activity_fragment, container, false)

        //Create a Array of dateButton for easier control
        buttonArray.put(2, view.mon)
        buttonArray.put(3, view.tue)
        buttonArray.put(4, view.wed)
        buttonArray.put(5, view.thu)
        buttonArray.put(6, view.fri)
        buttonArray.put(7, view.sat)
        buttonArray.put(1, view.sun)
        //Initiate RecyclerView with Adapter
        view.routine_recycler_list_view.apply {
            layoutManager = LinearLayoutManager(context)
            routineRecyclerViewAdapter = RoutineRecyclerViewAdapter(getCurrentActiveList(selectedDate))
            adapter = routineRecyclerViewAdapter
            currentButton = buttonArray.get(selectedDate)!!
            currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        }

        //Handle Views Behavior
        view.apply {
            mon.setOnClickListener(this@RoutinesListFragment)
            tue.setOnClickListener (this@RoutinesListFragment)
            wed.setOnClickListener (this@RoutinesListFragment)
            thu.setOnClickListener (this@RoutinesListFragment)
            fri.setOnClickListener (this@RoutinesListFragment)
            sat.setOnClickListener(this@RoutinesListFragment)
            sun.setOnClickListener (this@RoutinesListFragment)

            edit_routine_btn.setOnClickListener {
                startEditActivity(routines, selectedDate)
            }

            start_workout_btn.setOnClickListener{
                var intent = Intent(context, StartRoutineActivity::class.java)
                intent.putExtra("routine", activeRoutineList)
                intent.putExtra("selectedDate", selectedDate)
                startActivity(intent)
            }
        }

        return view
    }

    private fun changeSelectedDate(date: Int){
        selectedDate = date
        selectedDateAsString = when(date){
            2 -> "mon"
            3 -> "tue"
            4 -> "wed"
            5 -> "thu"
            6 -> "fri"
            7 -> "sat"
            else -> "sun"
        }
    }

    private fun createExercise(it: DocumentSnapshot, exerciseId: String, date: String){
        var name = it.get("name") as String
        var type = it.get("type") as String
        var duration = it.get("duration") as Long
        var equipmentNeeded = it.get("equipmentNeeded") as Boolean
        var img = it.get("img") as String
        var isRestTime = it.get("isRestTime") as Boolean
        var isTime = it.get("isTimed") as Boolean
        var rep = it.get("rep") as Long
        var weight = it.get("weight") as Long
        var vidId = it.get("vidId") as String
        var createdBy = it.get("createdBy") as String
        var desc = it.get("desc") as String
        var exercise = Exercise(
            name,
            type,
            exerciseId,
            duration,
            img,
            vidId,
            isRestTime,
            isTime,
            rep,
            equipmentNeeded,
            weight,
            createdBy,
            desc
        )

        routines[date]!!.add(exercise)

        if (routines[date]!!.size == (data.get(date) as ArrayList<String>).size){
            var sortedArray = ArrayList<Exercise>()
            for (id in data.get(date) as ArrayList<String>){
                for (exercise in routines[date]!!){
                    if (exercise.uid == id){
                        sortedArray.add(exercise)
                        break
                    }
                }
            }
            routines[date] = sortedArray
            if (date == selectedDateAsString){
                routineRecyclerViewAdapter.updateDataSet(routines[date]!!)
            }
        }
    }

    private fun startEditActivity(routines : HashMap<String, ArrayList<Exercise>>, dateCode: Int){
        var copyRoutine = HashMap(routines)
        val intent = Intent(context, RoutineEditActivity::class.java)
        intent.putExtra("routine", copyRoutine)
        intent.putExtra("date", dateCode)
        startActivityForResult(intent, 100)
    }

    companion object {
        @JvmStatic
        fun newInstance(todayDate: Int) =
            RoutinesListFragment().apply {
                arguments = Bundle().apply {
                    putInt(DAY_OF_WEEK, todayDate)
                }
            }
    }

    private fun getCurrentActiveList(selectedDate: Int): ArrayList<Exercise>{
        activeRoutineList = when(selectedDate){
            2 -> if (routines.get("mon") != null) routines.get("mon") else ArrayList()
            3 -> if (routines.get("tue") != null) routines.get("tue") else ArrayList()
            4 -> if (routines.get("wed") != null) routines.get("wed") else ArrayList()
            5 -> if (routines.get("thu") != null) routines.get("thu") else ArrayList()
            6 -> if (routines.get("fri") != null) routines.get("fri") else ArrayList()
            7 -> if (routines.get("sat") != null) routines.get("sat") else ArrayList()
            else -> if (routines.get("sun") != null) routines.get("sun") else ArrayList()
        } as ArrayList<Exercise>
        return activeRoutineList
    }

    private fun updateDatabase(newRoutine: HashMap<String, ArrayList<Exercise>>){ //This function will update Firestore with new dataset
        var updatedRoutine = HashMap<String, ArrayList<String>>()
        var exerciseIdArray = ArrayList<String>()
        for (exercise in newRoutine["mon"]!!){
            if (exercise.uid != ""){
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["mon"] = ArrayList(exerciseIdArray)
        exerciseIdArray.clear()

        for (exercise in newRoutine["tue"]!!){
            if (exercise.uid != ""){
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["tue"] = ArrayList(exerciseIdArray)
        exerciseIdArray.clear()

        for (exercise in newRoutine["wed"]!!){
            if (exercise.uid != ""){
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["wed"] = ArrayList(exerciseIdArray)
        exerciseIdArray.clear()

        for (exercise in newRoutine["thu"]!!){
            if (exercise.uid != ""){
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["thu"] = ArrayList(exerciseIdArray)
        exerciseIdArray.clear()

        for (exercise in newRoutine["fri"]!!){
            if (exercise.uid != ""){
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["fri"] = ArrayList(exerciseIdArray)
        exerciseIdArray.clear()

        for (exercise in newRoutine["sat"]!!){
            if (exercise.uid != ""){
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["sat"] = ArrayList(exerciseIdArray)
        exerciseIdArray.clear()

        for (exercise in newRoutine["sun"]!!){
            if (exercise.uid != ""){
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["sun"] = ArrayList(exerciseIdArray)
        exerciseIdArray.clear()


        firestore.collection("Users").document(currentUser.uid).update("routine", updatedRoutine).addOnCompleteListener {
            initiateRoutine()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (data != null) {
                updateDatabase(data.getSerializableExtra("routine") as HashMap<String, ArrayList<Exercise>>)
            }
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            changeSelectedDate(
                when (v.id) {
                    R.id.mon -> 2
                    R.id.tue -> 3
                    R.id.wed -> 4
                    R.id.thu -> 5
                    R.id.fri -> 6
                    R.id.sat -> 7
                    else -> 1
                }
            )
            routineRecyclerViewAdapter.apply {
                updateDataSet(getCurrentActiveList(selectedDate))
            }
            currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
            currentButton = buttonArray.get(selectedDate)!!
            currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        }
    }
}
