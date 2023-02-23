package com.stear.mahsul.utils

interface ResetListener {
    fun onResetSuccess()
    fun onResetFailure(message: String)
}