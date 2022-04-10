package com.example.rc_car_controller

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    lateinit var angelCtrl : SeekBar
    lateinit var angel : TextView

    lateinit var speedCtrl : SeekBar
    lateinit var speed : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        angelCtrl = findViewById<SeekBar>(R.id.AngelCtrl)
        angel = findViewById<TextView>(R.id.Angel)

        angelCtrl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                angel.text = p1.toString()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {
                angelCtrl.progress = 0
            }
        })
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            speedCtrl = findViewById<SeekBar>(R.id.SpeedCtrl)
            speed = findViewById<TextView>(R.id.Speed)

            speedCtrl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    speed.text = p1.toString()
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {
                    speedCtrl.progress = 0
                }
            })
        }


    }
}