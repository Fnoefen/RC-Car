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
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.*

class Controller : AppCompatActivity() {

    lateinit var angelCtrl : SeekBar
    lateinit var angel : TextView

    lateinit var speedCtrl : SeekBar
    lateinit var speed : TextView

    lateinit var testBTN : Button




    companion object {
        var m_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        var m_address: String ="some address"

        //  Values to send over bluetooth ---
        var mSpeed : String = " "
        var mAngel : String = " "
        //  -----
        var testvar :Boolean = false;

        fun sendCommand(input: String){
            if(m_bluetoothSocket != null){
                try{
                    m_bluetoothSocket!!.outputStream.write(input.toByteArray())
                } catch(e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        m_address = Connect.EXTRA_ADRESS
        if(m_address == "Device_address" ){
            Toast.makeText(applicationContext, "No device connected", Toast.LENGTH_SHORT).show()
        }else{
            ConnectToDevice(this).execute()
        }

        angelCtrl = findViewById<SeekBar>(R.id.AngelCtrl)
        angel = findViewById<TextView>(R.id.Angel)

        angelCtrl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                angel.text = p1.toString()
                mAngel = p1.toString()
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
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            testBTN = findViewById<Button>(R.id.TestBTN)
            testBTN.setOnClickListener { disconnect() }

        }

        val t1 = thread()
        t1.start()
    }

    //  Thread ------------------------------------------------------------
    class thread() :Thread() {
        var prevSpeed : String = " "
        var prevAngel : String = " "

        override fun run() {
            try {
                while(!m_isConnected){
                    if(mSpeed != prevSpeed){
                        sendCommand(mSpeed)
                        prevSpeed = mSpeed
                    }
                    if(mAngel != prevAngel){
                        sendCommand(mAngel)
                        prevAngel = mAngel
                    }
                }
                while(m_isConnected){
                    if(mSpeed != prevSpeed){
                        sendCommand(mSpeed)
                        prevSpeed = mSpeed
                    }
                    if(mAngel != prevAngel){
                        sendCommand(mAngel)
                        prevAngel = mAngel
                    }
                }
            }catch (ex:Exception){
                print(ex.message)
            }
        }



    }

    private fun disconnect(){
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




    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>(){
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
                    if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_UUID)

                    if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()

                    if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
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