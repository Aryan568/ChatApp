package com.example.whatsappclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.whatsappclone.Activity.modal.MessageModel
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.RecieverItemLayoutBinding
import com.example.whatsappclone.databinding.SentItemLayoutBinding
import com.google.firebase.auth.FirebaseAuth


class MessageAdapter (val context: Context, val list : ArrayList<MessageModel>):RecyclerView.Adapter<RecyclerView.ViewHolder> (){


    var ITEM_SENT = 1

    var ITEM_RECEIVER = 2



     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         return if (viewType == ITEM_SENT)

             SenderViewHolder(
                 LayoutInflater.from(context).inflate(R.layout.sent_item_layout, parent, false)

             )  else RecieverViewHolder (
               LayoutInflater.from(context).inflate(R.layout.reciever_item_layout ,parent,false )

             )
     }

     override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
         val message = list[position]
         if (holder.itemViewType == ITEM_SENT){

             val viewHolder = holder as SenderViewHolder
             viewHolder.binding.TxtSendtxt.text = message.message

         } else {

             val viewHolder = holder as RecieverViewHolder
             viewHolder.binding.TxtRecievertxt.text = message.message
         }
     }


    override fun getItemViewType(position: Int): Int {


        return if (FirebaseAuth.getInstance().uid == list[position].senderId) ITEM_SENT else ITEM_RECEIVER

    }

     override fun getItemCount(): Int {
         return list.size
     }

     inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         var binding  = SentItemLayoutBinding.bind(itemView)
     }

     inner class RecieverViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

         var binding = RecieverItemLayoutBinding.bind(itemView)

     }



 }












