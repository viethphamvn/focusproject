package com.example.focusproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.focusproject.R
import com.example.focusproject.models.Exercise
import kotlinx.android.synthetic.main.excercise_thumbnail_item.view.excerciseImageHolder
import kotlinx.android.synthetic.main.excercise_thumbnail_item.view.exerciseNameTextView
import kotlinx.android.synthetic.main.resttime_item.view.*
import kotlinx.android.synthetic.main.exercise_item.view.*

class RoutineRecyclerViewAdapter(exercises: ArrayList<Exercise>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var Exercises = exercises
    private val REST = 1
    private val EXCERCISE = 2

    fun updateDataSet(exercises: ArrayList<Exercise>){
        this.Exercises = exercises
        notifyDataSetChanged()
    }

    fun removeItemAt(position: Int){
        Exercises.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val routineItem : View
        routineItem = if (viewType == REST){
            inflater.inflate(R.layout.resttime_item, parent, false)
        } else {
            inflater.inflate(R.layout.exercise_item, parent, false)
        }
        return RoutineViewHolder(routineItem)
    }

    override fun getItemCount(): Int {
        return Exercises.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is RoutineViewHolder -> {
                holder.bind(Exercises[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (Exercises[position].isRestTime){
            REST
        } else {
            EXCERCISE
        }
    }

    class RoutineViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imageView: ImageView? = itemView.excerciseImageHolder
        private var exerciseNameTextView: TextView? = itemView.exerciseNameTextView
        private var equipmentOrWeightTextView: TextView? = itemView.equipmentOrWeightTextView
        private var repTextView: TextView? = itemView.reprange_textview
        private var timeTextView: TextView? = itemView.time_textview

        fun bind(exercise : Exercise){
            if (!exercise.isRestTime) {
                exerciseNameTextView!!.text = exercise.name
                if (exercise.weight > 0) {
                    equipmentOrWeightTextView!!.text = String.format("%d %%RM", exercise.weight)
                } else {
                    equipmentOrWeightTextView!!.text = "NA"
                }

                if (exercise.rep > 0){
                    repTextView!!.text = "REPETITION: ${exercise.rep}"
                } else {
                    repTextView!!.text = "REPETITION: NA"
                }
                //Bind image
                if (imageView != null) {
                    var url = ""
                    if (exercise.img != "") {
                        url = exercise.img
                    } else if (exercise.vidId != "") {
                        url = "https://img.youtube.com/vi/" + exercise.vidId + "/0.jpg"
                    }
                    Glide.with(itemView.context)
                        .load(url)
                        .centerCrop()
                        .into(imageView!!)
                }
            } else {
                timeTextView!!.text = "${exercise.duration} SECS"
            }
        }
    }

}