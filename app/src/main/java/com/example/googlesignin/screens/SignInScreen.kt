package com.example.googlesignin.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.googlesignin.model.SignInState
import com.example.googlesignin.model.User

@Composable
fun SignInScreen(
    signInState: SignInState,
    onSignIn:()->Unit={}
) {
    val context= LocalContext.current
    LaunchedEffect(key1 = signInState.signInError){
        signInState.signInError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), contentAlignment = Alignment.Center){
        Button(onClick = { onSignIn() }) {
            Text(text = "Sign In", fontSize = 18.sp)
        }
    }
}


@Composable
fun HomeScreen(user: User?,onSignOut:()->Unit={}) {

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        user?.profileUrl?.let {url->
            AsyncImage(model = url,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape))
        }
        Spacer(modifier = Modifier.height(16.dp))
        user?.name?.let { name->
            Text(text = name, fontSize = 23.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onSignOut() }) {
            Text(text = "Sign out", fontSize = 18.sp)
        }
    }
}