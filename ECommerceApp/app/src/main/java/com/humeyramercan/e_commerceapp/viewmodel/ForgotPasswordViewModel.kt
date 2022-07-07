package com.humeyramercan.e_commerceapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humeyramercan.e_commerceapp.model.AuthResultModel
import com.humeyramercan.e_commerceapp.repository.AuthenticationRepository

class ForgotPasswordViewModel:ViewModel() {
    private var _authResult= MutableLiveData<AuthResultModel>()
    val authResult:MutableLiveData<AuthResultModel>
        get()=_authResult

    private var authenticationRepository=AuthenticationRepository()

    fun resetPassword(email:String){
        authenticationRepository.resetPassword(email)
        _authResult=authenticationRepository.authResult
    }

}