package com.example.whatsappclone.Activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.whatsappclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class SplashScreenActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var database : FirebaseDatabase
    lateinit var context: Context
    companion object {
        var isAppInForeground = false
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)

        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver())

            Handler(Looper.getMainLooper()).postDelayed({

                if (intent.getBooleanExtra("openChat",false)){
                    val intent = Intent(this@SplashScreenActivity, ChatActivity::class.java)
                    intent.putExtra("fcm",intent.getStringExtra("fcm"))
                    intent.putExtra("name",intent.getStringExtra("name"))
                    intent.putExtra("uid",intent.getStringExtra("uid"))
                    startActivity(intent)
                    finish()
                }else {
                    val intent = Intent(this@SplashScreenActivity, NumberActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            },3000)

    }

    internal class AppLifecycleObserver : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onForeground() {
            isAppInForeground = true
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onBackground() {
            isAppInForeground = false
        }
    }

}