package com.capstoneproject.meatfyi

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.capstoneproject.meatfyi.databinding.ActivityRegisterBinding
import com.capstoneproject.meatfyi.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.scottyab.aescrypt.AESCrypt

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        binding.passwordEtReg.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        auth = FirebaseAuth.getInstance()
        goToLogin()
        registerButton()
    }

    private fun registerButton() {
        binding.regButton.setOnClickListener {
            val username = binding.usernameEtReg.text.toString()
            val email = binding.emailEtReg.text.toString()
            val password = binding.passwordEtReg.text.toString()

            if (username.isEmpty()) {
                binding.usernameEtReg.error = "Username harus diisi"
                binding.usernameEtReg.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.emailEtReg.error = "Email harus diisi"
                binding.emailEtReg.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEtReg.error = "Email tidak valid"
                binding.emailEtReg.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.passwordEtReg.error = "Password harus diisi"
                binding.passwordEtReg.requestFocus()
                return@setOnClickListener
            }
            if (password.length < 8) {
                binding.passwordEtReg.error = "Password harus terdiri dari 8 karakter"
                binding.passwordEtReg.requestFocus()
                return@setOnClickListener
            }
            registFirebase(email, password)
        }
    }


    private fun registFirebase(email: String, password: String) {
        val pg = ProgressDialog(this)
        pg.setTitle("Processing")
        pg.setMessage("Wait a moment...")
        pg.setCancelable(false)
        pg.show()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    createDatabase()
                    Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
                pg.dismiss()
            }
    }

    private fun createDatabase() {
        val nama = binding.usernameEtReg.text.toString()
        val emaill = binding.emailEtReg.text.toString()
        val passwordd = AESCrypt.encrypt("capstoneprojectpw", binding.passwordEtReg.text.toString())
        val uid = auth.currentUser?.uid
        var inputUid = uid.toString()


        val user = User(inputUid, nama, emaill, passwordd)
        FirebaseDatabase.getInstance("https://capstone-project-c23-ps026-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child(
            "Data_user"
        ).child(uid!!).setValue(user).addOnCompleteListener() {
        }

    }

    private fun goToLogin() {
        binding.logButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

//    // To Encrypt
//    String password = "password";
//    String message = "hello world";
//    try {
//        String encryptedMsg = AESCrypt.encrypt(password, message);
//    }catch (GeneralSecurityException e){
//        //handle error
//    }
//
//// To Decrypt
//    String password = "password";
//    String encryptedMsg = "2B22cS3UC5s35WBihLBo8w==";
//    try {
//        String messageAfterDecrypt = AESCrypt.decrypt(password, encryptedMsg);
//    }catch (GeneralSecurityException e){
//        //handle error - could be due to incorrect password or tampered encryptedMsg
//    }

}
