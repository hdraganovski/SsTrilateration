package com.example.dragon.sstrilateration.fragment

import android.content.Context
import android.net.wifi.ScanResult
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.dragon.sstrilateration.MainInteractionListener

import com.example.dragon.sstrilateration.R
import com.example.dragon.sstrilateration.distance
import kotlinx.android.synthetic.main.fragment_wifi_scan.*

class WifiScanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var listener: MainInteractionListener? = null

    private val adapter = WifiRecyclerAdapter(mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wifi_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wifiScanFragment_recycler?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WifiScanFragment.adapter
        }

        wifiScanFragment_fab.setOnClickListener {
            scanWifi()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement MainInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun scanWifi() {
        listener?.getWifiScanResults {
            adapter.setScanResults(it)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            WifiScanFragment()
    }
}

private class WifiRecyclerAdapter(private val scanResults: MutableList<ScanResult>): RecyclerView.Adapter<WifiRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(vg: ViewGroup, position: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(vg.context)
            .inflate(R.layout.view_wifi_beacon, vg, false))

    override fun getItemCount(): Int = scanResults.size

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val item = scanResults[position]

//        vh.name.text = item.SSID

        val positions = item.SSID.removePrefix("https://").split(",")

        if(positions.size == 2 && positions[0].toDoubleOrNull() != null && positions[1].toDoubleOrNull() != null) {
            vh.v.setBackgroundColor(vh.v.context.getColor(R.color.wifi))
        }
        else {
            vh.v.setBackgroundColor(vh.v.context.getColor(R.color.error))
        }

        vh.frequency.text = "${item.frequency}"
        vh.estimatedDistance.text = "%.4f".format(item.distance())
    }

    fun setScanResults(res: List<ScanResult>) {
        scanResults.clear()
        scanResults.addAll(res)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.wifi_name)
        val frequency: TextView = v.findViewById(R.id.wifi_frequency)
        val estimatedDistance: TextView = v.findViewById(R.id.wifi_estimatedDistance)
    }
}