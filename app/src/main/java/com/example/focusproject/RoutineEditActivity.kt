package com.example.focusproject

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.fragments.ExercisePickerFragment
import com.example.focusproject.models.Exercise
import com.example.focusproject.models.User
import com.example.focusproject.tools.DateToString
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_routine_edit.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RoutineEditActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var dateSpinner: Spinner
    private lateinit var routines: HashMap<String, ArrayList<Exercise>>
    var selectedDate: Int = 0
    private var selectedDateAsString = ""
    private lateinit var touchHelper: ItemTouchHelper
    private lateinit var activeRoutineList: ArrayList<Exercise>
    private lateinit var routine_recycler_list_view: RecyclerView
    private lateinit var routineRecyclerViewAdapter: RoutineRecyclerViewAdapter
    private var userWantedToAddRoutine: ArrayList<Exercise>? = null
    private var datePickerList: HashMap<String, CheckedTextView> = HashMap()
    private lateinit var deleteIcon: Drawable
    private var originRoutine : Int = 0

    //REQUEST CODE
    val ADD_WORKOUT = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_edit)

        //Drawable Objects
        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)!!

        //Get the data from caller
        routines = HashMap(User.dailyRoutine)
        selectedDate = intent.getIntExtra("date", 2)
        if (intent.getSerializableExtra("routine") != null){
            userWantedToAddRoutine = intent.getSerializableExtra("routine") as ArrayList<Exercise>
        }

        setUpSpinner()
        setUpRecyclerView()

        //Set up Views
        val saveBtn =
            findViewById<ExtendedFloatingActionButton>(R.id.floatingActionButton_saveAction)
        val addBtn = findViewById<ExtendedFloatingActionButton>(R.id.floatingActionButton_addAction)
        val cancelBtn =
            findViewById<ExtendedFloatingActionButton>(R.id.floatingActionButton_cancelAction)

        //Get List of Date checkbox
        datePickerList["mon"] = findViewById(R.id.mon)
        datePickerList["tue"] = findViewById(R.id.tue)
        datePickerList["wed"] = findViewById(R.id.wed)
        datePickerList["thu"] = findViewById(R.id.thu)
        datePickerList["fri"] = findViewById(R.id.fri)
        datePickerList["sat"] = findViewById(R.id.sat)
        datePickerList["sun"] = findViewById(R.id.sun)


        //If this is not null, then that means user wants to add new exercises to current routine. Call from RoutineDetail.kt
        if (userWantedToAddRoutine != null) {
            addBtn.text = getString(R.string.add_here)
            addBtn.setOnClickListener {
                for (exercise in userWantedToAddRoutine!!) {
                    activeRoutineList.add(exercise)
                    routineRecyclerViewAdapter.notifyItemInserted(activeRoutineList.size - 1)
                }
            }

            saveBtn.apply {
                text = ""
            }

            cancelBtn.apply {
                text = ""
            }

        } else {
            addBtn.setOnClickListener {
                val excercisePickerFragment =
                    supportFragmentManager.findFragmentByTag("pickerFragment")
                if (excercisePickerFragment != null) {
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
                    addBtn.setIconResource(R.drawable.ic_add)

                    //Show cancel btn and save btn
                    cancelBtn.visibility = View.VISIBLE
                    saveBtn.visibility = View.VISIBLE
                    copyBtn.visibility = View.VISIBLE
                    copyLayout.visibility = View.VISIBLE

                } else {
                    supportFragmentManager.beginTransaction()
                        .add(
                            R.id.exercisePickerContainer,
                            ExercisePickerFragment.newInstance(),
                            "pickerFragment"
                        )
                        .commit()

                    val display = windowManager.defaultDisplay
                    val layout = findViewById<FrameLayout>(R.id.exercisePickerContainer)
                    var screen_height = display.height
                    screen_height = (0.60 * screen_height).toInt()
                    val parms = layout.layoutParams
                    parms.height = screen_height
                    layout.layoutParams = parms
                    //change addBtn icon to arrow down
                    addBtn.setIconResource(R.drawable.ic_down)

                    //Hide cancel btn and save btn and copylayout
                    cancelBtn.visibility = View.GONE
                    saveBtn.visibility = View.GONE
                    copyBtn.visibility = View.GONE
                    copyLayout.visibility = View.GONE

                }
            }
        }

        saveBtn.setOnClickListener {
            checkCopies()
            updateDatabase(routines)
            //Finish when upload process is completed
        }

        cancelBtn.setOnClickListener {
            finish()
        }

        val copyBtn = findViewById<MaterialButton>(R.id.copyBtn)
        val copyLayout = findViewById<LinearLayout>(R.id.copyLayout)

        copyBtn.setOnClickListener {
            if (copyLayout.visibility == View.VISIBLE){
                copyBtn.text = getString(R.string.copy_current_routine)
                copyLayout.visibility = View.GONE
                copyBtn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryDark))
                copyBtn.icon = getDrawable(R.drawable.ic_right)
                originRoutine = 0
            } else {
                copyBtn.text = getString(R.string.cancel_copy)
                copyBtn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.darkgrey))
                copyBtn.icon = getDrawable(R.drawable.ic_down2)
                copyLayout.visibility = View.VISIBLE
                originRoutine = selectedDate

                findViewById<CheckedTextView>(R.id.mon).setOnClickListener(this)
                findViewById<CheckedTextView>(R.id.tue).setOnClickListener(this)
                findViewById<CheckedTextView>(R.id.wed).setOnClickListener(this)
                findViewById<CheckedTextView>(R.id.thu).setOnClickListener(this)
                findViewById<CheckedTextView>(R.id.fri).setOnClickListener(this)
                findViewById<CheckedTextView>(R.id.sat).setOnClickListener(this)
                findViewById<CheckedTextView>(R.id.sun).setOnClickListener(this)

            }
        }

    }

    private fun checkCopies() {
        for (checkField in datePickerList) {
            if (checkField.value.isChecked && checkField.key != DateToString.toString(originRoutine)) {
                routines[checkField.key]!!.clear()
                for (exercise in routines[DateToString.toString(originRoutine)]!!) {
                    routines[checkField.key]!!.add(exercise)
                }
            }
        }
    }

    private fun updateDatabase(newRoutine: HashMap<String, ArrayList<Exercise>>) { //This function will update Firestore with new dataset
        val updatedRoutine = HashMap<String, ArrayList<String>>()
        val exerciseIdArray = ArrayList<String>()
        for (exercise in newRoutine["mon"]!!) {
            if (exercise.uid != "") {
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["mon"] = ArrayList(exerciseIdArray)
        exerciseIdArray.clear()

        for (exercise in newRoutine["tue"]!!) {
            if (exercise.uid != "") {
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["tue"] = ArrayList(exerciseIdArray)
        exerciseIdArray.clear()

        for (exercise in newRoutine["wed"]!!) {
            if (exercise.uid != "") {
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["wed"] = ArrayList(exerciseIdArray)
        exerciseIdArray.clear()

        for (exercise in newRoutine["thu"]!!) {
            if (exercise.uid != "") {
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["thu"] = ArrayList(exerciseIdArray)
        exerciseIdArray.clear()

        for (exercise in newRoutine["fri"]!!) {
            if (exercise.uid != "") {
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["fri"] = ArrayList(exerciseIdArray)
        exerciseIdArray.clear()

        for (exercise in newRoutine["sat"]!!) {
            if (exercise.uid != "") {
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["sat"] = ArrayList(exerciseIdArray)
        exerciseIdArray.clear()

        for (exercise in newRoutine["sun"]!!) {
            if (exercise.uid != "") {
                exerciseIdArray.add(exercise.uid)
            }
        }
        updatedRoutine["sun"] = ArrayList(exerciseIdArray)

        FirebaseFirestore.getInstance().collection("Users").document(User.currentUser.id)
            .update("routine", updatedRoutine).addOnCompleteListener {
            finish()
        }
    }

    fun onItemClick(item: Exercise) {
        activeRoutineList.add(item)
        routineRecyclerViewAdapter.notifyItemInserted(activeRoutineList.size - 1)
    }

    private fun setUpSpinner() {
        dateSpinner = findViewById(R.id.date_spinner)
        //Set up options for Spinner
        val options =
            arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        dateSpinner.adapter =
            ArrayAdapter(this, R.layout.center_textview_for_spinner, options)
        //Set Up Default Option
        var defaultPosition = 0
        when (selectedDate) {
            2 -> {
                defaultPosition = 0
            }
            3 -> {
                defaultPosition = 1
            }
            4 -> {
                defaultPosition = 2
            }
            5 -> {
                defaultPosition = 3
            }
            6 -> {
                defaultPosition = 4
            }
            7 -> {
                defaultPosition = 5
            }
            1 -> {
                defaultPosition = 6
            }
        }
        dateSpinner.setSelection(defaultPosition)

        dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        selectedDate = 2
                    }
                    1 -> {
                        selectedDate = 3
                    }
                    2 -> {
                        selectedDate = 4
                    }
                    3 -> {
                        selectedDate = 5
                    }
                    4 -> {
                        selectedDate = 6
                    }
                    5 -> {
                        selectedDate = 7
                    }
                    6 -> {
                        selectedDate = 1
                    }
                }

                updateRecyclerViewAdapter(selectedDate)
            }
        }


    }

    private fun setUpRecyclerView() {
        //Set Up Recycler View
        //Handle Drag and Drop Item
        touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT
        ) {
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
                val deletedItemPosition = viewHolder.adapterPosition
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
                val itemView = viewHolder.itemView

                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                if (dX > 0) {
                    //When user swipe right
                } else {
                    //When user swipe left
                    deleteIcon.setBounds(
                        itemView.right - iconMargin - deleteIcon.intrinsicWidth,
                        itemView.top + iconMargin,
                        itemView.right - iconMargin,
                        itemView.bottom - iconMargin
                    )
                }

                c.save()

                if (dX > 0) {

                } else {
                    c.clipRect(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
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
            selectedDateAsString = DateToString.toString(selectedDate)
            activeRoutineList = routines[selectedDateAsString] as ArrayList<Exercise>
            routineRecyclerViewAdapter = RoutineRecyclerViewAdapter(activeRoutineList)
            adapter = routineRecyclerViewAdapter
        }
    }

    override fun onClick(v: View?) {
        if (v is CheckedTextView) {
            if (!v.isChecked) {
                v.checkMarkDrawable = resources.getDrawable(R.drawable.ic_check)
                v.checkMarkTintList = ColorStateList.valueOf(resources.getColor(R.color.pastelred))
                v.isChecked = true
            } else {
                v.isChecked = false
                v.checkMarkDrawable = resources.getDrawable(R.drawable.ic_uncheck)
                v.checkMarkTintList = ColorStateList.valueOf(resources.getColor(R.color.darkgrey))
            }
        }
    }
}
