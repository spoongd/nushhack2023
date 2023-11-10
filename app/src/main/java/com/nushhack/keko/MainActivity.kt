package com.nushhack.keko

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
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
//        binding.content.viewPager.isUserInputEnabled = false
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