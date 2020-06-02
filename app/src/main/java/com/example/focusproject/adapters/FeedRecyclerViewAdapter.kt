package com.example.focusproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.focusproject.R
import com.example.focusproject.models.Routine
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.routine_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FeedRecyclerViewAdapter (routineList: ArrayList<Routine>, val onClick: (Routine) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var routineList = routineList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var routineCard = inflater.inflate(R.layout.routine_item, parent, false)
        return UserViewHolder(routineCard)
    }

    override fun getItemCount(): Int {
        return routineList.size
    }

    fun  setNewData(routineList: ArrayList<Routine>){
        this.routineList = ArrayList(routineList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is UserViewHolder -> {
                holder.bind(routineList[position])
            }
        }
    }

    inner class UserViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var profileImageView: CircleImageView = itemView.user_profile_picture
        private var usernameTextView: TextView = itemView.username_textview
        private var routineNameTextView: TextView = itemView.routine_name
        private var timestampTextView: TextView = itemView.timeStampTextView

        fun bind(routine : Routine){
            getInfo(routine)
            routineNameTextView.text = routine.name
            timestampTextView.text = "Created on ${getDate(routine.createdOn, "dd/MM/yyyy hh:mm:ss")}"
            itemView.setOnClickListener {
                onClick(routine)
            }
        }

        private fun getInfo(routine: Routine){
            FirebaseFirestore.getInstance().collection("Users").document(routine.createdBy)
                .get()
                .addOnSuccessListener {
                    if (it.get("username") != null) {
                        usernameTextView.text = it.get("username").toString()
                    }
                    if (it.get("profilePictureUri") != null){
                        var url = ""
                        if (it.get("profilePictureUri").toString() != ""){
                            url = it.get("profilePictureUri").toString()
                        } else {
                            //use default profile picture
                            //url = "Some URL"
                        }
                        Glide.with(itemView.context)
                            .load(url)
                            .centerCrop()
                            .into(profileImageView)
                    }
                }
        }

        private fun getDate(milliSeconds: Long, dateFormat: String?): String {
            // Create a DateFormatter object for displaying date in specified format.
            val formatter = SimpleDateFormat(dateFormat)

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }
    }

}