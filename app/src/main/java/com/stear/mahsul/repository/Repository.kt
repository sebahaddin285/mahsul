package com.stear.mahsul.repository


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.stear.mahsul.network.FirebaseSource

class Repository() {

    fun signIn(eMail: String, password: String) = FirebaseSource.login(eMail, password)

    fun signUp(eMail: String, password: String) = FirebaseSource.register(eMail, password)


}