package com.stear.mahsul.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.stear.mahsul.repository.Repository
import com.stear.mahsul.utils.AuthListener
import com.stear.mahsul.utils.ResetListener

class SignInActivityViewModel(val repo: Repository) : ViewModel() {

    //auth listener
    var authListener: AuthListener? = null
    var resetListener: ResetListener? = null
    var googleListener : MutableLiveData<Boolean> = MutableLiveData()
    fun signIn(eMail: String, password: String) {
        val loginResult = repo.signIn(eMail, password)

        loginResult.addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                authListener?.onSuccess()
            } else {
                authListener?.onFailure(task.exception!!.message.toString())
            }
        }
    }

    fun resetPassword(eMail: String) {
        val resetResult = repo.resetPassword(eMail)


        resetResult.addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                resetListener?.onResetSuccess()
            } else {
                resetListener?.onResetFailure(task.exception!!.message.toString())
            }
        }


    }
    fun firebaseAuthWithGoogle(account: GoogleSignInAccount):MutableLiveData<Boolean>{

        repo.signInWithGoogle(account)
            .addOnCompleteListener { task ->
                googleListener.value = task.isSuccessful
            }

        return googleListener
    }








}