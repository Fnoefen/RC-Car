package com.example.rc_car_controller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

     private lateinit var controllerBtn : Button
     private lateinit var connectBtn : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controllerBtn = findViewById(R.id.ControllerBtn)
        connectBtn = findViewById(R.id.ConnectBtn)


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