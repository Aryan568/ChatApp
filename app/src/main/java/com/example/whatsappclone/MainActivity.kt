package com.example.whatsappclone

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.Activity.ChatActivity
import com.example.whatsappclone.Activity.NumberActivity
import com.example.whatsappclone.Activity.modal.UserModal
import com.example.whatsappclone.adapter.ViewPagerAdapter
import com.example.whatsappclone.databinding.ActivityMainBinding
import com.example.whatsappclone.ui.Call_Fragment
import com.example.whatsappclone.ui.Chat_Fragment
import com.example.whatsappclone.ui.Status_Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding?= null
    private  lateinit var auth: FirebaseAuth
    lateinit var database : FirebaseDatabase
    lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1);
            }
        }


        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null){
            startActivity(Intent(this@MainActivity  , NumberActivity::class.java))
            finish()
        }


        val fragmentArrayList = ArrayList<Fragment>()

        fragmentArrayList.add(Chat_Fragment())
        fragmentArrayList.add(Status_Fragment())
        fragmentArrayList.add(Call_Fragment())

        val adapter = ViewPagerAdapter(this@MainActivity ,supportFragmentManager, fragmentArrayList)

        binding!!.viewPager.adapter = adapter

        binding!!.tabs.setupWithViewPager(binding!!.viewPager)


        //  MainActivity.= DividerItemDecoration(this, RecyclerView.VERTICAL)

        getFCMToken()

        if (intent.extras != null) {
            val uid = intent.getStringExtra("uid")

            val userRef = database.getReference("Users").child(auth.uid.toString())

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val Modal = snapshot.getValue(UserModal::class.java)
                        if (Modal != null) {
                            val intent = Intent(context, ChatActivity::class.java)
                            intent.putExtra("uid", uid)
                            startActivity(intent)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error if needed
                }
            })
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d("MenuDebug", "onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.menus, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Profile -> {
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.Settings -> {
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.GroupChat -> {
                Toast.makeText(this, "GroupChat", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.Logout -> {
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                auth.signOut()
                val intent= Intent(this, NumberActivity::class.java)
                startActivity(intent)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}

fun getFCMToken() {
    FirebaseMessaging.getInstance().token
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Retrieve the FCM token from the task's result
                val token = task.result
                updateFCMToken(token)
            }
        }
}

fun updateFCMToken(token: String?) {
    if (token != null) {
        var database: FirebaseDatabase
        var auth : FirebaseAuth
        auth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()
        database.reference.child("Users").child(auth.uid.toString()).child("fcmToken").setValue(token)
            .addOnSuccessListener {
                println("FCM Token updated successfully")
            }
            .addOnFailureListener { e ->
                println("Failed to update FCM Token: ${e.message}")
            }
    }
}
