package com.stear.mahsul.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.stear.mahsul.model.Users
import com.stear.mahsul.network.FirebaseSource
import com.stear.mahsul.repository.Repository

class ProfileFragmentViewModel(val repo: Repository) : ViewModel() {


    val userInfo: MutableLiveData<Users> = MutableLiveData()
    val isUserUpdate : MutableLiveData<Int> = MutableLiveData()
    fun userUpdate(userName :String,userPhone:String,uUid: String){
        repo.doesHasUserInfo(uUid).get().addOnSuccessListener{snap->
            if (snap != null && !snap.isEmpty) {
                val document = snap.documents
                document.forEach(){
                    val ref = repo.getDb().collection("users").document(it.id)
                    repo.getDb().runTransaction { transaction ->
                        transaction.update(ref, "userName", userName)
                        transaction.update(ref, "userPhone", userPhone)
                    }.addOnSuccessListener {
                        isUserUpdate.value = 2
                    }.addOnFailureListener(){
                        isUserUpdate.value = 3
                    }
                }
            }


        }


    }

    fun doesHasUserInfo(uUid: String) {
        repo.doesHasUserInfo(uUid).addSnapshotListener() { snap, error ->
            if (snap != null && !snap.isEmpty) {
                val document = snap.documents
                document.forEach() {
                    val user = Users(
                        it.get("uId").toString(),
                        it.get("userName").toString(),
                        it.get("userPhone").toString(),
                        it.get("ilanSayisi").toString(), false,
                        it.get("photoUrl").toString(),
                    )
                    userInfo.value = user
                }
            }else{
                userInfo.value = Users("","","","",false,"")
            }
        }
    }

    fun userPhotoUpdate(userPhoto : String,uId : String){
        repo.doesHasUserInfo(uId).get().addOnSuccessListener{snap->
            if (snap != null && !snap.isEmpty) {
                val document = snap.documents
                document.forEach(){
                    val ref = repo.getDb().collection("users").document(it.id)
                    repo.getDb().runTransaction { transaction ->
                        transaction.update(ref, "photoUrl", userPhoto)
                    }.addOnSuccessListener {
                        isUserUpdate.value = 1
                    }.addOnFailureListener(){
                        isUserUpdate.value = 3
                    }
                }
            }
        }
    }



}


