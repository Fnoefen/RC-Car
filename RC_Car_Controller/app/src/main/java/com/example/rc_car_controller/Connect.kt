package com.example.rc_car_controller

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class Connect : AppCompatActivity() {

    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var m_pairedDevices: Set<BluetoothDevice>
    private var REQUEST_ENABLE_BLUETOOTH = 1

    private lateinit var testbutton: Button

    companion object {
        var EXTRA_ADRESS: String = "Device_address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)
        //canges top bar name
        supportActionBar?.hide()
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (m_bluetoothAdapter == null) {
            Toast.makeText(
                applicationContext,
                "This device dose not support Bluetooth",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (!m_bluetoothAdapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            )
                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        val refbutton: Button = findViewById(R.id.SelectDeviceRefresh)
        refbutton.setOnClickListener {
            pairedDeviceList()
        }
        pairedDeviceList()


    }

    private fun pairedDeviceList() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        )
            m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
        val list: ArrayList<BluetoothDevice> = ArrayList()

        if (m_pairedDevices.isNotEmpty()) {
            for (device: BluetoothDevice in m_pairedDevices) {
                list.add(device)
                Log.i("device", "" + device)
            }
        } else {
            Toast.makeText(
                applicationContext,
                "No paired Bluetooth devices found",
                Toast.LENGTH_SHORT
            ).show()
        }


        val listNames: ArrayList<String> = ArrayList()

        //for device name
        for (device: BluetoothDevice in list) {
            listNames.add(device.name)
        }
        val adapter = ArrayAdapter(this, R.layout.listview_item, listNames)

        val selectDeviceList: ListView = findViewById(R.id.SelectDeviceList)
        selectDeviceList.adapter = adapter

        selectDeviceList.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val device: BluetoothDevice = list[position]
                val address: String = device.address
                EXTRA_ADRESS = address

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

    }

    //  Toast logic - prompts
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (m_bluetoothAdapter!!.isEnabled) {
                    Toast.makeText(
                        applicationContext,
                        "Bluetooth has been enabled",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Bluetooth has been disabled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(
                    applicationContext,
                    "Bluetooth enabling has been canceled",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}