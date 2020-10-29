package com.kirvigen.usersphoto.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.usersphoto.Objects.User
import com.kirvigen.usersphoto.R

class UserAdapter(private val users:MutableList<User>): RecyclerView.Adapter<UserAdapter.Holder>() {

    private var onClickListener:((User)->Unit)? = null

    fun setOnClickListener(onClickListener: ((User)->Unit)){ this.onClickListener = onClickListener }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate( R.layout.item_user, parent, false ))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = users[position]
        holder.itemView.setOnClickListener { onClickListener?.let { it(user) } }
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user:User){
            itemView.findViewById<TextView>(R.id.name).text = user.name
        }
    }
}