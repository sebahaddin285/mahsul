package com.stear.mahsul.network

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stear.mahsul.model.Users

class FirebaseSource {


    companion object {
        private val firebaseAuth: FirebaseAuth by lazy {FirebaseAuth.getInstance()}
        private val firebaseStore: Firebase by lazy {Firebase}


        fun login(email: String, password: String) =
            firebaseAuth.signInWithEmailAndPassword(email, password)


        fun register(email: String, password: String) =
            firebaseAuth.createUserWithEmailAndPassword(email, password)

        fun resetPassword(email: String) = firebaseAuth.sendPasswordResetEmail(email)

        fun signInWithGoogle(acct: GoogleSignInAccount) = firebaseAuth.signInWithCredential(
            GoogleAuthProvider.getCredential(acct.idToken,null))


        //**DataBase**

        fun doesHasUserInfo(uUid : String) = firebaseStore.firestore.collection("users")
            .whereEqualTo("uId",uUid)

        fun userSave(map : HashMap<String,Any>) = firebaseStore.firestore.collection("users").add(map)


        fun getDb() = firebaseStore.firestore

    }


}