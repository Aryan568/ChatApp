package com.example.whatsappclone.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.whatsappclone.Activity.modal.UserModal
import com.example.whatsappclone.R
import com.example.whatsappclone.adapter.chatAdapter
import com.example.whatsappclone.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Chat_Fragment : Fragment() {

    lateinit var binding : FragmentChatBinding
    private var database : FirebaseDatabase?= null
    lateinit var userlist : ArrayList<UserModal>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance()

        userlist = ArrayList()

        database!!.reference.child("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userlist.clear()
                   for(snapshot1 in snapshot.children){
                       val user  = snapshot1.getValue(UserModal::class.java)
                       if (user!!.uid != FirebaseAuth.getInstance().uid){
                           userlist.add(user)
                       }
                   }

                    binding.userlistRecyclerView.adapter = chatAdapter(requireContext(),userlist)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        return binding.root
    }


}