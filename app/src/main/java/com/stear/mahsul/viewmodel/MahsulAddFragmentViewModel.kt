package com.stear.mahsul.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stear.mahsul.model.Users
import com.stear.mahsul.repository.Repository


class MahsulAddFragmentViewModel(private val repository: Repository) : ViewModel(){

    val isSuccessMahsul: MutableLiveData<Boolean> = MutableLiveData()

    fun saveMahsulAdd(map : HashMap<String,Any>) {
        repository.saveMahsulAdd(map).addOnCompleteListener(){task ->
            if (task.isSuccessful){
                isSuccessMahsul.value = true
            }
        }
    }




}