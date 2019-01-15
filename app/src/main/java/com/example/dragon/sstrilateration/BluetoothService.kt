package com.example.dragon.sstrilateration

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import org.altbeacon.beacon.*
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor


class BluetoothService : Service(), BeaconConsumer {

    private val binder = LocalBinder()
    private var beaconManager: BeaconManager? = null

    private var lastBeacons: Collection<Beacon> = listOf()

    private val changeListeners = mutableListOf<ChangeListener>()

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        beaconManager = BeaconManager.getInstanceForApplication(this)

        beaconManager?.beaconParsers?.add(BeaconParser().apply {
            setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT)
        })

        beaconManager?.bind(this)
    }

    override fun onBeaconServiceConnect() {
        beaconManager?.addRangeNotifier { beacons, _ ->
            beacons.forEach { beacon ->
                val url = UrlBeaconUrlCompressor.uncompress(beacon.id1.toByteArray())
                Log.v(
                    "Beacon", "I see a beacon transmitting a url: " + url +
                            " approximately " + beacon.distance + " meters away."
                )
            }

            lastBeacons = beacons
            changeListeners.forEach {
                it.onChange(beacons)
            }


        }

        beaconManager?.addMonitorNotifier(object : MonitorNotifier {
            override fun didDetermineStateForRegion(state: Int, p1: Region?) {
                Log.i("Range", "I have just switched from seeing/not seeing beacons: $state")
            }

            override fun didEnterRegion(p0: Region?) {
                Log.i("Range", "I just saw an beacon for the first time!")
            }

            override fun didExitRegion(p0: Region?) {
                Log.i("Range", "I no longer see an beacon")
            }

        })

        beaconManager?.startRangingBeaconsInRegion(Region("w", null, null, null))
        beaconManager?.startMonitoringBeaconsInRegion(Region("w", null, null, null))
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconManager?.unbind(this)
    }

    fun addUpdateListener(cl: ChangeListener) {
        changeListeners.add(cl)
        cl.onChange(lastBeacons)
    }

    fun removeUpdateListener(cl: ChangeListener) {
        changeListeners.remove(cl)
    }

    inner class LocalBinder : Binder() {
        fun getService(): BluetoothService = this@BluetoothService
    }

    interface ChangeListener {
        fun onChange(beacons: Collection<Beacon>)
    }
}

