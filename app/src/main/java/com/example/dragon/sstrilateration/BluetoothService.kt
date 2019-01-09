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
        beaconManager?.addRangeNotifier { beacons, region ->
            beacons.forEach {
                for (beacon in beacons) {
                    if (beacon.serviceUuid == 0xfeaa && beacon.beaconTypeCode == 0x10) {
                        // This is a Eddystone-URL frame
                        val url = UrlBeaconUrlCompressor.uncompress(beacon.id1.toByteArray())
                        Log.d(
                            "Beacon", "I see a beacon transmitting a url: " + url +
                                    " approximately " + beacon.distance + " meters away."
                        )
                    }
                }
            }

            lastBeacons = beacons
            changeListeners.forEach {
                it.onChange(beacons)
            }
        }

        beaconManager?.addMonitorNotifier(object: MonitorNotifier {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconManager?.unbind(this)
    }

    fun addUpdateListener(cl: ChangeListener) {
        changeListeners.add(cl)
        cl.onChange(lastBeacons)
    }

    inner class LocalBinder: Binder() {
        fun getService(): BluetoothService = this@BluetoothService
    }

    interface ChangeListener {
        fun onChange(beacons: Collection<Beacon>)
    }
}

