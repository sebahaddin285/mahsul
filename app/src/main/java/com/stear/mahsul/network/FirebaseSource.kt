package com.stear.mahsul.network

import com.google.firebase.auth.FirebaseAuth

class FirebaseSource {


    companion object {
        private val firebaseAuth: FirebaseAuth by lazy {
            FirebaseAuth.getInstance()
        }

        fun login(email: String, password: String) =
            firebaseAuth.signInWithEmailAndPassword(email, password)


        fun register(email: String, password: String) =
            firebaseAuth.createUserWithEmailAndPassword(email, password)
    }


}