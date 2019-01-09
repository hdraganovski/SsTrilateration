package com.example.dragon.sstrilateration.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dragon.sstrilateration.BluetoothService
import com.example.dragon.sstrilateration.MainInteractionListener
import com.example.dragon.sstrilateration.R
import kotlinx.android.synthetic.main.fragment_bluetooth_scanner.*
import org.altbeacon.beacon.Beacon

class BluetoothScannerFragment : Fragment(), BluetoothService.ChangeListener {


    private var mainInteractionListener: MainInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bluetooth_scanner, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainInteractionListener = context as? MainInteractionListener
    }

    override fun onDetach() {
        super.onDetach()
        mainInteractionListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainInteractionListener?.getBluetoothService {
            it.addUpdateListener(this)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onChange(beacons: Collection<Beacon>) {
        bluetoothScannerFragment_textView.text = "Beacons: ${beacons.size}"
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BluetoothScannerFragment()
    }
}
