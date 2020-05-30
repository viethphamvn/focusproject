package com.example.focusproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focusproject.adapters.ExerciseRecyclerViewAdapter
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.models.Exercise
import com.example.focusproject.models.Routine
import com.example.focusproject.tools.CreateExercise
import com.example.focusproject.tools.FireStore
import org.w3c.dom.Text

class RoutineDetailActivity : AppCompatActivity() {
    private lateinit var routine : Routine
    var exerciseList = ArrayList<Exercise>()
    var totalDuration: Long  = 0

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
                FireStore.fireStore.collection("Exercise").document(id)
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


        }
    }
}