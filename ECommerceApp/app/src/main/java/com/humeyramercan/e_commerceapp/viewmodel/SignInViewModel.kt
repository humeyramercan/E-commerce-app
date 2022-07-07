package com.humeyramercan.e_commerceapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humeyramercan.e_commerceapp.model.AuthResultModel
import com.humeyramercan.e_commerceapp.repository.AuthenticationRepository

class SignInViewModel:ViewModel() {
    private var _authResult= MutableLiveData<AuthResultModel>()
    val authResult:MutableLiveData<AuthResultModel>
        get()=_authResult

    private var _currentUserResult=MutableLiveData<Boolean>()
    val currentUserResult:MutableLiveData<Boolean>
        get()=_currentUserResult

    private val authenticationRepository=AuthenticationRepository()

    fun signIn(email:String,password:String){
        authenticationRepository.signIn(email,password)
        _authResult=authenticationRepository.authResult
    }

    fun checkCurrentUser(){
        authenticationRepository.checkCurrentUser()
        _currentUserResult=authenticationRepository.currentUserResult
    }



}