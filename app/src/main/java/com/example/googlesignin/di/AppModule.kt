package com.example.googlesignin.di

import com.example.googlesignin.SignInViewmodel
import com.example.googlesignin.repository.GoogleSignInRepository
import com.example.googlesignin.repository.GoogleSignInRepositoryImpl
import com.google.android.gms.auth.api.identity.Identity
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule= module {
    single {
        Identity.getSignInClient(androidContext())
    }
    single<GoogleSignInRepository>{
        GoogleSignInRepositoryImpl(get(),get())
    }
    viewModel { SignInViewmodel() }
}