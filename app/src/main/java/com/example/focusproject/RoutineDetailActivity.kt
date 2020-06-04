package com.example.focusproject

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.models.Exercise
import com.example.focusproject.models.Routine
import com.example.focusproject.models.User
import com.example.focusproject.tools.CreateExercise
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_routine_detail.*

const val MODIFY_ROUTINE_REQUEST_CODE = 300

class RoutineDetailActivity : AppCompatActivity() {
    private lateinit var routine : Routine
    private var exerciseList = ArrayList<Exercise>()
    private var totalDuration: Long  = 0
    private lateinit var actionButton : FloatingActionButton
    private lateinit var exerciseRecyclerViewAdapter : RoutineRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_detail)

        routine = intent.getSerializableExtra("routine") as Routine
        if (routine != null){
            exerciseRecyclerViewAdapter = RoutineRecyclerViewAdapter(exerciseList)
            findViewById<RecyclerView>(R.id.exerciseItemRecyclerView).apply {
                layoutManager = LinearLayoutManager(this@RoutineDetailActivity)
                adapter = exerciseRecyclerViewAdapter
            }

            getExercises()

            findViewById<TextView>(R.id.routine_name_text_view).text = routine.name

            actionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton_saveAction)
            if (routine.createdBy == User.currentUser.id){
                actionButton.apply {
                    setImageResource(R.drawable.ic_modify)
                    setOnClickListener {
                        var intent = Intent(context, CreateRoutineActivity::class.java)
                        intent.putExtra("routine", exerciseList)
                        intent.putExtra("routinejustid", routine.exerciseList)
                        intent.putExtra("routinetitle", routine.name)
                        intent.putExtra("id", routine.id)
                        startActivityForResult(intent, MODIFY_ROUTINE_REQUEST_CODE)
                    }
                }
            } else {
                setActionButtonToSave()
            }
            start_workout_btn.setOnClickListener {
                var intent = Intent(this, StartRoutineActivity::class.java)
                intent.putExtra("routine", exerciseList)
                startActivity(intent)
            }
        }
    }

//    private fun setActionButtonToDelete(){
//        actionButton.apply {
//            backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.pastelred))
//            setImageResource(R.drawable.ic_delete)
//            setOnClickListener {
//                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid)
//                    .update("savedRoutines", FieldValue.arrayRemove(routine.id))
//                    .addOnSuccessListener {
//                        Toast.makeText(context, "Routine is removed!", Toast.LENGTH_SHORT).show()
//                        setActionButtonToSave()
//                    }
//
//            }
//        }
//    }

//    private fun setUpActionButton(){
//        //find out if routine is saved in the user's library and set action button accordingly
//
//        FirebaseFirestore.getInstance().collection("Users").document(User.currentUser.id)
//            .get()
//            .addOnSuccessListener { result ->
//                if (result.get("savedRoutines") != null) {
//                    var savedRoutinesList = result.get("savedRoutines") as ArrayList<String>
//                    if (savedRoutinesList.contains(routine.id)) {
//                        setActionButtonToDelete()
//                    } else {
//                        setActionButtonToSave()
//                    }
//                }
//            }
//    }

    private fun getExercises(){
        exerciseRecyclerViewAdapter.notifyItemRangeRemoved(0, exerciseList.size)
        exerciseList.clear()
        totalDuration = 0
        for (id in routine.exerciseList){
            FirebaseFirestore.getInstance().collection("Exercise").document(id)
                .get()
                .addOnSuccessListener {
                    exerciseList.add(CreateExercise.createExercise(it))
                    totalDuration += exerciseList[exerciseList.size-1].duration
                    findViewById<TextView>(R.id.totalDurationTextView).text = "${(totalDuration / 60)} mins"
                    exerciseRecyclerViewAdapter.notifyItemInserted(exerciseList.size-1)
                }
        }
        findViewById<TextView>(R.id.totalExerciseTextView).text = routine.exerciseList.size.toString()
    }

    private fun setActionButtonToSave(){
        actionButton.apply {
            backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            setImageResource(R.drawable.ic_save)
            setOnClickListener {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.save_routine_to_location_dialog)
        val toLibraryBtn = dialog.findViewById(R.id.toMyLibrBtn) as Button
        val toRoutineBtn = dialog.findViewById(R.id.toRoutineBtn) as Button
        toLibraryBtn.setOnClickListener {
            saveCurrentRoutineToMyLibary()
            dialog.cancel()
        }
        toRoutineBtn.setOnClickListener {
            //Start activity RoutineEditActivity.kt
            var intent = Intent(this, RoutineEditActivity::class.java)
            intent.putExtra("date", MainActivity.todayDate)
            intent.putExtra("routine", exerciseList)
            startActivity(intent)
            dialog.cancel()
        }
        dialog.show()
    }

    private fun saveCurrentRoutineToMyLibary() {
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .update("savedRoutines", FieldValue.arrayUnion(routine.id))
            .addOnSuccessListener {
                Toast.makeText(this, "Routine is saved!", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MODIFY_ROUTINE_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    if (data.getSerializableExtra("routine") != null) {
                        routine.exerciseList =
                            data.getSerializableExtra("routine") as ArrayList<String>
                    }
                    if (data.getStringExtra("title") != null) {
                        var routineTitle = data.getStringExtra("title") as String
                        findViewById<TextView>(R.id.routine_name_text_view).text = routineTitle
                        routine.name = routineTitle
                    }
                    getExercises()
                }
            }
        }
    }


}
