package com.stear.sondaj.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.stear.sondaj.MainActivity
import com.stear.sondaj.R
import com.stear.sondaj.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var _binding : ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      _binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)


    }
}