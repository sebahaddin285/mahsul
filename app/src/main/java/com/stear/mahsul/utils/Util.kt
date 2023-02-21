package com.stear.mahsul.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.stear.mahsul.R

class Util {
    companion object{

        fun makeAlerDialog(context:Context,mesaj:String){
            val ad = AlertDialog.Builder(context)
            ad.setMessage(mesaj)
            ad.setTitle("Hatalı Giriş")
            ad.setIcon(R.drawable.titleicon)

            ad.setPositiveButton("Tamam"){dialogInterface,i ->

            }
            ad.create().show()
        }
    }
}