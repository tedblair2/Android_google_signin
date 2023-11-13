package com.example.googlesignin.model

data class SignInResult(
    val user:User?,
    val error:String?
)

data class User(
    val userId:String?,
    val name:String?,
    val profileUrl:String?
)
