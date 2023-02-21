package com.stear.mahsul.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.stear.mahsul.repository.Repository
import com.stear.mahsul.utils.AuthListener


class SignUpActivityViewModel(val repo: Repository) : ViewModel() {

    var authListener: AuthListener? = null

    fun signUp(eMail: String, password: String) {
        val result = repo.signUp(eMail, password)

        result.addOnCompleteListener(){task ->
            if (task.isSuccessful){
                authListener?.onSuccess()
                FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
            }
            else{
                authListener?.onFailure(task.exception!!.message.toString())
            }
        }

    }
}