package com.example.rc_car_controller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.pow
import kotlin.math.sqrt

class Joystick(
    centerPositionX: Int,
    centerPositionY: Int,
    outerCircleRadiusI: Int,
    innerCircleRadiusI: Int,
    context: Context
) : SurfaceView(context), SurfaceHolder.Callback {

    private var outerCircleCenterX: Float = centerPositionX.toFloat()
    private var outerCircleCenterY: Float = centerPositionY.toFloat()
    private var innerCircleCenterX: Float = centerPositionX.toFloat()
    private var innerCircleCenterY: Float = centerPositionY.toFloat()

    private var outerCircleRadius: Float = outerCircleRadiusI.toFloat()
    private var innerCircleRadius: Float = innerCircleRadiusI.toFloat()

    private var innerCirclePaint: Paint = Paint()
    private var outerCirclePaint: Paint = Paint()
//    private var isPressed = false
    private var joystickCenterToTouchDistance = 0.0
    private var actuatorX = 0.0
    private var actuatorY = 0.0

    init {
        // paint of circles
        outerCirclePaint.color = Color.GRAY
        outerCirclePaint.style = Paint.Style.FILL_AND_STROKE

        innerCirclePaint.color = Color.BLUE
        innerCirclePaint.style = Paint.Style.FILL_AND_STROKE

        val surfaceHolder = holder
        surfaceHolder.addCallback(this)

    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas != null) {
            // Draw outer circle
            canvas.drawCircle(
                outerCircleCenterX,
                outerCircleCenterY,
                outerCircleRadius,
                outerCirclePaint
            )

            // Draw inner circle
            canvas.drawCircle(
                innerCircleCenterX,
                innerCircleCenterY,
                innerCircleRadius,
                innerCirclePaint
            )
        }
    }

    fun update() {
        updateInnerCirclePosition()
    }

    private fun updateInnerCirclePosition() {
        innerCircleCenterX = (outerCircleCenterX + actuatorX * outerCircleRadius).toFloat()
        innerCircleCenterY = (outerCircleCenterY + actuatorY * outerCircleRadius).toFloat()
    }

    fun setActuator(touchPositionX: Double, touchPositionY: Double) {
        val deltaX: Double = touchPositionX - outerCircleCenterX
        val deltaY: Double = touchPositionY - outerCircleCenterY
        val deltaDistance = sqrt(deltaX.pow(2.0) + deltaY.pow(2.0))
        if (deltaDistance < outerCircleRadius) {
            actuatorX = deltaX / outerCircleRadius
            actuatorY = deltaY / outerCircleRadius
        } else {
            actuatorX = deltaX / deltaDistance
            actuatorY = deltaY / deltaDistance
        }
    }

    fun isPressed(touchPositionX: Double, touchPositionY: Double): Boolean {
        joystickCenterToTouchDistance = sqrt(
            (outerCircleCenterX - touchPositionX).pow(2.0) +
                    (outerCircleCenterY - touchPositionY).pow(2.0)
        )
        return joystickCenterToTouchDistance < outerCircleRadius
    }

    fun getIsPressed(): Boolean {
        return isPressed
    }

    fun setIsPressed(isPressed: Boolean) {
        this.isPressed = isPressed
    }

    fun getActuatorX(): Double {
        return actuatorX
    }

    fun getActuatorY(): Double {
        return actuatorY
    }

    fun resetActuator() {
        actuatorX = 0.0
        actuatorY = 0.0
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        Log.d("Game.java", "surfaceCreated()")
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            val surfaceHolder = holder
            surfaceHolder.addCallback(this)
            gameLoop = GameLoop(this, surfaceHolder)
        }
        gameLoop.startLoop()

        while(true) {

            // Try to update and render game
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    game.update();
                    updateCount++;

                    game.draw(canvas);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        Log.d("Controller.kotlin", "surfaceChanged()")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d("Controller.kotlin", "surfaceDestroyed()")
    }

}
