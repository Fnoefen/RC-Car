package com.example.rc_car_controller

import android.content.Context
import android.graphics.Canvas
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.SurfaceView


class Controller(context: Context): SurfaceView(context), SurfaceHolder.Callback {

    var joystick: Joystick = Joystick(300, 300, 70, 40)
    lateinit var angelCtrl : SeekBar
    lateinit var angel : TextView

    lateinit var speedCtrl : SeekBar
    lateinit var speed : TextView

<<<<<<< Updated upstream
    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)
=======
    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_controller)
//
//
//        angelCtrl = findViewById<SeekBar>(R.id.AngelCtrl)
//        angel = findViewById<TextView>(R.id.Angel)
//
//
//
//        angelCtrl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                angel.text = p1.toString()
//            }
//            override fun onStartTrackingTouch(p0: SeekBar?) {}
//            override fun onStopTrackingTouch(p0: SeekBar?) {
//                angelCtrl.progress = 0
//            }
//        })
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            speedCtrl = findViewById<SeekBar>(R.id.SpeedCtrl)
//            speed = findViewById<TextView>(R.id.Speed)
//
//            speedCtrl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    speed.text = p1.toString()
//                }
//                override fun onStartTrackingTouch(p0: SeekBar?) {}
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//                    speedCtrl.progress = 0
//                }
//            })
//        }
//
//
//    }
>>>>>>> Stashed changes

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas != null) {
            joystick.draw(canvas)
        }
    }

    fun update(){
        joystick.update()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event != null) {
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    if (joystick.isPressed(event.x.toDouble(), event.y.toDouble())) {
                        joystick.setIsPressed(true)
                    }
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (joystick.getIsPressed()){
                        joystick.setActuator(event.x.toDouble(), event.y.toDouble())
                    }
                    return true
                }
                MotionEvent.ACTION_UP ->{
                    joystick.setIsPressed(false)
                    joystick.resetActuator()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)

    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        TODO("Not yet implemented")
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        Log.d("Controller.java", "surfaceChanged()")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d("Controller.java", "surfaceDestroyed()")
    }

}