package com.stear.mahsul.view

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.ktx.Firebase
import com.stear.mahsul.R
import com.stear.mahsul.databinding.ActivitySignUpBinding
import com.stear.mahsul.repository.Repository
import com.stear.mahsul.utils.AuthListener
import com.stear.mahsul.utils.Util
import com.stear.mahsul.viewmodel.SignUpActivityViewModel
import com.stear.mahsul.viewmodel.SignUpActivityViewModelFactory


class SignUpActivity : AppCompatActivity(), AuthListener {

    private lateinit var viewModel: SignUpActivityViewModel
    private lateinit var _binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        //Mvvm
        val repo: Repository by lazy { Repository() }
        val viewModelFactory by lazy { SignUpActivityViewModelFactory(repo) }

        //send repository to viewmodel
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[SignUpActivityViewModel::class.java]


        viewModel.authListener = this

        _binding.signUpButton.setOnClickListener() {
            val eMail = _binding.emailText.text.toString().trim()
            val password1 = _binding.passwordText.text.toString().trim()
            val password2 = _binding.passwordText1.text.toString().trim()
            if (eMail != "" && password1 != "" && password2 != "") {
                if (password1 == password2) {
                    if (Util.isValidPassword(password1)){
                        viewModel.signUp(eMail, password1)
                    }else{
                        Util.makeAlerDialog(this,"-Şifre En Az 6 Karakter Olmalıdır\n" +
                                                              "-Sayı ve Harflerden Oluşmalıdır\n"
                                                              ,"Hata",R.drawable.titleicon)
                    }

                } else {
                    Util.makeAlerDialog(
                        this,
                        "Şifreler Aynı Değil",
                        "Hatalı Giriş",
                        R.drawable.titleicon
                    )
                }
            } else {
                Util.makeAlerDialog(
                    this,
                    "Boş Bırakılan alanlar Var",
                    "Hatalı Giriş",
                    R.drawable.titleicon
                )
            }


        }
    }

    override fun onSuccess() {
        val ad = AlertDialog.Builder(this)
        ad.setMessage("Lütfen Mailinize Gelen Doğrulamayı Onaylayınız")
        ad.setTitle("Başarılı")
        ad.setIcon(R.drawable.baseline_verified_24)

        ad.setPositiveButton("Tamam") { dialogInterface, i ->
            finish()
        }
        ad.create().show()
    }

    override fun onFailure(message: String) {
        Util.makeAlerDialog(this, "Bir Hata Oluştu", "Hatalı İşlem", R.drawable.titleicon)
    }
}