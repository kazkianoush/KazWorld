package com.example.kazworld

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {


    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val googleSignIn : Button = findViewById(R.id.googleSignIn)


        googleSignIn.setOnClickListener {
            onClickSignInButton()
        }

    }
    private fun onClickSignInButton() {


        val providers: ArrayList<AuthUI.IdpConfig> = arrayListOf(
            AuthUI.IdpConfig.AnonymousBuilder().build(),
            //                AuthUI.IdpConfig.GitHubBuilder().build(),
            //                AuthUI.IdpConfig.TwitterBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        val signInIntent: Intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLockOrientation(true)
            .build()

        signInLauncher.launch(signInIntent)
    }


    // ----------------------------------------------------------------------------


    private fun onResult(result: FirebaseAuthUIAuthenticationResult) {
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val response = result.idpResponse


                when (response?.providerType) {
                     "anonymous"-> {
                        Log.i("onSignIn anonymous USER", "string")
                    }
                    else -> {
                        when (response?.isNewUser) {
                            true -> {
                                Log.i("onSignIn NEW USER", "string")
                                val user = Firebase.auth.currentUser
                                val displayName =  "welcome " + user?.displayName
                                user?.sendEmailVerification()
                                val signInButton = findViewById<TextView>(R.id.signIn)
                                signInButton.text = displayName
                                signInButton.visibility = View.VISIBLE
                            }
                            else -> {
                                Log.i("onSignIn ALREADY USER", "string")
                                val user = Firebase.auth.currentUser
                                val displayName =  "welcome " + user?.displayName
                                user?.sendEmailVerification()
                                val signInButton = findViewById<TextView>(R.id.signIn)
                                signInButton.text = displayName
                                signInButton.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
            else -> {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.

            }
        }
    }



    //    ----------------------------------------------------------------------------


    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onResult(res)
    }


}
