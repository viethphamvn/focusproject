package com.example.focusproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.focusproject.R
import com.example.focusproject.models.Excercise
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.excercise_item.view.*

class ExcerciseRecyclerViewAdapter(Excercises: ArrayList<Excercise>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var Excercises = ArrayList(Excercises)
    private var firestore = FirebaseFirestore.getInstance()

    public fun updateDataSet(Excercises: ArrayList<Excercise>){
        this.Excercises = ArrayList(Excercises)
        notifyDataSetChanged()
    }

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

    class ExcerciseViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.excerciseImageHolder
        var excerciseNameTextView = itemView.excerciseNameTextView

        fun bind(excercise : Excercise){
            excerciseNameTextView.setText(excercise.name)
            //Bind image
            val url = "https://compote.slate.com/images/697b023b-64a5-49a0-8059-27b963453fb1.gif"
            Glide.with(itemView.context)  //2
                .load(url) //3
                .centerCrop() //4
                .into(imageView) //8

        }
    }
}