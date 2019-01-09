package com.example.dragon.sstrilateration

interface MainInteractionListener {
    fun getBluetoothService(lambda: ( BluetoothService) -> Unit)

}