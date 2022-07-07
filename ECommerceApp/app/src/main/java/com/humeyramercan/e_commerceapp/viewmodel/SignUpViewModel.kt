package com.humeyramercan.e_commerceapp.viewmodel

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.humeyramercan.e_commerceapp.model.AuthResultModel
import com.humeyramercan.e_commerceapp.repository.AuthenticationRepository
import com.humeyramercan.e_commerceapp.view.SignUpFragmentDirections

class SignUpViewModel : ViewModel() {
    private var _authResult= MutableLiveData<AuthResultModel>()
    val authResult:MutableLiveData<AuthResultModel>
        get()=_authResult

    private val authenticationRepository = AuthenticationRepository()

    fun signUp(
        email: String,
        password: String,
        name: String,
        surname: String,
        phoneNumber: String
    ) {

        authenticationRepository.signUp(email, password, name, surname, phoneNumber)
        _authResult = authenticationRepository.authResult

    }


}