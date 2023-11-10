package com.nushhack.keko

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    private val numTabs = 3
    override fun getItemCount() = numTabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LessonsFragment()
            1 -> ScanFragment()
            else -> AccountFragment()
        }
    }
}