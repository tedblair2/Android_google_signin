package com.example.googlesignin.repository

import android.content.Intent
import android.content.IntentSender
import com.example.googlesignin.model.SignInResult
import com.example.googlesignin.model.User

interface GoogleSignInRepository {
    suspend fun signIn():IntentSender?
    suspend fun signInWithIntent(intent: Intent):SignInResult
    suspend fun signOut()
    fun getCurrentUser():User?
}