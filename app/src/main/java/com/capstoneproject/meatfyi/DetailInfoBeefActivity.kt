package com.capstoneproject.meatfyi

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstoneproject.meatfyi.databinding.ActivityDetailInfoBinding

class DetailInfoBeefActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInfoBinding.inflate(layoutInflater)
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
        binding.webinfoButtonBeef.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nutritionix.com/food/beef/100-g"))
            startActivity(intent)
        }
    }
}