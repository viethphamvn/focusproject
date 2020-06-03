package com.example.focusproject

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.fragments.ExercisePickerFragment
import com.example.focusproject.models.Exercise
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RoutineEditActivity : AppCompatActivity() {

    private lateinit var dateSpinner : Spinner
    lateinit var routines : HashMap<String, ArrayList<Exercise>>
    var selectedDate : Int = 0
    private lateinit var touchHelper : ItemTouchHelper
    private lateinit var activeRoutineList: ArrayList<Exercise>
    private lateinit var routine_recycler_list_view: RecyclerView
    private lateinit var routineRecyclerViewAdapter: RoutineRecyclerViewAdapter

    private lateinit var deleteIcon: Drawable

    //REQUEST CODE
    val ADD_WORKOUT = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_edit)

        //Drawable Objects
        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)!!

        //Get the data from caller
        routines = HashMap(intent.getSerializableExtra("routine") as HashMap<String, ArrayList<Exercise>>)
        selectedDate = intent.getIntExtra("date", 2)

        setUpSpinner()
        setUpRecyclerView()

        //Set up Views
        var saveBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton_saveAction)
        var addBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton_addAction)
        var cancelBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton_cancelAction)

        saveBtn.setOnClickListener {
            var returnIntent: Intent = Intent()
            returnIntent.putExtra("routine", routines)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        addBtn.setOnClickListener{
            var excercisePickerFragment = supportFragmentManager.findFragmentByTag("pickerFragment")
            if (excercisePickerFragment != null){
                supportFragmentManager.beginTransaction()
                    .remove(excercisePickerFragment)
                    .commit()
                val display = windowManager.defaultDisplay
                val layout = findViewById<FrameLayout>(R.id.exercisePickerContainer)
                var screen_height = display.height
                screen_height = 0
                val parms = layout.layoutParams
                parms.height = screen_height
                layout.layoutParams = parms
                //change addBtn icon to add icon
                addBtn.setImageResource(R.drawable.ic_add)
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.exercisePickerContainer, ExercisePickerFragment.newInstance(),"pickerFragment")
                    .commit()

                val display = windowManager.defaultDisplay
                val layout = findViewById<FrameLayout>(R.id.exercisePickerContainer)
                var screen_height = display.height
                screen_height = (0.60 * screen_height).toInt()
                val parms = layout.layoutParams
                parms.height = screen_height
                layout.layoutParams = parms
                //change addBtn icon to arrow down
                addBtn.setImageResource(R.drawable.ic_down)
            }
        }

        cancelBtn.setOnClickListener{
            finish()
        }
    }

    fun onItemClick(item: Exercise){
        activeRoutineList.add(item)
        routineRecyclerViewAdapter.notifyItemInserted(activeRoutineList.size-1)
    }

    private fun setUpSpinner(){
        dateSpinner = findViewById(R.id.date_spinner)
        //Set up options for Spinner
        val options = arrayOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")
        dateSpinner.adapter = ArrayAdapter<String>(this, R.layout.center_textview_for_spinner, options)
        //Set Up Default Option
        var defaultPosition = 0
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
                when(position){
                    0 -> {selectedDate = 2}
                    1 -> {selectedDate = 3}
                    2 -> {selectedDate = 4}
                    3 -> {selectedDate = 5}
                    4 -> {selectedDate = 6}
                    5 -> {selectedDate = 7}
                    6 -> {selectedDate = 1}
                }

                updateRecyclerViewAdapter(selectedDate)
            }
        }


    }

    private fun setUpRecyclerView(){
        //Set Up Recycler View
        //Handle Drag and Drop Item
        touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = viewHolder.adapterPosition
                val targetPosition = target.adapterPosition
                Collections.swap(activeRoutineList, sourcePosition, targetPosition)
                routineRecyclerViewAdapter.notifyItemMoved(sourcePosition, targetPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var deletedItemPosition = viewHolder.adapterPosition
                routineRecyclerViewAdapter.removeItemAt(deletedItemPosition)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                var itemView = viewHolder.itemView

                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                if (dX > 0){
                    //When user swipe right
                } else {
                    //When user swipe left
                    deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin, itemView.right - iconMargin, itemView.bottom - iconMargin)
                }

                c.save()

                if (dX > 0){

                } else {
                    c.clipRect(itemView.right + dX.toInt(),itemView.top, itemView.right,itemView.bottom)
                }

                deleteIcon.draw(c)

                c.restore()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        })
        routine_recycler_list_view = findViewById(R.id.routine_recycler_list_view)
        updateRecyclerViewAdapter(selectedDate)
        touchHelper.attachToRecyclerView(routine_recycler_list_view)
    }

    private fun updateRecyclerViewAdapter(selectedDate: Int) {
        routine_recycler_list_view.apply {
            layoutManager = LinearLayoutManager(context)
            var selectedDateAsString = when (selectedDate) {
                2 -> "mon"
                3 -> "tue"
                4 -> "wed"
                5 -> "thu"
                6 -> "fri"
                7 -> "sat"
                else -> "sun"
            }
            activeRoutineList = routines.get(selectedDateAsString) as ArrayList<Exercise>
            routineRecyclerViewAdapter = RoutineRecyclerViewAdapter(activeRoutineList)
            adapter = routineRecyclerViewAdapter
        }
    }
}
