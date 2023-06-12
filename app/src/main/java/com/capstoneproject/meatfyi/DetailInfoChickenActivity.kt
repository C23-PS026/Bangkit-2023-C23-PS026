package com.capstoneproject.meatfyi

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstoneproject.meatfyi.databinding.ActivityDetailInfoBinding
import com.capstoneproject.meatfyi.databinding.ActivityDetailInfoChickenBinding

class DetailInfoChickenActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailInfoChickenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInfoChickenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Detail Info"

        linkToWeb()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun linkToWeb(){
        binding.webinfoButtonChick.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nutritionix.com/food/chicken"))
            startActivity(intent)
        }
    }
}