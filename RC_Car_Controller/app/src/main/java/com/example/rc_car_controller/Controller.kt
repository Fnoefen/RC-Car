package com.example.rc_car_controller

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class Controller : AppCompatActivity(), JoystickView.JoystickListener {

    lateinit var angelCtrl: SeekBar
    lateinit var angel: TextView

    lateinit var speedCtrl: SeekBar
    lateinit var speed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        // Landscape controller
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            angelCtrl = findViewById<SeekBar>(R.id.AngelCtrl)
            angel = findViewById<TextView>(R.id.Angel)

            angelCtrl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    angel.text = p1.toString()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {
                    angelCtrl.progress = 0
                }
            })
            speedCtrl = findViewById<SeekBar>(R.id.SpeedCtrl)
            speed = findViewById<TextView>(R.id.Speed)

            speedCtrl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

    override fun onJoystickMoved(xPercent: Float, yPercent: Float, id: Int) {
        when(id){
            R.id.joystickViewUp ->{
                Log.d("Right joystick", "X percent: " + xPercent + "Y percent: " + yPercent)
                angel = findViewById<TextView>(R.id.angle)
                speed = findViewById<TextView>(R.id.speed)
                angel.text = yPercent.toString()
                speed.text = xPercent.toString()
            }
            R.id.joystickView3 ->{
                Log.d("Left joystick", "X percent: " + xPercent + "Y percent: " + yPercent)
            }
        }

    }

}