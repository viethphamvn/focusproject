package com.example.focusproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.focusproject.R
import com.example.focusproject.models.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.user_card.view.*

class UserRecyclerViewAdapter (Users: ArrayList<User>, val onClick: (User) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var userList = Users

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var userCard = inflater.inflate(R.layout.user_card, parent, false)
        return UserViewHolder(userCard)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setNewData(userList: ArrayList<User>){
        this.userList = ArrayList(userList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is UserViewHolder -> {
                holder.bind(userList[position])
            }
        }
    }

    inner class UserViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var profileImageView: CircleImageView = itemView.user_profile_picture
        var usernameTextView: TextView = itemView.username_textview

        fun bind(user : User){
            usernameTextView.text = user.username
            //Bind image
            var url = ""
            url = if (user.profilePicture != ""){
                user.profilePicture
            } else {
                //use default profile picture
                "https://cdn.vox-cdn.com/thumbor/vbxRVJGeYs4rAJp_dlN2Swx3eKg=/1400x1400/filters:format(png)/cdn.vox-cdn.com/uploads/chorus_asset/file/19921093/mgidarccontentnick.comc008fa9d_d.png"
            }
            Glide.with(itemView.context)
                .load(url)
                .centerCrop()
                .into(profileImageView)

            itemView.setOnClickListener {
                onClick(user)
            }
        }
    }
}