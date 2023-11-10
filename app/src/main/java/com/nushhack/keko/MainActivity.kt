package com.nushhack.keko

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.nushhack.keko.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var photoLauncher: ActivityResultLauncher<Intent>

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

        photoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            if (res.resultCode == Activity.RESULT_OK) {
//                        photoChanged = true
//                        photoUri = res.data?.data!!
//                        binding.pfpImageview.setImageURI(photoUri)
//                        updateButton()
                Toast.makeText(this, "gg", Toast.LENGTH_SHORT).show()
            }
        }
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
            R.id.action_image -> {


                val imageIntent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
                photoLauncher.launch(imageIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}