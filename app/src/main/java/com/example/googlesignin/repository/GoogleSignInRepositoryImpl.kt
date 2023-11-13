package com.example.googlesignin.repository

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.googlesignin.R
import com.example.googlesignin.model.SignInResult
import com.example.googlesignin.model.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleSignInRepositoryImpl(
    private val context:Context,
    private val oneTapClient:SignInClient) : GoogleSignInRepository {

    private val auth=Firebase.auth

    override suspend fun signIn(): IntentSender? {
        val result=try {
            oneTapClient.beginSignIn(buildSignInRequest()).await()
        }catch (e:Exception){
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    override suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential=oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken=credential.googleIdToken
        val googleCredential=GoogleAuthProvider.getCredential(googleIdToken,null)
        return try {
            val user=auth.signInWithCredential(googleCredential).await().user
            SignInResult(
                user = User(
                    userId = user?.uid,
                    name = user?.displayName,
                    profileUrl = user?.photoUrl.toString()
                ),
                error = null
            )
        }catch (e:Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                user = null,
                error = e.message
            )
        }
    }

    override suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        }catch (e:Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    override fun getCurrentUser(): User? {
        return auth.currentUser?.run {
            User(
                userId = uid,
                name = displayName,
                profileUrl = photoUrl.toString()
            )
        }
    }

    private fun buildSignInRequest():BeginSignInRequest{
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}