package com.example.dragon.sstrilateration.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.dragon.sstrilateration.R

class BluetoothBeacon : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bluetooth_beacon, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BluetoothBeacon()
    }

}
