package com.example.rc_car_controller

import android.Manifest
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException
import java.util.*
import kotlin.math.*


class Controller : AppCompatActivity(), JoystickView.JoystickListener {

    lateinit var angleCtrl: SeekBar
    private lateinit var angle: TextView

    lateinit var speedCtrl: SeekBar
    lateinit var speed: TextView

    lateinit var discornect: FloatingActionButton


    companion object {
        var m_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        var m_address: String = "some address"

        //  Values to send over bluetooth ---
        var mSpeed: String = " "
        var mAngle: String = " "

        fun sendCommand(input: String) {
            if (m_bluetoothSocket != null) {
                try {
                    m_bluetoothSocket!!.outputStream.write(input.toByteArray())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        // checks address has been selected from connect
        m_address = Connect.EXTRA_ADRESS
        if (m_address == "Device_address") {
            Toast.makeText(applicationContext, "No device connected", Toast.LENGTH_SHORT).show()
        } else {
            ConnectToDevice(this).execute()
            val t1 = thread()
            t1.start()
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            angleCtrl = findViewById<SeekBar>(R.id.AngelCtrl)
            angleCtrl.thumb.mutate().alpha = 0;
            angleCtrl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    mAngle = p1.toString()
                    setAngle(mAngle)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {
                    angleCtrl.progress = 0
                }
            })

            speedCtrl = findViewById<SeekBar>(R.id.SpeedCtrl)
            speedCtrl.thumb.mutate().alpha = 0;
            speedCtrl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    mSpeed = p1.toString()
                    setSpeed(mSpeed)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {
                    speedCtrl.progress = 0
                }
            })
        }
    }

    override fun onJoystickMoved(xPercent: Float, yPercent: Float, id: Int) {
        Log.d("joystick", "X percent: " + xPercent + " Y percent: " + yPercent)
        var speedVal: Int = (sqrt(yPercent.pow(2) + xPercent.pow(2)) * 100).toInt()
        if (yPercent > 0)
            speedVal = -abs(speedVal)
        var angleVal: Int = 0
        angleVal = if (xPercent > 0 && xPercent != 0F)
            90 - ((atan(abs(yPercent) / xPercent) * 180) / PI).roundToInt()
        else if (xPercent != 0F)
            ((atan(abs(yPercent) / abs(xPercent)) * 180) / PI).roundToInt() - 90
        else
            0
        mSpeed = speedVal.toString()
        mAngle = angleVal.toString()
        setAngle(mAngle)
        setSpeed(mSpeed)
    }


    private fun setAngle(a: String) {
        angle = findViewById<TextView>(R.id.angle)
        angle.text = getString(R.string.angle) + " " + a
    }

    private fun setSpeed(s: String) {
        speed = findViewById<TextView>(R.id.speed)
        speed.text = getString(R.string.speed) + " " + s
    }


    //  Thread ------------------------------------------------------------
    class thread() : Thread() {
        var prevSpeed: String = " "
        var prevAngle: String = " "

        override fun run() {
            try {
                while (m_isConnected) {
                    if (mSpeed != prevSpeed) {
                        sendCommand(mSpeed)
                        prevSpeed = mSpeed
                    }
                    if (mAngle != prevAngle) {
                        sendCommand(mAngle)
                        prevAngle = mAngle
                    }
                }
            } catch (ex: Exception) {
                print(ex.message)
            }
        }
    }


    private fun disconnect() {
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting..", "please wait")
        }


        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (m_bluetoothSocket == null || !m_isConnected) {
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(m_address)
                    if (ActivityCompat.checkSelfPermission(
                            this.context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    )
                        m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_UUID)

                    if (ActivityCompat.checkSelfPermission(
                            this.context,
                            Manifest.permission.BLUETOOTH_SCAN
                        ) != PackageManager.PERMISSION_GRANTED
                    )
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery()

                    if (ActivityCompat.checkSelfPermission(
                            this.context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    )
                        m_bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
            } else {
                m_isConnected = true
            }
            m_progress.dismiss()
        }
    }

}