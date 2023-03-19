package com.stear.mahsul.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.stear.mahsul.R
import com.stear.mahsul.adapter.MahsulAdapter
import com.stear.mahsul.databinding.FragmentMahsulListBinding
import com.stear.mahsul.repository.Repository
import com.stear.mahsul.viewmodel.MahsulAddFragmentViewModel
import com.stear.mahsul.viewmodel.MahsulAddFragmentViewModelFactory
import com.stear.mahsul.viewmodel.MahsulListFragmentViewModel
import com.stear.mahsul.viewmodel.MahsulListFragmentViewModelFactory

class MahsulListFragment : Fragment() {

    private lateinit var _binding: FragmentMahsulListBinding
    private lateinit var viewModel: MahsulListFragmentViewModel
    val adap: MahsulAdapter by lazy { MahsulAdapter(requireActivity()) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val repo: Repository by lazy { Repository() }
        val viewModelFactory by lazy { MahsulListFragmentViewModelFactory(repo) }

        //send repository to viewmodel
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MahsulListFragmentViewModel::class.java]

        _binding = FragmentMahsulListBinding.inflate(inflater, container, false)

        _binding.addMahsulButton.setOnClickListener() {
            Navigation.findNavController(it).navigate(R.id.goToMahsulAdd)
        }

        _binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = adap
        }

        viewModel.mahsulList.observe(viewLifecycleOwner){
            adap.setMahsulList(it)
        }

        viewModel.getMahsul()







        return _binding.root
    }


}