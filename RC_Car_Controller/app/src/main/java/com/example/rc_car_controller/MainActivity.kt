package com.example.rc_car_controller

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {

     private lateinit var controllerBtn : Button
     private lateinit var connectBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        controllerBtn = findViewById(R.id.ControllerBtn)
        connectBtn = findViewById(R.id.ConnectBtn)

        var iconImg : ImageView = findViewById(R.id.IconImg)
        Glide.with(this).load("https://i.imgur.com/jC4ec16.png").into(iconImg)


        controllerBtn.setOnClickListener {
            val intent = Intent(this, Controller::class.java)
            startActivity(intent)
        }

        connectBtn.setOnClickListener {
            val intent = Intent(this, Connect::class.java)
            startActivity(intent)
        }
    }
}