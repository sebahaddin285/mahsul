package com.stear.mahsul

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.stear.mahsul.databinding.ActivityMainBinding
import com.stear.mahsul.utils.Util


class MainActivity : AppCompatActivity() {
    private lateinit var _binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        Util.makeAlerDialog(this,FirebaseAuth.getInstance().currentUser?.email.toString())
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        NavigationUI.setupWithNavController(_binding.bottomNav,navHostFragment.navController)




    }


}