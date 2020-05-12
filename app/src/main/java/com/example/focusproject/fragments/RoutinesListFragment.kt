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
import kotlinx.android.synthetic.main.fragment_routine_activity_fragment.view.mon_textview
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private lateinit var routineRecyclerViewAdapter: RoutineRecyclerViewAdapter

class RoutinesListFragment : Fragment() {
    private lateinit var activeRoutineList: ArrayList<Exercise>
    private var routines : HashMap<String, ArrayList<Exercise>> = HashMap()
    private var selectedDate : Int = 0
    private val DAY_OF_WEEK = "DayOfWeek"
    private lateinit var currentButton : TextView
    private var buttonArray : HashMap<Int, TextView> = HashMap()
    private var data : Map<String, Any> = HashMap()
    private lateinit var routineRecyclerViewAdapter : RoutineRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Get Arguments
        arguments?.let {
            selectedDate = it.getInt(DAY_OF_WEEK)
        }

        routines.put("mon", ArrayList())
        routines.put("tue", ArrayList())
        routines.put("wed", ArrayList())
        routines.put("thu", ArrayList())
        routines.put("fri", ArrayList())
        routines.put("sat", ArrayList())
        routines.put("sun", ArrayList())
    }

    private fun initiateRoutine(){
       var firestore = FirebaseFirestore.getInstance()
        var currentUser = FirebaseAuth.getInstance().currentUser!!
        firestore.collection("Users").document(currentUser.uid)
            .get()
            .addOnSuccessListener {
                data = it["routine"] as HashMap<String, Any>

                var tempArrayList = data.get("mon") as ArrayList<String>
                if (tempArrayList.size > 0){
                    routines["mon"]?.clear()
                    routineRecyclerViewAdapter.notifyDataSetChanged()
                }
                for (exerciseId in tempArrayList) {
                    if (exerciseId != "") {
                        firestore.collection("Exercise").document(exerciseId)
                            .get()
                            .addOnSuccessListener {
                                routines["mon"]?.add(createExercise(it, exerciseId))
                                routineRecyclerViewAdapter.notifyItemInserted(routines["mon"]!!.size - 1)
                            }
                    }
                }


                tempArrayList = data.get("tue") as ArrayList<String>
                if (tempArrayList.size > 0){
                    routines["tue"]?.clear()
                    routineRecyclerViewAdapter.notifyDataSetChanged()
                }
                for (exerciseId in tempArrayList) {
                    if (exerciseId != "") {
                        firestore.collection("Exercise").document(exerciseId)
                            .get()
                            .addOnSuccessListener {
                                routines["tue"]?.add(createExercise(it, exerciseId))
                                routineRecyclerViewAdapter.notifyItemInserted(routines["tue"]!!.size - 1)
                            }
                    }
                }


                tempArrayList = data.get("wed") as ArrayList<String>
                if (tempArrayList.size > 0){
                    routines["wed"]?.clear()
                    routineRecyclerViewAdapter.notifyDataSetChanged()
                }
                for (exerciseId in tempArrayList) {
                    if (exerciseId != "") {
                        firestore.collection("Exercise").document(exerciseId)
                            .get()
                            .addOnSuccessListener {
                                routines["wed"]?.add(createExercise(it, exerciseId))
                                routineRecyclerViewAdapter.notifyItemInserted(routines["wed"]!!.size - 1)
                            }
                    }
                }


                tempArrayList = data.get("thu") as ArrayList<String>
                if (tempArrayList.size > 0){
                    routines["thu"]?.clear()
                    routineRecyclerViewAdapter.notifyDataSetChanged()
                }
                for (exerciseId in tempArrayList) {
                    if (exerciseId != "") {
                        firestore.collection("Exercise").document(exerciseId)
                            .get()
                            .addOnSuccessListener {
                                routines["thu"]?.add(createExercise(it, exerciseId))
                                routineRecyclerViewAdapter.notifyItemInserted(routines["thu"]!!.size - 1)
                            }
                    }
                }

                tempArrayList = data.get("fri") as ArrayList<String>
                if (tempArrayList.size > 0){
                    routines["fri"]?.clear()
                    routineRecyclerViewAdapter.notifyDataSetChanged()
                }
                for (exerciseId in tempArrayList) {
                    if (exerciseId != "") {
                        firestore.collection("Exercise").document(exerciseId)
                            .get()
                            .addOnSuccessListener {
                                routines["fri"]?.add(createExercise(it, exerciseId))
                                routineRecyclerViewAdapter.notifyItemInserted(routines["fri"]!!.size - 1)
                            }
                    }
                }


                tempArrayList = data.get("sat") as ArrayList<String>
                if (tempArrayList.size > 0){
                    routines["sat"]?.clear()
                    routineRecyclerViewAdapter.notifyDataSetChanged()
                }
                for (exerciseId in tempArrayList) {
                    if (exerciseId != "") {
                        firestore.collection("Exercise").document(exerciseId)
                            .get()
                            .addOnSuccessListener {
                                routines["sat"]?.add(createExercise(it, exerciseId))
                                routineRecyclerViewAdapter.notifyItemInserted(routines["sat"]!!.size - 1)
                            }
                    }
                }


                tempArrayList = data.get("sun") as ArrayList<String>
                if (tempArrayList.size > 0){
                    routines["sun"]?.clear()
                    routineRecyclerViewAdapter.notifyDataSetChanged()
                }
                for (exerciseId in tempArrayList) {
                    if (exerciseId != "") {
                        firestore.collection("Exercise").document(exerciseId)
                            .get()
                            .addOnSuccessListener {
                                routines["sun"]?.add(createExercise(it, exerciseId))
                                routineRecyclerViewAdapter.notifyItemInserted(routines["sun"]!!.size - 1)
                            }
                    }
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_routine_activity_fragment, container, false)

        //Create a Array of dateButton for easier control
        buttonArray.put(2, view.mon_textview)
        buttonArray.put(3, view.tue_textview)
        buttonArray.put(4, view.wed_textview)
        buttonArray.put(5, view.thu_textview)
        buttonArray.put(6, view.fri_textview)
        buttonArray.put(7, view.sat_textview)
        buttonArray.put(1, view.sun_textview)
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
            mon_textview.setOnClickListener {
                selectedDate = 2
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
            tue_textview.setOnClickListener {
                selectedDate = 3
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
            wed_textview.setOnClickListener {
                selectedDate = 4
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
            thu_textview.setOnClickListener {
                selectedDate = 5
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
            fri_textview.setOnClickListener {
                selectedDate = 6
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
            sat_textview.setOnClickListener {
                selectedDate = 7
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
            sun_textview.setOnClickListener {
                selectedDate = 1
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
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

    override fun onResume() {
        super.onResume()

        initiateRoutine()
    }

    private fun createExercise(it: DocumentSnapshot, exerciseId: String): Exercise{
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

        return exercise
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

    private fun updateDatabase(){ //This function will update Firestore with new dataset

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (data != null) {
                routines = data.getSerializableExtra("routine") as HashMap<String, ArrayList<Exercise>>
                //TODO have to update the dataset first
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
            }
            updateDatabase()
        }
    }
}
