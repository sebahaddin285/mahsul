package com.stear.mahsul.network

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseSource {


    companion object {
        private val firebaseAuth: FirebaseAuth by lazy {
            FirebaseAuth.getInstance()
        }

        fun login(email: String, password: String) =
            firebaseAuth.signInWithEmailAndPassword(email, password)


        fun register(email: String, password: String) =
            firebaseAuth.createUserWithEmailAndPassword(email, password)

        fun resetPassword(email: String) = firebaseAuth.sendPasswordResetEmail(email)

        fun signInWithGoogle(acct: GoogleSignInAccount) = firebaseAuth.signInWithCredential(
            GoogleAuthProvider.getCredential(acct.idToken,null))
    }


}