package com.humeyramercan.e_commerceapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.humeyramercan.e_commerceapp.model.AuthResultModel
import com.humeyramercan.e_commerceapp.model.UserModel

class AuthenticationRepository {

    private var _authResult= MutableLiveData<AuthResultModel>()
    val authResult:MutableLiveData<AuthResultModel>
        get()=_authResult

    private var _currentUserResult= MutableLiveData<Boolean>()
    val currentUserResult:MutableLiveData<Boolean>
        get()=_currentUserResult

    private var _userModel= MutableLiveData<UserModel>()
    val userModel: MutableLiveData<UserModel>
        get() = _userModel

    private var auth=Firebase.auth
    private var fireStore=Firebase.firestore

    //Value assigned to authResult according to result of signUp process.
    fun signUp(email:String,password:String,name:String,surname:String,phoneNumber:String){
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            it?.let {
                val currentUser=auth.currentUser
                currentUser.let {
                    val userMap=HashMap<String,String>()
                    userMap.put("name",name)
                    userMap.put("surname",surname)
                    userMap.put("phoneNumber",phoneNumber)

                    fireStore.collection("users").document(currentUser?.uid.orEmpty()).set(userMap).addOnSuccessListener {
                        _authResult.value= AuthResultModel(true,"")
                    }.addOnFailureListener {
                        _authResult.value=AuthResultModel(false,it.message.toString())
                    }
                }

            }

        }.addOnFailureListener {
            _authResult.value=AuthResultModel(false,it.message.toString())
        }
    }

    //Value assigned to authResult according to result of signIn process.
    fun signIn(email:String,password:String){
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            it?.let {
                _authResult.value= AuthResultModel(true,"")
            }
        }.addOnFailureListener {
            _authResult.value=AuthResultModel(false,it.message.toString())
        }
    }

    //Current user control
    fun checkCurrentUser(){
        _currentUserResult.value=false
        auth.currentUser?.let {
            _currentUserResult.value = true

        }
    }

    //Reset Password
    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            _authResult.value= AuthResultModel(true,"")
        }.addOnFailureListener {
            _authResult.value= AuthResultModel(false,it.message.toString())
        }

    }
    fun getProfileInfo(){
        auth.currentUser?.let {user->
            fireStore.collection("users").document(user.uid).addSnapshotListener { value, error ->
                value?.let {
                    _userModel.value=UserModel(
                        user.email.orEmpty(),
                        it.get("name") as String,
                        it.get("surname") as String,
                        it.get("phoneNumber") as String,
                    )
                }
            }

        }

    }


    fun update(email: String,password: String,name: String,surname: String,phoneNumber: String){
        val currentUser=auth.currentUser
        currentUser?.let {

          if(password.isNotBlank() && email.isNotBlank()){
              it.updateEmail(email)
                  .addOnCompleteListener { task ->
                      if (task.isSuccessful) {
                          Log.d("Email updated", "User email address updated.")
                      }
                  }
              it.updatePassword(password)
                  .addOnCompleteListener { task ->
                      if (task.isSuccessful) {
                          Log.d("Password updated", "User password updated.")
                      }
                  }
          }

            fireStore.collection("users").document(it.uid)
                .update(
                    "name", name,
                    "surname", surname,
                    "phoneNumber", phoneNumber
                ).addOnSuccessListener {
                    _authResult.value= AuthResultModel(true,"")
                }.addOnFailureListener {
                    _authResult.value=AuthResultModel(false,it.message.toString())

                }
            }
    }

    fun signOut(){
        auth.signOut()
    }

}
