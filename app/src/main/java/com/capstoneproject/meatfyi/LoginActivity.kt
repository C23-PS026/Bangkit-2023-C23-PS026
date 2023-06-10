package com.capstoneproject.meatfyi

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.Toast
import com.capstoneproject.meatfyi.databinding.ActivityLoginBinding
import com.capstoneproject.meatfyi.databinding.ActivityMainBinding
import com.capstoneproject.meatfyi.model.User
import com.capstoneproject.meatfyi.sharepreference.constant
import com.capstoneproject.meatfyi.sharepreference.sharePreferenced
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    lateinit var user: User
    lateinit var sharedPref: sharePreferenced

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        sharedPref = sharePreferenced(this)
        auth = FirebaseAuth.getInstance()
        goToRegister()
        loginButton()

        binding.passwordEt.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    private fun loginButton() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            if (email.isEmpty()) {
                binding.emailEt.error = "Email harus diisi"
                binding.emailEt.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEt.error = "Email tidak valid"
                binding.emailEt.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.passwordEt.error = "Password harus diisi"
                binding.passwordEt.requestFocus()
                return@setOnClickListener
            }

            loginFirebase(email, password)
        }

    }

    override fun onStart() {
        super.onStart()
        if (sharedPref.getBoolean(constant.PREF_IS_LOGIN)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun loginFirebase(email: String, password: String) {
        val pg = ProgressDialog(this)
        pg.setTitle("Processing")
        pg.setMessage("Wait a moment...")
        pg.setCancelable(false)
        pg.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    sharedPref.put(constant.PREF_EMAIL, email)
                    sharedPref.put(constant.PREF_PASSWORD, password)
                    sharedPref.put(constant.PREF_IS_LOGIN, true)
                    Toast.makeText(this, "Welcome $email", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
                pg.dismiss()
            }
    }

    private fun goToRegister() {
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}