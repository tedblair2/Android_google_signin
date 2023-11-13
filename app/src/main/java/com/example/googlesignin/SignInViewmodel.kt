package com.example.googlesignin

import androidx.lifecycle.ViewModel
import com.example.googlesignin.model.SignInResult
import com.example.googlesignin.model.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewmodel:ViewModel() {

    private val _signInState= MutableStateFlow(SignInState())
    val signInState=_signInState.asStateFlow()

    fun onSignIn(result: SignInResult){
        _signInState.update {
            it.copy(
                isSignInSuccessful = result.user != null,
                signInError = result.error
            )
        }
    }

    fun resetState(){
        _signInState.update {
            SignInState()
        }
    }
}