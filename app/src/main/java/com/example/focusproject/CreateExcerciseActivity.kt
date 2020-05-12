package com.example.focusproject
import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.fragments.ExcercisePickerFragment
import com.example.focusproject.models.Exercise
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList


class CreateExcerciseActivity : AppCompatActivity() {

    private var newWorkout : ArrayList<Exercise> = ArrayList()
    private lateinit var touchHelper : ItemTouchHelper
    private lateinit var routine_recycler_list_view: RecyclerView
    private lateinit var routineRecyclerViewAdapter: RoutineRecyclerViewAdapter

    private lateinit var deleteIcon: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_excercise)
        //Drawable Objects
        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)!!
        setUpRecyclerView()

        var saveBtn = findViewById<Button>(R.id.saveButton)
        var cancelBtn = findViewById<Button>(R.id.cancel_button)
        var addBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton_addAction)

        saveBtn.setOnClickListener {
            var returnIntent: Intent = Intent()
            returnIntent.putExtra("newworkout", newWorkout)
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
                screen_height = (0.60 * screen_height).toInt()
                val parms = layout.layoutParams
                parms.height = screen_height
                layout.layoutParams = parms
            }
        }

        cancelBtn.setOnClickListener{
            finish()
        }

    }

    fun onItemClick(item: Exercise){
        newWorkout.add(item)
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
        routine_recycler_list_view.apply {
            layoutManager = LinearLayoutManager(context)
            routineRecyclerViewAdapter = RoutineRecyclerViewAdapter(newWorkout)
            adapter = routineRecyclerViewAdapter
        }
        touchHelper.attachToRecyclerView(routine_recycler_list_view)
    }
}
