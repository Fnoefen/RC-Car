package com.example.rc_car_controller

import android.content.AttributionSource
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class Joystick(context: Context, attributeSet: AttributeSet? = null, style: Int? = null) :
    SurfaceView(context), SurfaceHolder.Callback, View.OnTouchListener {


    private lateinit var joystickCallback: JoystickListener
    private var centerY: Float = (width / 2).toFloat()
    private var centerX: Float = (height / 2).toFloat()
    private var baseRadius: Float = (min(width, height) / 3).toFloat()
    private var hatRadius: Float = (min(width, height) / 5).toFloat()

    init {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener){
            joystickCallback = context
        }
    }

    private fun drawJoystick(newX: Float, newY: Float) {
        if (holder.surface.isValid) {
            var myCanvas: Canvas = this.holder.lockCanvas()
            var colors: Paint = Paint()
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            colors.setARGB(255, 50, 50, 50)
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors)
            colors.setARGB(255, 200, 0, 0)
            myCanvas.drawCircle(newX, newY, hatRadius, colors)
            holder.unlockCanvasAndPost(myCanvas)
        }
    }

    private fun setupDimensions() {
        centerX = (width / 2).toFloat()
        centerY = (height / 1.5).toFloat()
        baseRadius = (min(width, height) / 4).toFloat()
        hatRadius = (min(width, height) / 8).toFloat()
    }

    override fun onTouch(v: View?, e: MotionEvent?): Boolean {
        if (v == this) {
            if (e!!.action != ACTION_UP) {
                val displacement: Float = sqrt((e.x - centerX).toDouble().pow(2.0) + (e.y - centerY).toDouble().pow(2.0)).toFloat()
                if (displacement < baseRadius) {
                    drawJoystick(e.x, e.y)
                    joystickCallback.onJoystickMoved((e.x - centerX) / baseRadius, (e.y - centerY)/ baseRadius, id)
                } else {
                    val ratio: Float = baseRadius / displacement
                    val constrainedX: Float = centerX + (e.x - centerX) * ratio
                    val constrainedY: Float = centerY + (e.y - centerY) * ratio
                    drawJoystick(constrainedX, constrainedY)
                    joystickCallback.onJoystickMoved((constrainedX - centerX) / baseRadius, (constrainedY - centerY)/ baseRadius, id)
                }
            } else {
                drawJoystick(centerX, centerY)
                joystickCallback.onJoystickMoved(0F, 0F,id)
            }
        }
        return true
    }

    interface JoystickListener{
        fun onJoystickMoved(xPercent: Float, yPercent: Float, source: Int)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        setupDimensions()
        drawJoystick(centerX, centerY)
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        Log.d("Controller.kotlin", "surfaceChanged()")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d("Controller.kotlin", "surfaceDestroyed()")
    }


//    override fun draw(canvas: Canvas?) {
//        super.draw(canvas)
//        if (canvas != null) {
//            // Draw outer circle
//            canvas.drawCircle(
//                outerCircleCenterX,
//                outerCircleCenterY,
//                outerCircleRadius,
//                outerCirclePaint
//            )
//
//            // Draw inner circle
//            canvas.drawCircle(
//                innerCircleCenterX,
//                innerCircleCenterY,
//                innerCircleRadius,
//                innerCirclePaint
//            )
//        }
//    }
//
//    fun update() {
//        updateInnerCirclePosition()
//    }
//
//    private fun updateInnerCirclePosition() {
//        innerCircleCenterX = (outerCircleCenterX + actuatorX * outerCircleRadius).toFloat()
//        innerCircleCenterY = (outerCircleCenterY + actuatorY * outerCircleRadius).toFloat()
//    }

//    fun setActuator(touchPositionX: Double, touchPositionY: Double) {
//        val deltaX: Double = touchPositionX - outerCircleCenterX
//        val deltaY: Double = touchPositionY - outerCircleCenterY
//        val deltaDistance = sqrt(deltaX.pow(2.0) + deltaY.pow(2.0))
//        if (deltaDistance < outerCircleRadius) {
//            actuatorX = deltaX / outerCircleRadius
//            actuatorY = deltaY / outerCircleRadius
//        } else {
//            actuatorX = deltaX / deltaDistance
//            actuatorY = deltaY / deltaDistance
//        }
//    }
//
//    fun isPressed(touchPositionX: Double, touchPositionY: Double): Boolean {
//        joystickCenterToTouchDistance = sqrt(
//            (outerCircleCenterX - touchPositionX).pow(2.0) +
//                    (outerCircleCenterY - touchPositionY).pow(2.0)
//        )
//        return joystickCenterToTouchDistance < outerCircleRadius
//    }


}

