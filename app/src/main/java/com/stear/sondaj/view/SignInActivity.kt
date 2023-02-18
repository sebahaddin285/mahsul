package com.stear.sondaj.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.stear.sondaj.MainActivity
import com.stear.sondaj.R
import com.stear.sondaj.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var _binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        var intent: Intent? = null

        _binding.signInButton.setOnClickListener() {
            intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        _binding.signUpButton.setOnClickListener() {
            intent = Intent(applicationContext,SignUpActivity::class.java)
            startActivity(intent)
        }
        _binding.googleButton.setOnClickListener() {
            intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}