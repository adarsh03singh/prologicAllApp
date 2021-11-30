package com.prologicwebsolution.blutoothexample

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
class SelectDeviceActivity : AppCompatActivity() {

    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var m_pairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1

    lateinit var select_device_list:ListView

    companion object {
        val EXTRA_ADDRESS: String = "Device_address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_device)

        val select_device_refresh = findViewById<Button>(R.id.select_device_refresh)
         select_device_list = findViewById<ListView>(R.id.select_device_list)

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter == null) {
            showToast("this device doesn't support bluetooth")
            return
        }
        if(!m_bluetoothAdapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        select_device_refresh.setOnClickListener{ pairedDeviceList() }

    }

    private fun pairedDeviceList() {
        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
        val list : ArrayList<BluetoothDevice> = ArrayList()

        if (!m_pairedDevices.isEmpty()) {
            for (device: BluetoothDevice in m_pairedDevices) {
                list.add(device)
                Log.i("device", ""+device)
            }
        } else {
            showToast("no paired bluetooth devices found")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        select_device_list.adapter = adapter
        select_device_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val address: String = device.address

            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (m_bluetoothAdapter!!.isEnabled) {
                    showToast("Bluetooth has been enabled")
                } else {
                    showToast("Bluetooth has been disabled")
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showToast("Bluetooth enabling has been canceled")
            }
        }
    }
    fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
