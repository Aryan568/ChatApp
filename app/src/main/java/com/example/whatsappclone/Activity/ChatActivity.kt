package com.example.whatsappclone.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.whatsappclone.API.Notification
import com.example.whatsappclone.API.NotificationData
import com.example.whatsappclone.API.RetrofitInstance
import com.example.whatsappclone.Activity.modal.MessageModel
import com.example.whatsappclone.R
import com.example.whatsappclone.adapter.MessageAdapter
import com.example.whatsappclone.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChatActivity : AppCompatActivity(){

     private lateinit var binding : ActivityChatBinding
     private lateinit var database: FirebaseDatabase

     private lateinit var senderUid: String
     private lateinit var receiverUid : String

     private lateinit var senderRoom : String

     private lateinit var receiverRoom : String

     private lateinit var list : ArrayList<MessageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        senderUid = FirebaseAuth.getInstance().uid.toString()
        receiverUid = intent.getStringExtra("uid")!!

        list = ArrayList()

        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid





        database = FirebaseDatabase.getInstance()

        fetchSenderProfile()


        binding.ImgSend.setOnClickListener {

            if (binding.EdtMsgBox.text.isEmpty()){
                Toast.makeText(this@ChatActivity, "Please enter a message", Toast.LENGTH_SHORT).show()

            } else {

                val message = MessageModel(binding.EdtMsgBox.text.toString() , senderUid , Date().time)

                val randomKey = database.reference.push().key

                database.reference.child("chats")
                    .child(senderRoom).child("message")
                    .child(randomKey!!).setValue(message)
                    .addOnSuccessListener {

                        database.reference.child("chats")
                            .child(receiverRoom).child("message")
                            .child(randomKey!!).setValue(message).addOnSuccessListener {

                               Toast.makeText(this@ChatActivity, "Message sent", Toast.LENGTH_SHORT).show()

                                database.reference.child("Users")
                                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            Toast.makeText(applicationContext, binding.EdtMsgBox.text.toString(), Toast.LENGTH_SHORT).show()

                                            RetrofitInstance.getInterface().sendNotification(
                                                Notification(
                                                    intent.getStringExtra("fcm"),
                                                    NotificationData(
                                                        snapshot.child("name").value.toString(),
                                                        binding.EdtMsgBox.text.toString(),
                                                        snapshot.child("uid").value.toString(),
                                                        snapshot.child("fcmToken").value.toString()
                                                        )
                                                )

                                            ).enqueue(object : Callback<Notification?> {
                                                override fun onResponse(
                                                    call: Call<Notification?>,
                                                    response: Response<Notification?>
                                                ) {
                                                }

                                                override fun onFailure(
                                                    call: Call<Notification?>,
                                                    t: Throwable
                                                ) {
                                                }
                                            })

                                            binding.EdtMsgBox.text = null

                                        }

                                        override fun onCancelled(error: DatabaseError) {

                                        }
                                    })

                            }
                    }

            }
        }


        database.reference.child("chats")
            .child(senderRoom).child("message")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    list.clear()

                    for (snapshot1 in snapshot.children){

                        val data = snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)

                    }

                    binding.RecyclerView2.adapter = MessageAdapter(this@ChatActivity, list)
                }

                override fun onCancelled(error: DatabaseError) {
                 //   Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@ChatActivity, "Error : $error", Toast.LENGTH_SHORT).show()
                }
            })


    }

    private fun fetchSenderProfile() {
        val receiverRef = database.reference.child("Users").child(receiverUid)
        receiverRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val username = snapshot.child("name").value.toString()
                    val profileImageUrl = snapshot.child("imageUrl").value.toString()

                    // Update sender's profile image using Glide or any other image loading library
                    Glide.with(this@ChatActivity)
                        .load(profileImageUrl)
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.avatar)
                        .into(binding.profileImg)

                    binding.userName.text = username
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }
}