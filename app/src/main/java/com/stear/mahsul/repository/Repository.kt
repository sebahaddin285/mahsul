package com.stear.mahsul.repository


import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.stear.mahsul.network.FirebaseSource

class Repository() {

    fun signIn(eMail: String, password: String) = FirebaseSource.login(eMail, password)

    fun signUp(eMail: String, password: String) = FirebaseSource.register(eMail, password)

    fun resetPassword(eMail: String) = FirebaseSource.resetPassword(eMail)

    fun signInWithGoogle(acct: GoogleSignInAccount) =
        FirebaseSource.signInWithGoogle(acct)
}