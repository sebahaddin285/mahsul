package com.stear.mahsul.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.stear.mahsul.R
import com.stear.mahsul.databinding.FragmentMahsulAddBinding
import com.stear.mahsul.repository.Repository
import com.stear.mahsul.utils.Util
import com.stear.mahsul.viewmodel.MahsulAddFragmentViewModel
import com.stear.mahsul.viewmodel.MahsulAddFragmentViewModelFactory
import com.stear.mahsul.viewmodel.SignInActivityViewModel
import com.stear.mahsul.viewmodel.SignInActivityViewModelFactory
import java.util.*


class MahsulAddFragment : Fragment() {

    private lateinit var viewModel: MahsulAddFragmentViewModel
    private lateinit var _binding: FragmentMahsulAddBinding
    private val storage = Firebase.storage
    private var imageBitmap: Bitmap? = null
    private var selectedImageUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMahsulAddBinding.inflate(inflater, container, false)
        val repo: Repository by lazy { Repository() }
        val viewModelFactory by lazy { MahsulAddFragmentViewModelFactory(repo) }
        selectedImageUri = null

        //send repository to viewmodel
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MahsulAddFragmentViewModel::class.java]

        viewModel.isSuccessMahsul.value = false


        viewModel.isSuccessMahsul.observe(viewLifecycleOwner){
            if (it){
                viewModel.isSuccessMahsul.value = false
                findNavController().popBackStack()
            }
        }


        _binding.saveMahsulButton.setOnClickListener() {
            val price = _binding.priceText.text.toString()
            if (selectedImageUri == null && Util.isValidNumber(price)){
                Toast.makeText(requireContext(),"Bir Hata Oluştu",Toast.LENGTH_SHORT).show()
            }else{
                val map = HashMap<String, Any>()
                map.put("uId", Firebase.auth.currentUser!!.uid)
                map.put("titleText", _binding.titleText.text.toString())
                map.put("destinationText", _binding.destinationText.text.toString())
                map.put("turText", _binding.turText.text.toString())
                map.put("priceText", _binding.priceText.text.toString())
                map.put("photoUrl", selectedImageUri.toString())
                viewModel.saveMahsulAdd(map)
            }


        }
        _binding.image1.setOnClickListener(){
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }


        return _binding.root
    }

    private fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        launchSomeActivity.launch(i)
    }

    //izin verilmediyse izin ister
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                imageChooser()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Fotoğraf Yüklemek İsterseniz Ayarlardan Bellek İznini veriniz",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    //galeriden resim seçer
    private var launchSomeActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode
            == Activity.RESULT_OK
        ) {
            val data = result.data
            // do your operation from here....
            if (data != null
                && data.data != null
            ) {
                selectedImageUri = data.data
                imageBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedImageUri)
                imageBitmap = Util.reduceBitmapSize(imageBitmap!!,921600)
                selectedImageUri = Util.getImageUri(requireContext(),imageBitmap!!)

                val reference = storage.reference
                val uuid = UUID.randomUUID()
                val gorselIsmi = "${uuid}.jpg"
                val gorselReference = reference.child("gorseller").child(gorselIsmi)
                gorselReference.putFile(selectedImageUri!!).addOnSuccessListener { task ->
                    val uploadedImage = reference.child("gorseller").child(gorselIsmi)
                    uploadedImage.downloadUrl.addOnSuccessListener { uri ->
                       selectedImageUri = uri
                    }

                }.addOnFailureListener() {
                    Toast.makeText(requireContext(), "Bir Hata Oluştu", Toast.LENGTH_SHORT)
                        .show()
                }

                _binding.image1.setImageBitmap(imageBitmap)
            }
        }
    }


}