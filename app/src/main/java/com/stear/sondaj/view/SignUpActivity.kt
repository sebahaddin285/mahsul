package com.stear.mahsul.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.stear.mahsul.R
import com.stear.mahsul.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var _binding : ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        _binding.signUpButton.setOnClickListener(){

        }
    }
}