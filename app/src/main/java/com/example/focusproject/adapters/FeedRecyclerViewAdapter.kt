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

class FeedRecyclerViewAdapter (routineList: ArrayList<Routine>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var routineList = routineList

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
        var routineNameTextView: TextView = itemView.routine_name

        fun bind(routine : Routine){
            getInfo(routine)
            routineNameTextView.text = routine.name
            itemView.setOnClickListener {
                //launch a new activity or a dialog window
            }
        }

        private fun getInfo(routine: Routine){
            var firestore = FirebaseFirestore.getInstance()
            firestore.collection("Users").document(routine.createdBy)
                .get()
                .addOnSuccessListener {
                    if (it.get("username") != null) {
                        usernameTextView.text = it.get("username").toString()
                    }
                    if (it.get("profilePicture") != null){
                        var url = ""
                        if (it.get("profilePicture").toString() != ""){
                            url = it.get("profilePicture").toString()
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
    }

}