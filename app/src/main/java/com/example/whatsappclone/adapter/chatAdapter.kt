package com.example.whatsappclone.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone.Activity.ChatActivity
import com.example.whatsappclone.Activity.modal.UserModal
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.ChatUserItemLayoutBinding

class chatAdapter (val context: Context , val list : ArrayList<UserModal>):RecyclerView.Adapter<chatAdapter.chatViewHolder>() {

    inner class chatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var binding: ChatUserItemLayoutBinding = ChatUserItemLayoutBinding.bind(view)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatViewHolder {

        return chatViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_user_item_layout, parent, false)
        )


    }

    override fun onBindViewHolder(holder: chatViewHolder, position: Int) {
        val user = list[position]
        Glide.with(context).load(user.imageUrl).into(holder.binding.userImage2)
        holder.binding.userName2.text = user.name
        holder.itemView.setOnClickListener {

            Toast.makeText(context, "Chat opened", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("uid",user.uid)
            intent.putExtra("fcm",user.fcmToken)
            intent.putExtra("name",user.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}