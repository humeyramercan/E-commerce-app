package com.humeyramercan.e_commerceapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.humeyramercan.e_commerceapp.R
import com.humeyramercan.e_commerceapp.databinding.FragmentSignInBinding
import com.humeyramercan.e_commerceapp.util.validEmail
import com.humeyramercan.e_commerceapp.util.validNameOrSurname
import com.humeyramercan.e_commerceapp.util.validPassword
import com.humeyramercan.e_commerceapp.util.validPhone
import com.humeyramercan.e_commerceapp.viewmodel.SignInViewModel


class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var signInViewModel: SignInViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        with(binding) {

            signInViewModel.checkCurrentUser()  //If the user has logged in before, we must redirect her/him to the home page.
            observeCurrentUserResult()

                signInButton.setOnClickListener {
                    val emailResult = emailText.validEmail()   //validation
                    val passwordResult = passwordText.validPassword()

                    if (emailResult && passwordResult) {
                        val email = emailText.text.toString()
                        val password = passwordText.text.toString()

                        signInViewModel.signIn(email, password)
                        observeAuthResult()
                    }
                }

            forgotPasswordText.setOnClickListener {
                val action = SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment()
                findNavController().navigate(action)
            }

            haveAccountText.setOnClickListener {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
        }

    }

    private fun observeAuthResult(){
        signInViewModel.authResult.observe(viewLifecycleOwner, Observer {
            if (it.result) {
                val action =
                    SignInFragmentDirections.actionSignInFragmentToProductsGraph()
                findNavController().navigate(action)

            } else {
                Toast.makeText(context, it.errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun observeCurrentUserResult(){
        signInViewModel.currentUserResult.observe(viewLifecycleOwner, Observer {
            if(it){
                val action =
                    SignInFragmentDirections.actionSignInFragmentToProductsGraph()
                findNavController().navigate(action)
            }
        })
    }

}