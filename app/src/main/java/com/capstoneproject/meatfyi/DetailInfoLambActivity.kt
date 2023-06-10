package com.capstoneproject.meatfyi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstoneproject.meatfyi.databinding.ActivityDetailInfoBinding
import com.capstoneproject.meatfyi.databinding.ActivityDetailInfoLambBinding

class DetailInfoLambActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailInfoLambBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInfoLambBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Detail Info"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}