package com.example.dragon.sstrilateration

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.example.dragon.sstrilateration.fragment.TabContentFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = MainViewPagerAdapter(this.supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpUI()
        setUpBluetooth()

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
