package com.example.googlesignin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.googlesignin.repository.GoogleSignInRepository
import com.example.googlesignin.screens.HomeScreen
import com.example.googlesignin.screens.SignInScreen
import com.example.googlesignin.ui.theme.GoogleSignInTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val googleSignInRepository by inject<GoogleSignInRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoogleSignInTheme {
                // A surface container using the 'background' color from the theme
                val navController= rememberNavController()
                val viewmodel by viewModel<SignInViewmodel>()

                NavHost(navController = navController, startDestination = "login"){
                    composable(route="login"){
                        val signInState by viewmodel.signInState.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = Unit){
                            if (googleSignInRepository.getCurrentUser() != null){
                                navController.navigate("home")
                            }
                        }
                        val signInLauncher= rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult()){
                            if (it.resultCode== RESULT_OK){
                                lifecycleScope.launch {
                                    val signInResult=googleSignInRepository.signInWithIntent(it.data ?: return@launch)
                                    viewmodel.onSignIn(signInResult)
                                }
                            }
                        }

                        LaunchedEffect(key1 = signInState.isSignInSuccessful){
                            if (signInState.isSignInSuccessful){
                                navController.navigate("home")
                                viewmodel.resetState()
                            }
                        }

                        SignInScreen(signInState = signInState, onSignIn = {
                            lifecycleScope.launch {
                                val singInIntentSender=googleSignInRepository.signIn()
                                signInLauncher.launch(
                                    IntentSenderRequest.Builder(
                                        singInIntentSender ?: return@launch
                                    ).build())
                            }
                        })
                    }
                    composable(route = "home"){
                        val user=googleSignInRepository.getCurrentUser()
                        HomeScreen(user =user, onSignOut = {
                            lifecycleScope.launch {
                                googleSignInRepository.signOut()
                                navController.popBackStack()
                                Toast.makeText(this@MainActivity, "Signed Out", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
        }
    }
}
