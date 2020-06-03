package com.example.focusproject
import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.fragments.ExercisePickerFragment
import com.example.focusproject.models.Exercise
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CreateRoutineActivity : AppCompatActivity() {

    private lateinit var newWorkout : ArrayList<Exercise>
    private lateinit var newWorkoutJustId: ArrayList<String>
    private lateinit var touchHelper : ItemTouchHelper
    private lateinit var routineRecyclerListView: RecyclerView
    private lateinit var routineRecyclerViewAdapter: RoutineRecyclerViewAdapter
    private var routineTitle : String? = null
    private var routineId : String? = null

    private lateinit var deleteIcon: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)

        newWorkout = if (intent.getSerializableExtra("routine") != null){
            intent.getSerializableExtra("routine") as ArrayList<Exercise>
        } else {
            ArrayList()
        }

        newWorkoutJustId = if (intent.getSerializableExtra("routinejustid") != null){
            intent.getSerializableExtra("routinejustid") as ArrayList<String>
        } else {
            ArrayList()
        }

        routineTitle = if (intent.getStringExtra("routinetitle") != null){
            intent.getStringExtra("routinetitle") as String
        } else {
            null
        }

        routineId = if (intent.getStringExtra("id") != null){
            intent.getStringExtra("id") as String
        } else {
            null
        }

        //Drawable Objects
        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)!!
        setUpRecyclerView()

        var saveBtn = findViewById<FloatingActionButton>(R.id.saveButton)
        var cancelBtn = findViewById<ImageButton>(R.id.cancel_button)
        var addBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton_addAction)
        var routineName = findViewById<EditText>(R.id.newRoutineNameTextView) as TextView

        if (routineTitle != null){
            routineName.text = routineTitle
        }

        saveBtn.setOnClickListener {
            if (newWorkout.size > 0 && routineName.text.toString() != "") {
                uploadData(routineName.text.toString())
            } else {
                if (newWorkout.size == 0){
                    Toast.makeText(this, "Your routine is empty!", Toast.LENGTH_SHORT).show()
                }
                if (routineName.text.toString() == ""){
                    Toast.makeText(this, "Your routine needs a title", Toast.LENGTH_SHORT).show()
                }
            }
        }

        addBtn.setOnClickListener{
            var exercisePickerFragment = supportFragmentManager.findFragmentByTag("pickerFragment")
            if (exercisePickerFragment != null){
                supportFragmentManager.beginTransaction()
                    .remove(exercisePickerFragment)
                    .commit()
                val display = windowManager.defaultDisplay
                val layout = findViewById<FrameLayout>(R.id.exercisePickerContainer)
                var screenHeight = display.height
                screenHeight = 0
                val params = layout.layoutParams
                params.height = screenHeight
                layout.layoutParams = params
                addBtn.setImageResource(R.drawable.ic_add)
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.exercisePickerContainer, ExercisePickerFragment.newInstance(),"pickerFragment")
                    .commit()
                val display = windowManager.defaultDisplay
                val layout = findViewById<FrameLayout>(R.id.exercisePickerContainer)
                var screenHeight = display.height
                screenHeight = (0.60 * screenHeight).toInt()
                val parms = layout.layoutParams
                parms.height = screenHeight
                layout.layoutParams = parms
                addBtn.setImageResource(R.drawable.ic_down)
            }
        }

        cancelBtn.setOnClickListener{
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }

    private fun uploadData(routineName: String) {
        var firestore = FirebaseFirestore.getInstance().collection("Routines")
        var newRoutineHashMap = HashMap<String, Any>()

        if (routineId == null){
            routineId = FirebaseFirestore.getInstance().collection("Routines").document().id
        }

        newRoutineHashMap["createdBy"] = FirebaseAuth.getInstance().currentUser!!.uid
        newRoutineHashMap["createdOn"] = System.currentTimeMillis()
        newRoutineHashMap["name"] = routineName
        newRoutineHashMap["exercises"] = newWorkoutJustId
        newRoutineHashMap["id"] = routineId!!
        firestore.document(routineId!!).set(newRoutineHashMap).addOnSuccessListener {
            Toast.makeText(this, "Your routine has been posted!", Toast.LENGTH_SHORT).show()
            var returnIntent = Intent()
            returnIntent.putExtra("title", routineName)
            returnIntent.putExtra("routine", newWorkoutJustId)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    fun onItemClick(item: Exercise){
        newWorkout.add(item)
        newWorkoutJustId.add(item.uid)
        routineRecyclerViewAdapter.notifyItemInserted(newWorkout.size-1)
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
                Collections.swap(newWorkout, sourcePosition, targetPosition)
                routineRecyclerViewAdapter.notifyItemMoved(sourcePosition, targetPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var deletedItemPosition = viewHolder.adapterPosition
                routineRecyclerViewAdapter.removeItemAt(deletedItemPosition)
                newWorkoutJustId.removeAt(deletedItemPosition)
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
        routineRecyclerListView = findViewById(R.id.routine_recycler_list_view)
        routineRecyclerListView.apply {
            layoutManager = LinearLayoutManager(context)
            routineRecyclerViewAdapter = RoutineRecyclerViewAdapter(newWorkout)
            adapter = routineRecyclerViewAdapter
        }
        touchHelper.attachToRecyclerView(routineRecyclerListView)
    }
}
