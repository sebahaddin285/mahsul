package com.stear.mahsul.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.stear.mahsul.R
import com.stear.mahsul.view.SignUpActivity

class Util {
    companion object{

        fun makeAlerDialog(context:Context,mesaj:String,title:String,icon:Int){
            val ad = AlertDialog.Builder(context)
            ad.setMessage(mesaj)
            ad.setTitle(title)
            ad.setIcon(icon)

            ad.setPositiveButton("Tamam"){dialogInterface,i ->

            }
            ad.create().show()
        }

        fun isValidPassword(password: String): Boolean {
            if (password.length < 6) return false
            password.forEach {
                if (!it.isLetterOrDigit()) return false
            }

            return true
        }

        fun isNetworkAvailable(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false
        }



    }
}