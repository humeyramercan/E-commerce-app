package com.humeyramercan.e_commerceapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humeyramercan.e_commerceapp.model.AuthResultModel
import com.humeyramercan.e_commerceapp.model.UserModel
import com.humeyramercan.e_commerceapp.repository.AuthenticationRepository

class ProfileViewModel:ViewModel() {

    private var _authResult= MutableLiveData<AuthResultModel>()
    val authResult:MutableLiveData<AuthResultModel>
        get()=_authResult

    private var _userModel= MutableLiveData<UserModel>()
    val userModel: MutableLiveData<UserModel>
        get() = _userModel

    private val authenticationRepository = AuthenticationRepository()


    fun getProfileInfo(){
        authenticationRepository.getProfileInfo()
        _userModel=authenticationRepository.userModel
    }


    fun update(email: String,password: String,name: String,surname: String,phoneNumber: String) {
        authenticationRepository.update(email,password,name,surname,phoneNumber)
        _authResult=authenticationRepository.authResult
    }


    fun signOut(){
        authenticationRepository.signOut()
    }

}