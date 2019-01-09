package com.example.dragon.sstrilateration.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dragon.sstrilateration.BlankFragment
import com.example.dragon.sstrilateration.R
import com.example.dragon.sstrilateration.fragment.TabContentFragment.Companion.KIND_BOTH
import com.example.dragon.sstrilateration.fragment.TabContentFragment.Companion.KIND_BT
import com.example.dragon.sstrilateration.fragment.TabContentFragment.Companion.KIND_WIFI
import kotlinx.android.synthetic.main.fragment_tab_content.*

class TabContentFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab_content, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val kind = arguments?.getInt(ARG_KIND)

        kind!!

        tabFragment_vp.adapter = TabViewPagerAdapter(this.childFragmentManager, kind)
        tabFragment_tabLayout.setupWithViewPager(tabFragment_vp)
    }

    companion object {
        const val KIND_BT = 0
        const val KIND_WIFI = 1
        const val KIND_BOTH = 2

        private const val ARG_KIND = "kind"

        @JvmStatic
        fun newInstance(kind: Int) =
            TabContentFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_KIND, kind)
                }
            }
    }
}

private class TabViewPagerAdapter(fm: FragmentManager, private val kind: Int) : FragmentPagerAdapter(fm) {
    override fun getPageTitle(position: Int): CharSequence? {
        return when (kind) {
            KIND_BT -> {
                when (position) {
                    0 -> "Bluetooth beacon"
                    1 -> "Bluetooth receiver"
                    else -> throw IndexOutOfBoundsException(position.toString())
                }
            }
            KIND_WIFI -> {
                when (position) {
                    0 -> "Wifi beacon"
                    1 -> "Wifi receiver"
                    else -> throw IndexOutOfBoundsException(position.toString())
                }
            }
            KIND_BOTH -> {
                when (position) {
                    0 -> "Both beacon"
                    1 -> "Both receiver"
                    else -> throw IndexOutOfBoundsException(position.toString())
                }
            }
            else -> throw IndexOutOfBoundsException(kind.toString())
        }
    }

    override fun getItem(position: Int): Fragment {
        return when (kind) {
            KIND_BT -> {
                when (position) {
                    0 -> BluetoothBeaconFragment.newInstance()
                    1 -> BluetoothScannerFragment.newInstance()
                    else -> throw IndexOutOfBoundsException(position.toString())
                }
            }
            KIND_WIFI -> {
                when (position) {
                    0 -> BlankFragment.newInstance()
                    1 -> BlankFragment.newInstance()
                    else -> throw IndexOutOfBoundsException(position.toString())
                }
            }
            KIND_BOTH -> {
                when (position) {
                    0 -> BlankFragment.newInstance()
                    1 -> BlankFragment.newInstance()
                    else -> throw IndexOutOfBoundsException(position.toString())
                }
            }
            else -> throw IndexOutOfBoundsException(kind.toString())
        }
    }

    override fun getCount(): Int = when (kind) {
        KIND_BT -> 2
        KIND_WIFI -> 2
        KIND_BOTH -> 2
        else -> throw IndexOutOfBoundsException(kind.toString())
    }

}