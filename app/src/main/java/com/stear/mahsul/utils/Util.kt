package com.stear.mahsul.utils

import android.content.Context
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

    }
}