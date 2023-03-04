package com.stear.mahsul.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt


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
        fun reduceBitmapSize(bitmap: Bitmap, MAX_SIZE: Int): Bitmap? {
            val ratioSquare: Double
            val bitmapHeight: Int = bitmap.height
            val bitmapWidth: Int = bitmap.width
            ratioSquare = (bitmapHeight * bitmapWidth / MAX_SIZE).toDouble()
            if (ratioSquare <= 1) return bitmap
            val ratio = Math.sqrt(ratioSquare)
            Log.d("mylog", "Ratio: $ratio")
            val requiredHeight = (bitmapHeight / ratio).roundToInt()
            val requiredWidth = (bitmapWidth / ratio).roundToInt()
            return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true)
        }

        fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "Title",
                null
            )
            return Uri.parse(path)
        }

        fun isValidPassword(password: String): Boolean {
            if (password.length < 6) return false
            password.forEach {
                if (!it.isLetterOrDigit()) return false
            }

            return true
        }
        fun isValidNumber(number: String): Boolean {
            number.forEach {
                if (!it.isDigit()) return false
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