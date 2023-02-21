package com.stear.mahsul.utils

interface AuthListener {
    fun onSuccess()
    fun onFailure(message: String)
}