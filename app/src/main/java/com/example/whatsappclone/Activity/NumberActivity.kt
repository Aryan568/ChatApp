package com.example.whatsappclone.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsappclone.MainActivity
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.ActivityNumberBinding
import com.google.firebase.auth.FirebaseAuth

class NumberActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNumberBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null){
            startActivity(Intent(this@NumberActivity , MainActivity::class.java))
            finish()
        }

       binding.BtnContinue.setOnClickListener {

           if (binding.phoneNumber.text!!.isEmpty()){
               Toast.makeText(this@NumberActivity, "Please enter your phone number", Toast.LENGTH_SHORT).show()

           } else {
               val intent = Intent(this@NumberActivity, OtpActivity::class.java)
               intent.putExtra("number", binding.phoneNumber.text!!.toString())
               startActivity(intent)
           }
       }

    }
}