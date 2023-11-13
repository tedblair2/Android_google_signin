package com.example.googlesignin.model

data class SignInState(
    val isSignInSuccessful:Boolean=false,
    val signInError:String?=null
)
