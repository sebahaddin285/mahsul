package com.stear.mahsul.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stear.mahsul.repository.Repository
import com.stear.mahsul.utils.AuthListener
import kotlinx.coroutines.launch

class SignInActivityViewModel(val repo: Repository) : ViewModel() {

    //auth listener
    var authListener: AuthListener? = null

    fun signIn(eMail: String, password: String) {
        val loginResult = repo.signIn(eMail, password)

        loginResult.addOnCompleteListener() { task ->
            if (task.isSuccessful){
                authListener?.onSuccess()
            }else {
                authListener?.onFailure(task.exception!!.message.toString())
            }
        }
    }


}