package com.example.dragon.sstrilateration

import android.net.wifi.ScanResult

interface MainInteractionListener {
    fun getBluetoothService(lambda: ( BluetoothService) -> Unit)

    fun getWifiScanResults(lambda: (List<ScanResult>) -> Unit)
}