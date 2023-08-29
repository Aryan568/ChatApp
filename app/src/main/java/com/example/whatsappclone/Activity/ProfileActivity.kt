package com.example.whatsappclone.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.whatsappclone.Activity.modal.UserModal
import com.example.whatsappclone.MainActivity
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthSettings
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ProfileActivity : AppCompatActivity() {

    lateinit var binding : ActivityProfileBinding
    lateinit var auth : FirebaseAuth
    lateinit var database : FirebaseDatabase
    lateinit var storage : FirebaseStorage
    lateinit var selectedImg : Uri
    lateinit var dialog: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = AlertDialog.Builder(this)
            .setMessage("Uploading Profile .....")
            .setCancelable(false)


        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.userImage.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"

            startActivityForResult(intent,1)


        }

        binding.ContinueBtn.setOnClickListener {

            if (binding.userName.text!!.isEmpty()){
                Toast.makeText(this, "Please Enter your name", Toast.LENGTH_LONG).show()

            } else if (selectedImg == null){
                Toast.makeText(this, " Please Select your Image", Toast.LENGTH_LONG).show()

            }  else uploadData()

        }


    }

    private fun uploadData() {


        val reference = storage.reference.child("Profile ")
            .child(Date().time.toString())
        reference.putFile(selectedImg).addOnCompleteListener {
           if (it.isSuccessful){
               reference.downloadUrl.addOnSuccessListener {   task ->

                   uploadInfo(task.toString())

               }
           }
        }

    }

    private fun uploadInfo(imgUrl: String) {

   val user = UserModal(auth.uid.toString(), binding.userName.text.toString()!!
     ,auth.currentUser!!.phoneNumber.toString(),imgUrl)

        database.reference.child("Users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnCompleteListener {

                Toast.makeText(this, "Data Inserted" , Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {

            if (data.data != null){

                selectedImg = data.data!!

                binding.userImage.setImageURI(selectedImg)
            }
        }
    }



}