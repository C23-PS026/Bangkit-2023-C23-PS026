package com.capstoneproject.meatfyi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstoneproject.meatfyi.databinding.ActivityInfoBinding
import com.capstoneproject.meatfyi.databinding.ActivityMainBinding

class InfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Meat Info"

        infoDetailBeef()
        //infoDetailLamb()
        infoDetailChicken()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun infoDetailBeef(){
        binding.cardview1.setOnClickListener{
            startActivity(Intent(this, DetailInfoBeefActivity::class.java))
        }
    }

//    private fun infoDetailLamb(){
//        binding.cardview2.setOnClickListener{
//            startActivity(Intent(this, DetailInfoLambActivity::class.java))
//        }
//    }

    private fun infoDetailChicken(){
        binding.cardview3.setOnClickListener{
            startActivity(Intent(this, DetailInfoChickenActivity::class.java))
        }
    }
}