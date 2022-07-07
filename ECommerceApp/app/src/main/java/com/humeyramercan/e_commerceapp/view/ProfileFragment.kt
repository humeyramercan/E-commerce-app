package com.humeyramercan.e_commerceapp.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.humeyramercan.e_commerceapp.databinding.FragmentProfileBinding
import com.humeyramercan.e_commerceapp.util.*
import com.humeyramercan.e_commerceapp.viewmodel.ProfileViewModel


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        with(binding) {

            profileViewModel.getProfileInfo()
            observeUserModel()

            updateButton.setOnClickListener {

                val emailResult = emailText.validEmail()
                val passwordResult = passwordText.validPasswordForUpdate()
                val nameResult = nameText.validNameOrSurname()
                val surnameResult = surnameText.validNameOrSurname()
                val phoneNumberResult = phoneNumberText.validPhone()

                if (emailResult && passwordResult && nameResult && surnameResult && phoneNumberResult) {
                    val email = emailText.text.toString()
                    val password = passwordText.text.toString()
                    val name = nameText.text.toString()
                    val surname = surnameText.text.toString()
                    val phoneNumber = phoneNumberText.text.toString()

                    profileViewModel.update(email, password, name, surname, phoneNumber)
                    observeAuthResult()
                }
            }
            signOutButton.setOnClickListener {
               profileViewModel.signOut()
                val intent=Intent(requireActivity(),MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    private fun observeUserModel(){
       with(binding){
           profileViewModel.userModel.observe(viewLifecycleOwner, Observer {
               emailText.setText(it.email)
               nameText.setText(it.name)
               surnameText.setText(it.surname)
               phoneNumberText.setText(it.phoneNumber)
               userNameText.setText(it.name+" "+it.surname)
           })
       }
    }

    private fun observeAuthResult(){
        profileViewModel.authResult.observe(viewLifecycleOwner, Observer {
            if (it.result) {
                Toast.makeText(
                    context,
                    "Your profile information has been updated.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(context, it.errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

}