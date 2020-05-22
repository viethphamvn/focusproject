package com.example.focusproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.focusproject.R
import com.example.focusproject.models.User
import kotlinx.android.synthetic.main.user_card.view.*

class UserRecyclerViewAdapter (Users: ArrayList<User>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var Users = Users

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var userCard = inflater.inflate(R.layout.user_card, parent, false)
        return UserViewHolder(userCard)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    inner class UserViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var profileImageView = itemView.user_profile_picture
        var usernameTextView = itemView.username_textview

        fun bind(user : User){
            usernameTextView.text = user.username
            //Bind image
            var url = ""
            if (user.profilePicture != ""){
                url = user.profilePicture
            } else {
                //use default profile picture
                //url = "Some URL"
            }
            Glide.with(itemView.context)
                .load(url)
                .centerCrop()
                .into(profileImageView)

            itemView.setOnClickListener {
                //launch a new activity or a dialog window
            }
        }
    }
}