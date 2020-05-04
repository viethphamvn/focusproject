package com.example.focusproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.focusproject.R
import com.example.focusproject.fragments.MuscleGroupFragment
import com.example.focusproject.models.Excercise
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.excercise_item.view.*

class ExcerciseRecyclerViewAdapter(Excercises: ArrayList<Excercise>, val adapterOnClick: (Excercise) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var Excercises = Excercises

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var excercise_item = inflater.inflate(R.layout.excercise_item, parent, false)
        var excerciseViewHolder = ExcerciseViewHolder(excercise_item)
        return excerciseViewHolder
    }

    override fun getItemCount(): Int {
        return Excercises.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is ExcerciseViewHolder -> {
                holder.bind(Excercises.get(position))
            }
        }
    }

    inner class ExcerciseViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.excerciseImageHolder
        var excerciseNameTextView = itemView.excerciseNameTextView

        fun bind(excercise : Excercise){
            excerciseNameTextView.text = excercise.name
            //Bind image
            var url = ""
            if (excercise.img != ""){
                url = excercise.img
            } else if (excercise.vidId != ""){
                url = "https://img.youtube.com/vi/" + excercise.vidId + "/0.jpg"
            }
            Glide.with(itemView.context)  //2
                .load(url) //3
                .centerCrop() //4
                .into(imageView) //8
            setItem(excercise)
        }

        fun setItem(item: Excercise){
            itemView.setOnClickListener{
                adapterOnClick(item)
            }
        }
    }
}