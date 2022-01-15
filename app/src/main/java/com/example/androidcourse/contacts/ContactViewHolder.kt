package com.example.androidcourse.contacts

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcourse.R

class ContactViewHolder(val item: ConstraintLayout): RecyclerView.ViewHolder(item) {
    val imageView = item.findViewById<ImageView>(R.id.userImage)
    val nameView = item.findViewById<TextView>(R.id.userName)
    val groupView = item.findViewById<TextView>(R.id.userGroup)
}