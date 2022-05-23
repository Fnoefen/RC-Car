package com.example.rc_car_controller

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class JoystickView : SurfaceView, SurfaceHolder.Callback, OnTouchListener {
    private var centerX = 0f
    private var centerY = 0f
    private var baseRadius = 0f
    private var hatRadius = 0f
    private var joystickCallback: JoystickListener? = null
    private val ratio = 1 //The smaller, the more shading will occur

    // Default constructor for the joystick with no added specifications
    constructor(context: Context?) : super(context) {
        holder.addCallback(this)
        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSPARENT)
        setOnTouchListener(this)
        if (context is JoystickListener) joystickCallback = context
    }

    // Default constructor for the joystick with added attributes and styles
    constructor(context: Context?, attributes: AttributeSet?, style: Int) : super(context, attributes, style) {
        holder.addCallback(this)
        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSPARENT)
        setOnTouchListener(this)
        if (context is JoystickListener) joystickCallback = context
    }

    // Default constructor for the joystick with added attributes
    constructor(context: Context?, attributes: AttributeSet?) : super(context, attributes) {
        holder.addCallback(this)
        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSPARENT)
        setOnTouchListener(this)
        if (context is JoystickListener) joystickCallback = context
    }

    private fun drawJoystick(newX: Float, newY: Float) {
        if (holder.surface.isValid) {

            val myCanvas: Canvas = this.holder.lockCanvas()
            val colors = Paint()
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

            val hypotenuse: Float = sqrt((newX - centerX).toDouble().pow(2.0) + (newY - centerY).toDouble().pow(2.0)).toFloat()
            val sin: Float = (newY - centerY) / hypotenuse
            val cos: Float = (newX - centerX) / hypotenuse

            colors.setARGB(255, 161,154,132)
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors)
            for (i in 1..((baseRadius / ratio).toInt())) {
                colors.setARGB(255+i, 255,248,226)
                myCanvas.drawCircle(newX - cos * hypotenuse * (ratio/baseRadius) * i,newY - sin * hypotenuse * (ratio/baseRadius) * i, i * (hatRadius * ratio / baseRadius), colors)
            }

            for (i in 1..((hatRadius / ratio).toInt())) {
                colors.setARGB(255, 255,
                    (i * (116 * ratio / hatRadius)).toInt(), (i * (94 * ratio / hatRadius)).toInt())
                myCanvas.drawCircle(newX, newY, hatRadius - i * ratio / 2, colors)
            }

            holder.unlockCanvasAndPost(myCanvas)
        }
    }

    private fun setupDimensions() {
        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()
        baseRadius = (min(width, height) / 4).toFloat()
        hatRadius = (min(width, height) / 6).toFloat()
    }

    override fun onTouch(v: View?, e: MotionEvent?): Boolean {
        if (v == this) {
            if (e!!.action != ACTION_UP) {
                val displacement: Float = sqrt(
                    (e.x - centerX).toDouble().pow(2.0) + (e.y - centerY).toDouble().pow(2.0)
                ).toFloat()
                if (displacement < baseRadius) {
                    drawJoystick(e.x, e.y)
                    joystickCallback?.onJoystickMoved(
                        (e.x - centerX) / baseRadius,
                        (e.y - centerY) / baseRadius,
                        id
                    )
                } else {
                    val ratio: Float = baseRadius / displacement
                    val constrainedX: Float = centerX + (e.x - centerX) * ratio
                    val constrainedY: Float = centerY + (e.y - centerY) * ratio
                    drawJoystick(constrainedX, constrainedY)
                    joystickCallback?.onJoystickMoved(
                        (constrainedX - centerX) / baseRadius,
                        (constrainedY - centerY) / baseRadius,
                        id
                    )
                }
            } else {
                drawJoystick(centerX, centerY)
                joystickCallback?.onJoystickMoved(0F, 0F, id)
            }
        }
        return true
    }

    interface JoystickListener { fun onJoystickMoved(xPercent: Float, yPercent: Float, source: Int) }

    override fun surfaceCreated(p0: SurfaceHolder) {
        setupDimensions()
        drawJoystick(centerX, centerY)
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: SurfaceHolder) {}

}


