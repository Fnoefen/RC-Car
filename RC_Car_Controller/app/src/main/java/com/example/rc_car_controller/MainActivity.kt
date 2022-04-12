package com.example.rc_car_controller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

     lateinit var contollerBtn : Button
     lateinit var connectBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contollerBtn = findViewById<Button>(R.id.ControllerBtn)
        connectBtn = findViewById<Button>(R.id.ConnectBtn)

        contollerBtn.setOnClickListener {
            val intent = Intent(this, Controller::class.java)
            startActivity(intent)
        }

        connectBtn.setOnClickListener {
            val intent = Intent(this, Connect::class.java)
            startActivity(intent)
        }
    }
}