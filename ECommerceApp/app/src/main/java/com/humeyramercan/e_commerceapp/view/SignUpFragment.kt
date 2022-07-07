package com.humeyramercan.e_commerceapp.view

import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.humeyramercan.e_commerceapp.R
import com.humeyramercan.e_commerceapp.databinding.FragmentSignUpBinding
import com.humeyramercan.e_commerceapp.util.validEmail
import com.humeyramercan.e_commerceapp.util.validNameOrSurname
import com.humeyramercan.e_commerceapp.util.validPassword
import com.humeyramercan.e_commerceapp.util.validPhone
import com.humeyramercan.e_commerceapp.viewmodel.SignUpViewModel
import java.util.regex.Pattern


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        with(binding) {
            signUpButton.setOnClickListener {
                //If the user clicks the button directly, all helper texts must be shown.
                val emailResult = emailText.validEmail()
                val passwordResult = passwordText.validPassword()
                val nameResult = nameText.validNameOrSurname()
                val surnameResult = surnameText.validNameOrSurname()
                val phoneNumberResult = phoneNumberText.validPhone()

                if (emailResult && passwordResult && nameResult && surnameResult && phoneNumberResult) {

                    val email = emailText.text.toString()
                    val password = passwordText.text.toString()
                    val name = nameText.text.toString()
                    val surname = surnameText.text.toString()
                    val phoneNumber = phoneNumberText.text.toString()

                    signUpViewModel.signUp(email, password, name, surname, phoneNumber)
                    observeAuthResult()
                }

            }
        }


    }

    private fun observeAuthResult(){
        signUpViewModel.authResult.observe(viewLifecycleOwner, Observer {
            if (it.result) {
                val action =
                    SignUpFragmentDirections.actionSignUpFragmentToProductsGraph()
                findNavController().navigate(action)
            } else {
                Toast.makeText(context, it.errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
}