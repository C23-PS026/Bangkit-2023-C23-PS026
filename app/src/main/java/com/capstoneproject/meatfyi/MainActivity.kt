package com.capstoneproject.meatfyi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstoneproject.meatfyi.databinding.ActivityCameraBinding
import com.capstoneproject.meatfyi.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraPermission()
        openCameraX()
        meatInfo()
        meatArticle()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        val item: MenuItem = menu!!.findItem(R.id.logout)
        item.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile_page -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCam = it.data?.getBooleanExtra("isBackCam", true) as Boolean

//            myFile?.let { file ->
//                rotateFile(file, isBackCam)
//                getFileImg = file
//                binding.uploadImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Izin ditolak",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun cameraPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun openCameraX() {
        binding.detectButton.setOnClickListener {
            launcherIntentCameraX.launch(Intent(this, CameraActivity::class.java))
        }

    }

    private fun meatInfo() {
        binding.infoButton.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }
    }

    private fun meatArticle() {
        binding.articleButton.setOnClickListener {
            startActivity(Intent(this, ArticleActivity::class.java))
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        var TOKEN: String? = null
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}