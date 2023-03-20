package com.stear.mahsul.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.stear.mahsul.R
import com.stear.mahsul.databinding.FragmentMahsulAddBinding
import com.stear.mahsul.databinding.FragmentMahsulDetailBinding


class MahsulDetailFragment : Fragment() {

    private lateinit var _binding: FragmentMahsulDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMahsulDetailBinding.inflate(inflater,container,false)


        val bundle : MahsulDetailFragmentArgs by navArgs()

        _binding.titleDetailText.text = bundle.title
        _binding.turDetailText.text = bundle.tur
        _binding.priceDetailText.text = bundle.price + "TL"
        _binding.destinationDetailText.text = bundle.destination
        _binding.eMailDetailText.text = bundle.eMail

        Picasso.get().load(bundle.url).into(_binding.imageView5)





        return _binding.root
    }


}