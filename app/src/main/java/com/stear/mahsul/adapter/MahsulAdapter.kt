package com.stear.mahsul.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import com.stear.mahsul.R
import com.stear.mahsul.model.Mahsul
import com.stear.mahsul.utils.Util
import com.stear.mahsul.view.MahsulListFragmentDirections
import java.text.NumberFormat
import java.util.*

class MahsulAdapter (val mContext: Context) : RecyclerView.Adapter<MahsulAdapter.ViewHolderClass>() {
    var list: List<Mahsul> = listOf()
    var imagePhoto: ImageView? = null
    inner class ViewHolderClass(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView
        val priceText: TextView
        val turText: TextView
        val ilanCardView: MaterialCardView



        init {
            titleText = view.findViewById(R.id.titleFText)
            priceText = view.findViewById(R.id.priceFText)
            turText = view.findViewById(R.id.turFText)
            imagePhoto = view.findViewById(R.id.imagePhoto)
            ilanCardView = view.findViewById(R.id.ilanCardView)
        }

    }

    @SuppressLint("ResourceType", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val list = list[position]

        holder.apply {
            titleText.text = list.titleText
            turText.text = "Mahsul Türü : "+list.turText
            priceText.text = Util.currencyFormatter(list.priceText) + " TL"

            Picasso.get().load(list.photoUrl).into(imagePhoto)

            holder.ilanCardView.setOnClickListener(){
                val pass = MahsulListFragmentDirections.goToDetail(list.titleText,list.destinationText,list.turText,list.eMail,list.priceText,list.photoUrl)
                Navigation.findNavController(it).navigate(pass)
            }
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val design = LayoutInflater.from(mContext).inflate(R.layout.adapter_design, parent, false)
        return ViewHolderClass(design)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMahsulList(listNew: List<Mahsul>) {
        list = listNew
        notifyDataSetChanged()
    }

}


