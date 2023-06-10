package com.capstoneproject.meatfyi

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.capstoneproject.meatfyi.databinding.ActivityProfileBinding
import com.capstoneproject.meatfyi.model.User
import com.capstoneproject.meatfyi.sharepreference.constant
import com.capstoneproject.meatfyi.sharepreference.sharePreferenced
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.scottyab.aescrypt.AESCrypt

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharepref: sharePreferenced
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var uid: String
    lateinit var us: User
    lateinit var pd: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharepref = sharePreferenced(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.passwordEtProfile.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        database =
            FirebaseDatabase.getInstance("https://capstone-project-c23-ps026-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Data_user")
        readData()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        val item: MenuItem = menu!!.findItem(R.id.profile_page)
        item.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> logout()
            else -> return super.onOptionsItemSelected(item)
        }
        return true

    }

    private fun logout() {
        var ad = AlertDialog.Builder(this)
        ad.setTitle("Confirm Logout")
        sharepref.put(constant.PREF_IS_LOGIN, false)
        ad.setMessage("Are you sure want to logout?")
        ad.setCancelable(false)
        ad.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
            sharepref.clear()
            val pg = ProgressDialog(this)
            pg.setTitle("Logout")
            pg.setMessage("Wait a moment...")
            pg.setCancelable(false)
            pg.show()
            Toast.makeText(this, "You have logged out", Toast.LENGTH_SHORT).show()
            this.finish()
            dialog.cancel()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        })
        ad.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })

        var alert = ad.create()
        alert.show()
    }

    fun readData() {
        showProgressDialog()
        val uid = auth.currentUser?.uid
        if (uid!!.isNotEmpty()) {
            database.child(uid!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    us = snapshot.getValue(User::class.java)!!
                    binding.usernameEtProfile.setText(us.username)
                    binding.usernameEtProfile.inputType = 0
                    binding.emailEtProfile.setText(us.email)
                    binding.emailEtProfile.inputType = 0
                    val password = "capstoneprojectpw"
                    val stringpw = us.password.toString()
//                    binding.passwordEtProfile.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordEtProfile.setText(AESCrypt.decrypt(password, stringpw))
//                    binding.passwordEtProfile.isFocusableInTouchMode = false
//                    binding.passwordInputProfile.isFocusableInTouchMode = false
                    binding.passwordEtProfile.isFocusable = false
                    closeProgressDialog()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        }
    }

    private fun showProgressDialog() {
        pd = ProgressDialog(this)
        pd.setTitle("Loading")
        pd.setMessage("Wait a moment...")
        pd.setCancelable(false)
        pd.show()
    }

    private fun closeProgressDialog() {
        pd.dismiss()
    }
}