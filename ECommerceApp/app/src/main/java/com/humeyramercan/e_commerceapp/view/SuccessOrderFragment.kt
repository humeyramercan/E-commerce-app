package com.humeyramercan.e_commerceapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.humeyramercan.e_commerceapp.R
import com.humeyramercan.e_commerceapp.databinding.FragmentSuccessOrderBinding


class SuccessOrderFragment : Fragment() {
    private lateinit var binding:FragmentSuccessOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSuccessOrderBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.successButton.setOnClickListener {
            val action=SuccessOrderFragmentDirections.actionSuccessOrderFragmentToHomePage()
            findNavController().navigate(action)
        }
    }

}