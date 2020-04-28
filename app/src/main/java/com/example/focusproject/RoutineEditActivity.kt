package com.example.focusproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.models.Excercise
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_routine_activity_fragment.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RoutineEditActivity : AppCompatActivity() {

    lateinit var dateSpinner : Spinner
    lateinit var Routines : HashMap<String, ArrayList<Excercise>>
    var selectedDate : Int = 0
    private lateinit var touchHelper : ItemTouchHelper
    private lateinit var ActiveRoutineList: ArrayList<Excercise>
    private lateinit var routineRecyclerViewAdapter: RoutineRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_edit)

        //Get the data from caller
        Routines = intent.getSerializableExtra("routine") as HashMap<String, ArrayList<Excercise>>
        selectedDate = intent.getIntExtra("date", 2)

        setUpSpinner()

        //Set up Views
        var saveBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton_saveAction)
        var addBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton_addAction)

        saveBtn.setOnClickListener {
            var returnIntent: Intent = Intent()
            returnIntent.putExtra("routine", Routines)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

    }

    private fun setUpSpinner(){
        dateSpinner = findViewById(R.id.date_spinner)
        //Set up options for Spinner
        val options = arrayOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")
        dateSpinner.adapter = ArrayAdapter<String>(this, R.layout.center_textview_for_spinner, options)
        //Set Up Default Option
        var defaultPosition = 0
        System.out.println(selectedDate)
        when(selectedDate){
            2 -> {defaultPosition = 0}
            3 -> {defaultPosition = 1}
            4 -> {defaultPosition = 2}
            5 -> {defaultPosition = 3}
            6 -> {defaultPosition = 4}
            7 -> {defaultPosition = 5}
            1 -> {defaultPosition = 6}
        }
        dateSpinner.setSelection(defaultPosition)
        //TODO Center the options https://stackoverflow.com/questions/7511049/set-view-text-align-at-center-in-spinner-in-android

        //TODO Set spinner first option to be the passed in date
        dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var selectedDateAsString = when(position){
                    0 -> "mon"
                    1 -> "tue"
                    2 -> "wed"
                    3 -> "thu"
                    4 -> "fri"
                    5 -> "sat"
                    else -> "sun"
                }
                ActiveRoutineList = Routines.get(selectedDateAsString) as ArrayList<Excercise>
                routineRecyclerViewAdapter.updateDataSet(ActiveRoutineList)
                routineRecyclerViewAdapter.notifyDataSetChanged()
            }
        }

        //Set Up Recycler View
        //Handle Drag and Drop Item
        touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = viewHolder.adapterPosition
                val targetPosition = target.adapterPosition
                Collections.swap(ActiveRoutineList, sourcePosition, targetPosition)
                routineRecyclerViewAdapter.notifyItemMoved(sourcePosition, targetPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }

        })
        var routine_recycler_list_view = findViewById<RecyclerView>(R.id.routine_recycler_list_view)
        routine_recycler_list_view.apply {
            layoutManager = LinearLayoutManager(context)
            var selectedDateAsString = when(selectedDate){
                2 -> "mon"
                3 -> "tue"
                4 -> "wed"
                5 -> "thu"
                6 -> "fri"
                7 -> "sat"
                else -> "sun"
            }
            ActiveRoutineList = Routines.get(selectedDateAsString) as ArrayList<Excercise>
            routineRecyclerViewAdapter = RoutineRecyclerViewAdapter(ActiveRoutineList)
            adapter = routineRecyclerViewAdapter
        }
        touchHelper.attachToRecyclerView(routine_recycler_list_view)
    }
}
