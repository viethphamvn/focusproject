package com.example.focusproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.focusproject.R
import com.example.focusproject.models.Excercise
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.excercise_item.view.*
import kotlinx.android.synthetic.main.resttime_item.view.*

class RoutineRecyclerViewAdapter(Excercises: ArrayList<Excercise>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var Excercises = ArrayList(Excercises)
    private var firestore = FirebaseFirestore.getInstance()
    val REST = 1
    val EXCERCISE = 2

    fun updateDataSet(Excercises: ArrayList<Excercise>){
        this.Excercises = Excercises
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
        return Excercises.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is RoutineViewHolder -> {
                holder.bind(Excercises.get(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (Excercises.get(position).isRestTime){
            return REST
        } else {
            return EXCERCISE
        }
    }

    class RoutineViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.excerciseImageHolder
        var excerciseNameTextView = itemView.excerciseNameTextView
        var timeTextView = itemView.time_textview

        fun bind(excercise : Excercise){
            if (!excercise.isRestTime) {
                excerciseNameTextView.text = excercise.name
                //Bind image
                val url =
                    "https://compote.slate.com/images/697b023b-64a5-49a0-8059-27b963453fb1.gif"
                Glide.with(itemView.context)  //2
                    .load(url) //3
                    .centerCrop() //4
                    .into(imageView) //8
            } else {
                timeTextView.text = "${excercise.duration} SECS"
            }
        }
    }

}