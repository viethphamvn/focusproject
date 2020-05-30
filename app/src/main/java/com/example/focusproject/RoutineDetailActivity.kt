package com.example.focusproject

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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

class RoutineDetailActivity : AppCompatActivity() {
    private lateinit var routine : Routine
    var exerciseList = ArrayList<Exercise>()
    var totalDuration: Long  = 0
    private lateinit var actionButton : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_detail)

        routine = intent.getSerializableExtra("routine") as Routine
        if (routine != null){
            var exerciseRecyclerViewAdapter = RoutineRecyclerViewAdapter(exerciseList)
            findViewById<RecyclerView>(R.id.exerciseItemRecyclerView).apply {
                layoutManager = LinearLayoutManager(this@RoutineDetailActivity)
                adapter = exerciseRecyclerViewAdapter
            }

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

            findViewById<TextView>(R.id.routine_name_text_view).text = routine.name
            findViewById<TextView>(R.id.totalExerciseTextView).text = routine.exerciseList.size.toString()


            actionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton_saveAction)

            FirebaseFirestore.getInstance().collection("Users").document(User.currentUser.id)
                .get()
                .addOnSuccessListener {result ->
                    if (result.get("savedRoutines") != null){
                        var savedRoutinesList = result.get("savedRoutines") as ArrayList<String>
                        if (savedRoutinesList.contains(routine.id)){
                            setActionButtonToDelete()
                        } else {
                            setActionButtonToSave()
                        }
                    }
                }
        }
    }

    private fun setActionButtonToDelete(){
        actionButton.apply {
            backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.pastelred))
            setImageResource(R.drawable.ic_delete)
            setOnClickListener {
                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                    .update("savedRoutines", FieldValue.arrayRemove(routine.id))
                    .addOnSuccessListener {
                        setActionButtonToSave()
                    }

            }
        }
    }

    private fun setActionButtonToSave(){
        actionButton.apply {
            backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            setImageResource(R.drawable.ic_save)
            setOnClickListener {
                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                    .update("savedRoutines", FieldValue.arrayUnion(routine.id))
                    .addOnSuccessListener {
                        setActionButtonToDelete()
                    }
            }
        }
    }
}
