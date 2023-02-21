package com.stear.mahsul.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.stear.mahsul.MainActivity
import com.stear.mahsul.R
import com.stear.mahsul.databinding.ActivitySignInBinding
import com.stear.mahsul.repository.Repository
import com.stear.mahsul.utils.AuthListener
import com.stear.mahsul.utils.Util
import com.stear.mahsul.viewmodel.SignInActivityViewModel
import com.stear.mahsul.viewmodel.SignInActivityViewModelFactory

class SignInActivity : AppCompatActivity(), AuthListener {
    private lateinit var _binding: ActivitySignInBinding
    private lateinit var firebase: Firebase
    private lateinit var viewModel: SignInActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        var intent: Intent?

        //Mvvm
        val repo: Repository by lazy { Repository() }
        val viewModelFactory by lazy { SignInActivityViewModelFactory(repo) }

        //send repository to viewmodel
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[SignInActivityViewModel::class.java]

        viewModel.authListener = this


        _binding.signInButton.setOnClickListener() {
            val eMail = _binding.emailText.text.toString().trim()
            val password = _binding.passwordText.text.toString().trim()
            if (eMail != "" && password != "") {
                viewModel.signIn(eMail, password)
            } else {
                Util.makeAlerDialog(this, "Boş Bırakılan Alanlar Var")
            }
        }

        _binding.signUpButton.setOnClickListener() {
            intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
        }

        _binding.googleButton.setOnClickListener() {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onSuccess() {
        if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true){
            intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }else{
            Util.makeAlerDialog(this,"E Postanıza Gelen Maili Onaylayınız")
        }

    }

    override fun onFailure(message: String) {
        Util.makeAlerDialog(this,"Lütfen E-Posta ve ya Parolayı Kontrol Ediniz")
    }


}