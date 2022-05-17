package com.example.rc_car_controller

import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class  Controller : AppCompatActivity(){

    private var joystick = Joystick(300, 300, 70, 40, this)
    lateinit var angelCtrl: SeekBar
    lateinit var angel: TextView

    lateinit var speedCtrl: SeekBar
    lateinit var speed: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        // Joystick
        setContentView(joystick)


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

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event != null) when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (joystick.isPressed(event.x.toDouble(), event.y.toDouble())) {
                    joystick.setIsPressed(true)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (joystick.getIsPressed()) {
                    joystick.setActuator(event.x.toDouble(), event.y.toDouble())
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                joystick.setIsPressed(false)
                joystick.resetActuator()
                return true
            }
        }
        return super.onTouchEvent(event)

    }




}