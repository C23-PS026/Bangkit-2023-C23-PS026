package com.capstoneproject.meatfyi.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstoneproject.meatfyi.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLandingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.contButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java ))
            finish()
        }
    }
}