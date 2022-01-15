package com.example.androidcourse.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidcourse.R

class ContactsRecyclerViewAdapter : RecyclerView.Adapter<ContactViewHolder>() {
    var userData: List<User> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user, parent, false) as ConstraintLayout
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        Glide.with(holder.imageView)
            .load(userData[position].avatarUrl)
            .circleCrop()
            .into(holder.imageView)
        holder.nameView.text = userData[position].userName
        holder.groupView.text = userData[position].groupName
    }

    override fun getItemCount(): Int {
        return userData.size
    }
}