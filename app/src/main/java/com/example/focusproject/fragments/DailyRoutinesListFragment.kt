package com.example.focusproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.focusproject.R
import com.example.focusproject.RoutineEditActivity
import com.example.focusproject.StartRoutineActivity
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.models.Exercise
import com.example.focusproject.models.User
import com.example.focusproject.tools.CreateExercise
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_daily_routine_activity.view.*


class DailyRoutinesListFragment : Fragment(), View.OnClickListener {
    private var routines = User.dailyRoutine
    private var selectedDate : Int = 0
    private var selectedDateAsString: String = ""
    private val DAY_OF_WEEK = "DayOfWeek"
    private lateinit var currentButton : TextView
    private var buttonArray : HashMap<Int, TextView> = HashMap()
    private var data : Map<String, Any> = HashMap()
    private lateinit var routineRecyclerViewAdapter : RoutineRecyclerViewAdapter
    private var firestore = FirebaseFirestore.getInstance()
    private var currentUser = User.currentUser.id
    private lateinit var emptyLayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Get Arguments
        arguments?.let {
            changeSelectedDate(it.getInt(DAY_OF_WEEK))
        }

        //Initiate the routines Hashmap with arraylists that will hold exerciseIds that will be fetched from
        //the database
        routines["mon"] = ArrayList()
        routines["tue"] = ArrayList()
        routines["wed"] = ArrayList()
        routines["thu"] = ArrayList()
        routines["fri"] = ArrayList()
        routines["sat"] = ArrayList()
        routines["sun"] = ArrayList()
    }

    private fun initiateRoutine(){
        firestore.collection("Users").document(currentUser)
            .get()
            .addOnSuccessListener { it ->
                if (it["routine"] != null) {
                    data = it["routine"] as HashMap<String, Any>
                    var tempArrayList = data["mon"] as ArrayList<String>
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


                    tempArrayList = data["tue"] as ArrayList<String>
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


                    tempArrayList = data["wed"] as ArrayList<String>
                    routines["wed"]?.clear()
                    if (tempArrayList.size > 0) {
                        for (exerciseId in tempArrayList) {
                            if (exerciseId != "") {
                                firestore.collection("Exercise").document(exerciseId)
                                    .get()
                                    .addOnSuccessListener {
                                        createExercise(it, exerciseId, "wed")
                                    }
                            }
                        }
                    }


                    tempArrayList = data["thu"] as ArrayList<String>
                    routines["thu"]?.clear()
                    if (tempArrayList.size > 0) {
                        for (exerciseId in tempArrayList) {
                            if (exerciseId != "") {
                                firestore.collection("Exercise").document(exerciseId)
                                    .get()
                                    .addOnSuccessListener {
                                        createExercise(it, exerciseId, "thu")
                                    }
                            }
                        }
                    }

                    tempArrayList = data["fri"] as ArrayList<String>
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


                    tempArrayList = data["sat"] as ArrayList<String>
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


                    tempArrayList = data["sun"] as ArrayList<String>
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
                    //Handle empty routines[date]
                    if (routines[selectedDateAsString]!!.size == 0) {
                        routineRecyclerViewAdapter.notifyDataSetChanged()
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
        val view = inflater.inflate(R.layout.fragment_daily_routine_activity, container, false)

        emptyLayout = view.emptyLayout

        //Create a Array of dateButton for easier control
        buttonArray[2] = view.mon
        buttonArray[3] = view.tue
        buttonArray[4] = view.wed
        buttonArray[5] = view.thu
        buttonArray[6] = view.fri
        buttonArray[7] = view.sat
        buttonArray[1] = view.sun
        //Initiate RecyclerView with Adapter
        view.routine_recycler_list_view.apply {
            layoutManager = LinearLayoutManager(context)
            routineRecyclerViewAdapter = RoutineRecyclerViewAdapter(getCurrentActiveList(selectedDate))
            adapter = routineRecyclerViewAdapter
            currentButton = buttonArray[selectedDate]!!
            currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        }

        //Handle Views Behavior
        view.apply {
            mon.setOnClickListener(this@DailyRoutinesListFragment)
            tue.setOnClickListener (this@DailyRoutinesListFragment)
            wed.setOnClickListener (this@DailyRoutinesListFragment)
            thu.setOnClickListener (this@DailyRoutinesListFragment)
            fri.setOnClickListener (this@DailyRoutinesListFragment)
            sat.setOnClickListener(this@DailyRoutinesListFragment)
            sun.setOnClickListener (this@DailyRoutinesListFragment)

            edit_routine_btn.setOnClickListener {
                startEditActivity(selectedDate)
            }

            start_workout_btn.setOnClickListener{
                if (getCurrentActiveList(selectedDate).size > 0) {
                    val intent = Intent(context, StartRoutineActivity::class.java)
                    intent.putExtra("routine", getCurrentActiveList(selectedDate))
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "You don't have any exercises", Toast.LENGTH_SHORT).show()
                }
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
        val exercise = CreateExercise.createExercise(it, exerciseId)

        routines[date]!!.add(exercise)

        if (routines[date]!!.size == (data[date] as ArrayList<String>).size){
            val sortedArray = ArrayList<Exercise>()
            for (id in data[date] as ArrayList<String>){
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
                if (routines[date]!!.size == 0){
                    emptyLayout.visibility = View.VISIBLE
                } else {
                    emptyLayout.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun startEditActivity(dateCode: Int){
        val intent = Intent(context, RoutineEditActivity::class.java)
        intent.putExtra("date", dateCode)
        startActivityForResult(intent, 100)
    }

    companion object {
        @JvmStatic
        fun newInstance(todayDate: Int) =
            DailyRoutinesListFragment().apply {
                arguments = Bundle().apply {
                    putInt(DAY_OF_WEEK, todayDate)
                }
            }
    }

    private fun getCurrentActiveList(selectedDate: Int): ArrayList<Exercise>{
        val returnList = when(selectedDate){
            2 -> if (routines["mon"] != null) routines["mon"] else ArrayList()
            3 -> if (routines["tue"] != null) routines["tue"] else ArrayList()
            4 -> if (routines["wed"] != null) routines["wed"] else ArrayList()
            5 -> if (routines["thu"] != null) routines["thu"] else ArrayList()
            6 -> if (routines["fri"] != null) routines["fri"] else ArrayList()
            7 -> if (routines["sat"] != null) routines["sat"] else ArrayList()
            else -> if (routines["sun"] != null) routines["sun"] else ArrayList()
        } as ArrayList<Exercise>

        if (returnList.size == 0){
            emptyLayout.visibility = View.VISIBLE
        } else {
            emptyLayout.visibility = View.INVISIBLE
        }

        return returnList
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
            currentButton = buttonArray[selectedDate]!!
            currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        }
    }
}
