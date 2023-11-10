package com.nushhack.keko

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.nushhack.keko.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.content.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_item_1 -> {
                    binding.content.viewPager.currentItem = 0
                }
                R.id.nav_item_2 -> {
                    binding.content.viewPager.currentItem = 1
                }
                R.id.nav_item_3 -> {
                    binding.content.viewPager.currentItem = 2
                }
            }
            true
        }

        binding.content.viewPager.adapter = MainViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.content.viewPager.isUserInputEnabled = false

        binding.content.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                if (!binding.toolbarTitle2.isVisible) binding.toolbarTitle2.isVisible = true

                binding.toolbarTitle.translationX = -positionOffset * binding.toolbar.width
                binding.toolbarTitle2.translationX = -positionOffset * binding.toolbar.width + binding.toolbar.width

                binding.toolbarTitle2.text = when (position) {
                    0 -> getString(R.string.scan_title)
                    1 -> getString(R.string.account_title)
                    else -> getString(R.string.app_title)
                }

                binding.toolbarTitle.text = when (position) {
                    0 -> getString(R.string.lessons_title)
                    1 -> getString(R.string.scan_title)
                    2 -> getString(R.string.account_title)
                    else -> getString(R.string.app_title)
                }

                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageScrollStateChanged(state: Int) {
                when(state) {
                    ViewPager2.SCROLL_STATE_IDLE -> {
                        if (binding.toolbarTitle2.isVisible) binding.toolbarTitle2.isVisible = false
                    }
                    else -> {}
                }
                super.onPageScrollStateChanged(state)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}