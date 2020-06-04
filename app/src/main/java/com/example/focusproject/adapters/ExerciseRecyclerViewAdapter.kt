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
import kotlinx.android.synthetic.main.excercise_thumbnail_item.view.*

class ExerciseRecyclerViewAdapter(exercises: ArrayList<Exercise>, val adapterOnClick: (Exercise) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var Exercises = exercises

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val exerciseItem = inflater.inflate(R.layout.excercise_thumbnail_item, parent, false)
        return ExerciseViewHolder(exerciseItem)
    }

    override fun getItemCount(): Int {
        return Exercises.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is ExerciseViewHolder -> {
                holder.bind(Exercises[position])
            }
        }
    }

    inner class ExerciseViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imageView: ImageView = itemView.excerciseImageHolder
        private var excerciseNameTextView: TextView = itemView.exerciseNameTextView

        fun bind(exercise : Exercise){
            excerciseNameTextView.text = exercise.name
            //Bind image
            var url = ""
            if (exercise.img != ""){
                url = exercise.img
            } else if (exercise.vidId != ""){
                url = "https://img.youtube.com/vi/" + exercise.vidId + "/0.jpg"
            }
            Glide.with(itemView.context)
                .load(url)
                .centerCrop()
                .into(imageView)
            setItem(exercise)
        }

        private fun setItem(item: Exercise){
            itemView.setOnClickListener{
                adapterOnClick(item)
            }
        }
    }
}