package com.stear.mahsul.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.stear.mahsul.R
import com.stear.mahsul.databinding.FragmentProfileBinding
import com.stear.mahsul.repository.Repository
import com.stear.mahsul.utils.ImageResizer
import com.stear.mahsul.utils.Util
import com.stear.mahsul.viewmodel.ProfileFragmentViewModel
import com.stear.mahsul.viewmodel.ProfileFragmentViewModelFactory
import java.io.IOException


class ProfileFragment : Fragment() {

    private lateinit var _binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileFragmentViewModel
    var imageBitmap: Bitmap? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        //Mvvm
        val repo: Repository by lazy { Repository() }
        val viewModelFactory by lazy { ProfileFragmentViewModelFactory(repo) }

        //send repository to viewmodel
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[ProfileFragmentViewModel::class.java]



        _binding.updateButton.setOnClickListener() {


        }


        _binding.exitButton.setOnClickListener() {
            if (Util.isNetworkAvailable(requireContext())) {
                Firebase.auth.signOut()
                val intent = Intent(requireContext(), SignInActivity::class.java)
                requireActivity().finish()
                startActivity(intent)

            } else {
                Util.makeAlerDialog(
                    requireContext(),
                    "İnternet Bağlantısını Kontrol Ediniz",
                    "Hata",
                    R.drawable.titleicon
                )
            }


        }


        _binding.premiumButton.setOnClickListener() {


        }
        _binding.deleteAcountButton.setOnClickListener() {


        }

        _binding.profileImage.setOnClickListener(){
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }


        return _binding.root
    }

    // this function is triggered when
    // the Select Image Button is clicked
     fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        launchSomeActivity.launch(i)
    }

    var launchSomeActivity = registerForActivityResult<Intent, ActivityResult>(
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
                var selectedImageUri: Uri? = data.data
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().getContentResolver(),
                        selectedImageUri
                    )
                    imageBitmap = ImageResizer.reduceBitmapSize(imageBitmap!!,921600)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                _binding.profileImage.setImageBitmap(imageBitmap)
            }
        }
    }
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                imageChooser()
            } else {
                Toast.makeText(requireContext(),"Fotoğraf Yüklemek İsterseniz Ayarlardan Bellek İznini veriniz",Toast.LENGTH_SHORT).show()
            }
        }










}