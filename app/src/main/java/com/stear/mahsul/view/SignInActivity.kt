package com.stear.mahsul.view


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.stear.mahsul.MainActivity
import com.stear.mahsul.R
import com.stear.mahsul.databinding.ActivitySignInBinding
import com.stear.mahsul.repository.Repository
import com.stear.mahsul.utils.AuthListener
import com.stear.mahsul.utils.ResetListener
import com.stear.mahsul.utils.Util
import com.stear.mahsul.viewmodel.SignInActivityViewModel
import com.stear.mahsul.viewmodel.SignInActivityViewModelFactory


class SignInActivity : AppCompatActivity(), AuthListener, ResetListener {
    companion object {
        private const val TAG = "LoginFragment"
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var _binding: ActivitySignInBinding
    private lateinit var viewModel: SignInActivityViewModel
    private lateinit var gsc : GoogleSignInClient
    private lateinit var gso : GoogleSignInOptions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        var intent: Intent?

        //sign in with google
         gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(applicationContext.getString(R.string.client_id))
            .requestEmail()
            .build()
         gsc = GoogleSignIn.getClient(this,gso)

        //Mvvm
        val repo: Repository by lazy { Repository() }
        val viewModelFactory by lazy { SignInActivityViewModelFactory(repo) }

        //send repository to viewmodel
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[SignInActivityViewModel::class.java]

        viewModel.authListener = this
        viewModel.resetListener = this

        currentUser()


        _binding.signInButton.setOnClickListener() {
            val eMail = _binding.emailText.text.toString().trim()
            val password = _binding.passwordText.text.toString().trim()
            if (eMail != "" && password != "") {
                viewModel.signIn(eMail, password)
            } else {
                Util.makeAlerDialog(
                    this,
                    "Boş Bırakılan Alanlar Var",
                    "Hatalı Giriş",
                    R.drawable.titleicon
                )
            }
        }

        _binding.signUpButton.setOnClickListener() {
            intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
        }

        //Sign In With Google


        _binding.googleButton.setOnClickListener() {
            signIn()
        }
        _binding.passwordForget.setOnClickListener() {
            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(R.layout.fragment_reset_email_bottom_sheet)
            val eMailInputText = bottomSheetDialog.findViewById<TextInputEditText>(R.id.emailText)
            val resetButton = bottomSheetDialog.findViewById<MaterialCardView>(R.id.resetButton)

            resetButton?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val eMail = eMailInputText?.text.toString()
                    if (eMail.isNotEmpty()) {
                        viewModel.resetPassword(eMail)
                    } else {
                        Util.makeAlerDialog(
                            this@SignInActivity,
                            "Boş Bırakılan Alanlar Var",
                            "Hata",
                            R.drawable.titleicon
                        )
                    }
                }

            })

            bottomSheetDialog.show()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!

                viewModel.firebaseAuthWithGoogle(account).observe(this){
                    if (it){
                        intent = Intent(applicationContext, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }else{
                        Util.makeAlerDialog(
                            this,
                            "Bir Hata Meydan Geldi",
                            "Hatalı Giriş",
                            R.drawable.titleicon
                        )
                    }
                }

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        currentUser()
    }

    override fun onSuccess() {
        if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
            intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Util.makeAlerDialog(
                this,
                "E Postanıza Gelen Maili Onaylayınız",
                "Hatalı Giriş",
                R.drawable.titleicon
            )
        }

    }

    override fun onFailure(message: String) {
        Util.makeAlerDialog(
            this,
            "Lütfen E-Posta veya Parolayı Kontrol Ediniz",
            "Hatalı Giriş",
            R.drawable.titleicon
        )
    }

    override fun onResetSuccess() {
        val ad = AlertDialog.Builder(this)
        ad.setMessage("Lütfen Mailiniz Üzerinden Şifrenizi Yenileyiniz")
        ad.setTitle("Başarılı")
        ad.setIcon(R.drawable.baseline_verified_24)

        ad.setPositiveButton("Tamam") { dialogInterface, i ->

        }
        ad.create().show()
    }

    override fun onResetFailure(message: String) {
        Util.makeAlerDialog(this, "Hatalı E-Posta Girişi", "Hatalı Giriş", R.drawable.titleicon)

    }

    fun currentUser() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signIn() {


        val ad = AlertDialog.Builder(this)
        ad.setMessage("Giriş Yaparak Gizlilik Politikası ve Kullanım Şartlarını Kabul Etmiş Olursunuz")
        ad.setTitle("Politikamız")
        ad.setIcon(R.drawable.titleicon)

        ad.setPositiveButton("Tamam"){dialogInterface,i ->
            val signInIntent = gsc.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        ad.setNegativeButton("Gizlilik Politikası"){dialogInterface,i ->
            openPolitics("https://raw.githubusercontent.com/sebahaddin285/mahsul-politika/main/Gizlilik-S%C3%B6zle%C5%9Fmesi.txt")
        }
        ad.setNeutralButton("Kullanım Şartları"){dialogInterface,i ->
            openPolitics("https://raw.githubusercontent.com/sebahaddin285/mahsul-politika/main/kullan%C4%B1m%20%C5%9Fartlar%C4%B1.txt")
        }

        ad.create().show()

    }
    fun openPolitics(link : String){
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(browserIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this, "No application can handle this request."
                        + " Please install a webbrowser", Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }




}