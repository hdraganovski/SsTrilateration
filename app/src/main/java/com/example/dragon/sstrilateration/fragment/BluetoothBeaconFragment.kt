package com.example.dragon.sstrilateration.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dragon.sstrilateration.MainInteractionListener
import com.example.dragon.sstrilateration.R
import com.uriio.beacons.Beacons
import com.uriio.beacons.model.EddystoneURL
import kotlinx.android.synthetic.main.fragment_bluetooth_beacon.*

class BluetoothBeaconFragment : Fragment() {

    private var mainInteractionListener: MainInteractionListener? = null

    private var activeBeacon: EddystoneURL? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bluetooth_beacon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Beacons.getActive().also { beacons ->
            if(beacons.size > 0) {
                (beacons[0] as? EddystoneURL)?.also {
                    bBeaconFragment_url.setText(it.url)

                    bBeaconFragment_url.isEnabled = false

                    bBeaconFragment_toggleButton.isChecked = true

                    activeBeacon = it
                }
            }
        }

        bBeaconFragment_toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if(!isChecked) {
                activeBeacon?.stop()
                activeBeacon?.delete()
                activeBeacon = null

                bBeaconFragment_url.isEnabled = true
            }
            else {
                val url = "https://${bBeaconFragment_url.text}"

                EddystoneURL(url).apply {
                        start()
                        activeBeacon = this

                        bBeaconFragment_url.isEnabled = false
                    }
                Log.d("Beacon", "Started with url: $url")
            }
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainInteractionListener = context as? MainInteractionListener
    }

    override fun onDetach() {
        super.onDetach()
        mainInteractionListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BluetoothBeaconFragment()
    }

}
