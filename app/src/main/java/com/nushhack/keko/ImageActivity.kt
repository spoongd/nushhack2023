package com.nushhack.keko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nushhack.keko.databinding.ActivityImageBinding
import coil.load

class ImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra(getString(R.string.key_path))

        binding.image.load("http://34.125.233.174:5000$url") {
            crossfade(true)
            placeholder(R.drawable.no_image)
        }
    }
}