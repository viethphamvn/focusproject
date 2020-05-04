package com.example.focusproject

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.example.focusproject.fragments.ExcercisePickerFragment
import com.example.focusproject.models.Excercise
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_routine_activity_fragment.view.*
import kotlinx.android.synthetic.main.rountine_item.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RoutineEditActivity : AppCompatActivity() {

    lateinit var dateSpinner : Spinner
    lateinit var Routines : HashMap<String, ArrayList<Excercise>>
    var selectedDate : Int = 0
    private lateinit var touchHelper : ItemTouchHelper
    private lateinit var ActiveRoutineList: ArrayList<Excercise>
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
        Routines = HashMap(intent.getSerializableExtra("routine") as HashMap<String, ArrayList<Excercise>>)
        selectedDate = intent.getIntExtra("date", 2)

        setUpSpinner()
        setUpRecyclerView()

        //Set up Views
        var saveBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton_saveAction)
        var addBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton_addAction)
        var cancelBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton_cancelAction)

        saveBtn.setOnClickListener {
            var returnIntent: Intent = Intent()
            returnIntent.putExtra("routine", Routines)
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
                val layout = findViewById<FrameLayout>(R.id.excercisePickerContainer)
                var screen_height = display.height
                screen_height = 0
                val parms = layout.layoutParams
                parms.height = screen_height
                layout.layoutParams = parms
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.excercisePickerContainer, ExcercisePickerFragment.newInstance(),"pickerFragment")
                    .commit()

                val display = windowManager.defaultDisplay
                val layout = findViewById<FrameLayout>(R.id.excercisePickerContainer)
                var screen_height = display.height
                screen_height = (0.40 * screen_height).toInt()
                val parms = layout.layoutParams
                parms.height = screen_height
                layout.layoutParams = parms
            }
        }

        cancelBtn.setOnClickListener{
            finish()
        }
    }

    fun onItemClick(item: Excercise){
        ActiveRoutineList.add(item)
        routineRecyclerViewAdapter.notifyItemInserted(ActiveRoutineList.size-1)
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
                Collections.swap(ActiveRoutineList, sourcePosition, targetPosition)
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
            ActiveRoutineList = Routines.get(selectedDateAsString) as ArrayList<Excercise>
            routineRecyclerViewAdapter = RoutineRecyclerViewAdapter(ActiveRoutineList)
            adapter = routineRecyclerViewAdapter
        }
    }
}
