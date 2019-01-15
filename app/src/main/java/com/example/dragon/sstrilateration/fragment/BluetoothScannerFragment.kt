package com.example.dragon.sstrilateration.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.dragon.sstrilateration.*
import kotlinx.android.synthetic.main.fragment_bluetooth_scanner.*
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor
import java.util.*


class BluetoothScannerFragment : Fragment(), BluetoothService.ChangeListener {


    private var mainInteractionListener: MainInteractionListener? = null
    private val btBeaconsAdapter = BluetoothDevicesRecyclerViewAdapter(mutableListOf())

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

        btScannerFragment_beaconsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = btBeaconsAdapter
        }

        btScannerFragment_scanButton.setOnClickListener { performScan() }
    }

    @SuppressLint("SetTextI18n")
    override fun onChange(beacons: Collection<Beacon>) {
        btBeaconsAdapter.updateBeaconsList(beacons)
    }

    fun performScan() {
        val beacons: List<Position> = btBeaconsAdapter.getBeaconList()
            .filter {
                it.serviceUuid == 0xfeaa && it.beaconTypeCode == 0x10
            }
            .map {
                var url = UrlBeaconUrlCompressor.uncompress(it.id1.toByteArray())
                url = url.removePrefix("http://")
                url = url.removePrefix("https://")

                val p = url.split(",")

                if (p.size >= 2) {
                    return@map Position(
                        Position.TAG_BLUETOOTH,
                        x = p[0].toDouble(),
                        y = p[1].toDouble(),
                        d = it.distance
                    )
                }

                return@map null
            }
            .filterNotNull()

        if (beacons.size >= 3) {
            val position = computePoint(beacons)
            setResult(position)
            Log.d("Computed", position.toString())
        } else {
            Snackbar
                .make(cardView, "Not enough valid beacons", Snackbar.LENGTH_SHORT)
                .show()

            Log.d("Computed", "Not enough valid beacons ${beacons.size}")
        }
    }

    @SuppressLint("SetTextI18n")
    fun setResult(position: Position?) {
        if (position == null) {
            btScanner_textView_no.visibility = View.GONE
            btScannerFragment_textView_position.visibility = View.GONE
            btScanner_textView_time.visibility = View.GONE
        } else {
            btScanner_textView_no.visibility = View.VISIBLE
            btScanner_textView_no.text = "${position.no}"

            btScannerFragment_textView_position.visibility = View.VISIBLE
            btScannerFragment_textView_position.text = "x: ${position.x}, y: ${position.y}"

            btScanner_textView_time.visibility = View.VISIBLE
            btScanner_textView_time.text = Calendar.getInstance().toString()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BluetoothScannerFragment()
    }

}

class BluetoothDevicesRecyclerViewAdapter(private val bluetoothBeacons: MutableList<Beacon>) :
    RecyclerView.Adapter<BluetoothDevicesRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_bluetooth_beacon, parent, false))


    override fun getItemCount(): Int =
        bluetoothBeacons.size


    override fun onBindViewHolder(vh: ViewHolder, i: Int) {
        val item = bluetoothBeacons[i]

        if (item.serviceUuid == 0xfeaa && item.beaconTypeCode == 0x10) {
            // This is a Eddystone-URL frame
            val url = UrlBeaconUrlCompressor.uncompress(item.id1.toByteArray())
            vh.name.text = url
        } else {
            vh.name.text = "Not an EddystoneURL Beacon"
        }

        vh.distance.text = "%.2f".format(item.distance)
    }

    fun updateBeaconsList(beacons: Collection<Beacon>) {
        bluetoothBeacons.clear()
        bluetoothBeacons.addAll(beacons)
        notifyDataSetChanged()
    }

    fun getBeaconList(): List<Beacon> {
        return bluetoothBeacons
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name = v.findViewById<TextView>(R.id.nameOfBeacon)
        val distance = v.findViewById<TextView>(R.id.distanceFromBeacon)
    }
}