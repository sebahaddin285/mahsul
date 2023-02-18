package com.stear.sondaj.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.stear.sondaj.R
import com.stear.sondaj.databinding.FragmentMapBinding


class MapFragment : Fragment() {

    private lateinit var  _binding : FragmentMapBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMapBinding.inflate(inflater,container,false)





        return _binding.root
    }




}