package com.kyawhtut.pos.ui.devicedialog

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.kyawhtut.pos.R
import com.kyawhtut.pos.utils.gone
import com.kyawhtut.pos.utils.visible
import kotlinx.android.synthetic.main.dialog_device_list.*

/**
 * @author kyawhtut
 * @date 04/05/2020
 */

class DeviceListDialog : DialogFragment(R.layout.dialog_device_list) {

    private val mBtAdapter: BluetoothAdapter by lazy { BluetoothAdapter.getDefaultAdapter() }
    private val pairedDevicesArrayAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1
        )
    }
    private val newDevicesArrayAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1
        )
    }

    private var delegate: (String) -> Unit = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paired_devices.adapter = pairedDevicesArrayAdapter
        paired_devices.onItemClickListener = mDeviceClickListener

        new_devices.adapter = newDevicesArrayAdapter
        new_devices.onItemClickListener = mDeviceClickListener

        // Register for broadcasts when a device is discovered
        var filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        requireActivity().registerReceiver(receiver, filter)

        // Register for broadcasts when discovery has finished
        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        requireActivity().registerReceiver(receiver, filter)

        mBtAdapter.bondedDevices.map {
            pairedDevicesArrayAdapter.add("%s\n%s".format(it.name, it.address))
            "%s\n%s".format(it.name, it.address)
        }.run {
            if (isEmpty()) {
                pairedDevicesArrayAdapter.add("No devices not found.")
            }
        }

        button_scan.setOnClickListener {
            doDiscovery()
            it.gone()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBtAdapter.cancelDiscovery()
        requireActivity().unregisterReceiver(receiver)
    }

    fun setDelegate(delegate: (String) -> Unit) {
        this.delegate = delegate
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private fun doDiscovery() {
        progress_view.visible()
        newDevicesArrayAdapter.clear()

        if (mBtAdapter.isDiscovering) {
            mBtAdapter.cancelDiscovery()
        }
        
        mBtAdapter.startDiscovery()
    }

    // The on-click listener for all devices in the ListViews
    private val mDeviceClickListener = AdapterView.OnItemClickListener { _, v, _, _ ->
        // Cancel discovery because it's costly and we're about to connect
        mBtAdapter.cancelDiscovery()

        // Get the device MAC address, which is the last 17 chars in the View
        val info = (v as TextView).text.toString()
        if (info == "No devices not found.") return@OnItemClickListener
        val address = info.substring(info.length - 17)

        delegate(address)
        dismiss()
    }

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Get the BluetoothDevice object from the Intent
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                // If it's already paired, skip it, because it's been listed already
                device?.let {
                    if (it.bondState != BluetoothDevice.BOND_BONDED) {
                        newDevicesArrayAdapter.add("%s\n%s".format(it.name, it.address))
                    }
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                progress_view.gone()
                if (newDevicesArrayAdapter.isEmpty) {
                    newDevicesArrayAdapter.add("No devices not found.")
                }
            }
        }
    }
}
