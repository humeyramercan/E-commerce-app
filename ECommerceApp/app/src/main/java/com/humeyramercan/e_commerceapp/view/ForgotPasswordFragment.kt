package com.humeyramercan.e_commerceapp.view

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.humeyramercan.e_commerceapp.R
import com.humeyramercan.e_commerceapp.databinding.FragmentForgotPasswordBinding
import com.humeyramercan.e_commerceapp.util.validEmail
import com.humeyramercan.e_commerceapp.viewmodel.ForgotPasswordViewModel


class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forgotPasswordViewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)

        with(binding) {
            resetPasswordButton.setOnClickListener {
                val emailResult = emailText.validEmail()
                if (emailResult) {
                    val email = emailText.text.toString()
                    forgotPasswordViewModel.resetPassword(email)
                    authResultObserve()
                }
            }

        }
    }

    private fun authResultObserve() {
        forgotPasswordViewModel.authResult.observe(viewLifecycleOwner, Observer {
                if (it.result) {
                    Toast.makeText(context, "Email sent.", Toast.LENGTH_LONG).show()
                    val action =
                        ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSignInFragment()
                    findNavController().navigate(action)
                }else{
                    Toast.makeText(context, it.errorMessage, Toast.LENGTH_LONG).show()

                }
        })
    }

}