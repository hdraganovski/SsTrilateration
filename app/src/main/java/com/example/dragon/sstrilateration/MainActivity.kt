package com.example.dragon.sstrilateration

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.example.dragon.sstrilateration.fragment.TabContentFragment
import com.uriio.beacons.Beacons
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainInteractionListener {
    private val adapter = MainViewPagerAdapter(this.supportFragmentManager)

    private var bluetoothService: BluetoothService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpUI()


        setUpBluetooth()

        if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 10)
        }
        else {

        }

    }

    private fun setUpUI() {
        main_vp.adapter = adapter

        main_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item_bluetooth -> main_vp.setCurrentItem(MainViewPagerAdapter.BLUETOOTH, true)
                R.id.menu_item_wifi -> main_vp.setCurrentItem(MainViewPagerAdapter.WIFI, true)
                R.id.menu_item_both -> main_vp.setCurrentItem(MainViewPagerAdapter.BOTH, true)
            }
            true
        }

        main_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
                /* nothing */
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                /* nothing */
            }

            override fun onPageSelected(i: Int) {
                main_nav.selectedItemId = when (i) {
                    MainViewPagerAdapter.BLUETOOTH -> R.id.menu_item_bluetooth
                    MainViewPagerAdapter.WIFI -> R.id.menu_item_wifi
                    MainViewPagerAdapter.BOTH -> R.id.menu_item_both
                    else -> throw IndexOutOfBoundsException(i.toString())
                }
            }

        })
    }

    private fun setUpBluetooth() {
        setUpNotificationChannels()
        Beacons.initialize(this)
    }

    private fun setUpNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.bluetooth_notification_channel)
            val descriptionText = getString(R.string.bluetooth_notification_channel)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(getString(R.string.bluetooth_notification_channel_id), name, importance)
            mChannel.description = descriptionText

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    override fun getBluetoothService(lambda: (BluetoothService) -> Unit) {
        bluetoothService.also {
            if(it == null) {
                Intent(this, BluetoothService::class.java).also { intent ->
                    bindService(intent, object: ServiceConnection {
                        override fun onServiceDisconnected(name: ComponentName?) {
                            /* nothing */
                        }

                        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                            val bs = (service as? BluetoothService.LocalBinder)?.getService()
                            if(bs != null) {
                                bluetoothService = bs
                                lambda(bs)
                            }
                        }
                    }, Context.BIND_AUTO_CREATE)
                }
            } else {
                lambda(it)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}

private class MainViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(i: Int): Fragment {
        return when (i) {
            BLUETOOTH -> TabContentFragment.newInstance(TabContentFragment.KIND_BT)
            WIFI -> TabContentFragment.newInstance(TabContentFragment.KIND_WIFI)
            BOTH -> TabContentFragment.newInstance(TabContentFragment.KIND_BOTH)
            else -> throw IndexOutOfBoundsException(i.toString())
        }
    }

    override fun getCount(): Int = 3

    companion object {
        const val BLUETOOTH = 0
        const val WIFI = 1
        const val BOTH = 2
    }
}
