package com.example.whatsappclone.Activity

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.ActivityUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class userProfile : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var userImage: CircleImageView
    private lateinit var userRef: DatabaseReference
    private lateinit var userName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        usrProf= intent.getStringExtra("uid").toString()
//
//        database= FirebaseDatabase.getInstance()

        userImage = findViewById(R.id.userImage)
        userName = findViewById(R.id.userName)

        val mAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = mAuth.currentUser

        if (currentUser != null) {
            // Assuming your database structure has a "users" node
            userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.uid)

            // Fetch and load the image using Glide
            userRef.child("imageUrl").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val imageUrl: String? = dataSnapshot.value as? String
                    if (!imageUrl.isNullOrBlank()) {
                        Glide.with(this@userProfile)
                            .load(imageUrl)
                            .into(userImage)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                }
            })

            // Fetch and set the user's name
            userRef.child("name").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val name: String? = dataSnapshot.value as? String
                    userName.text = name
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                }
            })
        }


//        fetchUsrProfile()

    }

//    private fun fetchUsrProfile() {
//        val userRef= database.reference.child("Users").child(usrProf)
//        userRef.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    val username= snapshot.child("name").value.toString()
//                    val profileImageUrl= snapshot.child("imageUrl").value.toString()
//
//
//                    Glide.with(applicationContext)
//                        .load(profileImageUrl)
//                        .placeholder(R.drawable.avatar)
//                        .error(R.drawable.avatar)
//                        .into(binding.userImage)
//
//                    binding.userName.text= username
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//    }
}

