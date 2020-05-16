package com.example.focusproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.focusproject.R
import com.example.focusproject.models.Exercise
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.excercise_item.view.excerciseImageHolder
import kotlinx.android.synthetic.main.excercise_item.view.excerciseNameTextView
import kotlinx.android.synthetic.main.resttime_item.view.*
import kotlinx.android.synthetic.main.rountine_item.view.*

class RoutineRecyclerViewAdapter(exercises: ArrayList<Exercise>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var Exercises = exercises
    private var firestore = FirebaseFirestore.getInstance()
    val REST = 1
    val EXCERCISE = 2

    fun updateDataSet(exercises: ArrayList<Exercise>){
        this.Exercises = exercises
    }

    fun removeItemAt(position: Int){
        Exercises.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var routine_item : View
        if (viewType == REST){
            routine_item = inflater.inflate(R.layout.resttime_item, parent, false)
        } else {
            routine_item = inflater.inflate(R.layout.rountine_item, parent, false)
        }
        var routinerViewHolder = RoutineRecyclerViewAdapter.RoutineViewHolder(routine_item)
        return routinerViewHolder
    }

    override fun getItemCount(): Int {
        return Exercises.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is RoutineViewHolder -> {
                holder.bind(Exercises.get(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (Exercises.get(position).isRestTime){
            return REST
        } else {
            return EXCERCISE
        }
    }

    class RoutineViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.excerciseImageHolder
        var excerciseNameTextView = itemView.excerciseNameTextView
        var equipmentOrWeightTextView = itemView.equipmentOrWeightTextView
        var repTextView = itemView.reprange_textview
        var timeTextView = itemView.time_textview

        fun bind(exercise : Exercise){
            if (!exercise.isRestTime) {
                excerciseNameTextView.text = exercise.name
                if (exercise.weight > 0) {
                    equipmentOrWeightTextView.text = String.format("%d %%RM", exercise.weight)
                } else {
                    equipmentOrWeightTextView.text = "NA"
                }

                if (exercise.rep > 0){
                    repTextView.text = "REPETITION: ${exercise.rep}"
                } else {
                    repTextView.text = "REPETITION: NA"
                }
                //Bind image
                var url = ""
                if (exercise.img != ""){
                    url = exercise.img
                } else if (exercise.vidId != ""){
                    url = "https://img.youtube.com/vi/" + exercise.vidId + "/0.jpg"
                }
                Glide.with(itemView.context)  //2
                    .load(url) //3
                    .centerCrop() //4
                    .into(imageView) //8
            } else {
                timeTextView.text = "${exercise.duration} SECS"
            }
        }
    }

}