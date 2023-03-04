package com.stear.mahsul.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
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
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.stear.mahsul.R
import com.stear.mahsul.databinding.FragmentProfileBinding
import com.stear.mahsul.repository.Repository
import com.stear.mahsul.utils.Util
import com.stear.mahsul.viewmodel.ProfileFragmentViewModel
import com.stear.mahsul.viewmodel.ProfileFragmentViewModelFactory
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var _binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileFragmentViewModel
    private var imageBitmap: Bitmap? = null
    private var selectedImageUri: Uri? = null
    private val auth = Firebase.auth
    private val storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        //Mvvm
        val repo: Repository by lazy { Repository() }
        val viewModelFactory by lazy { ProfileFragmentViewModelFactory(repo) }

        //send repository to viewmodel
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[ProfileFragmentViewModel::class.java]
        _binding.progressBar.visibility = View.VISIBLE


        viewModel.doesHasUserInfo(auth.currentUser!!.uid)
        viewModel.isUserUpdate.value = 0

        viewModel.isUserUpdate.observe(viewLifecycleOwner){
            if (it == 1){
                Toast.makeText(requireContext(),"Fotoğraf Başarıyla Güncellendi",Toast.LENGTH_SHORT).show()
            }else if (it == 2){
                Toast.makeText(requireContext(),"Kullanıcı Bilgileri Güncellendi",Toast.LENGTH_SHORT).show()
            }else if (it == 3){
                Toast.makeText(requireContext(),"Bir Sorun Oluştu",Toast.LENGTH_SHORT).show()
                _binding.progressBar.visibility = View.GONE
            }
        }


        viewModel.userInfo.observe(viewLifecycleOwner){
            _binding.eMailAdressText.text = auth.currentUser!!.email.toString()
            if (Util.isNetworkAvailable(requireContext())){
                if (it.photoUrl != "" ) Picasso.get().load(it.photoUrl).into(_binding.profileImage)
                _binding.userNameText.setText(it.userName)
                _binding.userPhoneText.setText(it.userPhone)
                object : CountDownTimer(2000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        _binding.progressBar.visibility = View.GONE
                    }
                }.start()
            }else{
                Toast.makeText(requireContext(),"İnternet Bağlantısını Kontrol Ediniz",Toast.LENGTH_SHORT).show()
                _binding.progressBar.visibility = View.GONE
            }


        }

        _binding.updateButton.setOnClickListener() {
            val userName = _binding.userNameText.text.toString()
            val userPhone = _binding.userPhoneText.text.toString().trim()
            _binding.progressBar.visibility = View.VISIBLE
            if (userName != "" && userPhone.length >= 10 && Util.isValidNumber(userPhone)) {

                viewModel.userUpdate(
                    userName,
                    userPhone,
                    auth.currentUser!!.uid
                )


            } else {
                Util.makeAlerDialog(
                    requireContext(),
                    "Lütfen Telefonu ve Adınızı Doğru Giriniz",
                    "Hata",
                    R.drawable.titleicon
                )
            }


        }


        _binding.exitButton.setOnClickListener() {
            if (Util.isNetworkAvailable(requireContext())) {
                auth.signOut()
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
        _binding.removeAdButton.setOnClickListener() {


        }

        _binding.profileImage.setOnClickListener() {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }


        return _binding.root
    }

    // this function is triggered when
    // the Select Image Button is clicked
    private fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        launchSomeActivity.launch(i)
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
                        viewModel.userPhotoUpdate(uri.toString(),auth.currentUser!!.uid)
                        _binding.progressBar.visibility = View.VISIBLE
                    }

                }.addOnFailureListener() {
                    Toast.makeText(requireContext(), "Bir Hata Oluştu", Toast.LENGTH_SHORT)
                        .show()
                }

                _binding.profileImage.setImageBitmap(imageBitmap)
            }
        }
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



}