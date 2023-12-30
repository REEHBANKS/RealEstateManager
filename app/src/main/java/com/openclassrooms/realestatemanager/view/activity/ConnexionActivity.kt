package com.openclassrooms.realestatemanager.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.viewmodel.AgentViewModel
import com.openclassrooms.realestatemanager.viewmodel.PropertyDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnexionActivity : AppCompatActivity() {
    private val viewModel: AgentViewModel by viewModels()

    private val RC_SIGN_IN = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)

        if (FirebaseAuth.getInstance().currentUser != null) {
            // L'utilisateur est déjà connecté, lancer l'activité principale directement
            startMainActivity()
        }

        findViewById<View>(R.id.buttonSignIn).setOnClickListener {
            startEmailConnexionInActivity(false)
        }

        findViewById<View>(R.id.buttonSignUp).setOnClickListener {
            startEmailConnexionInActivity(true)
        }

        findViewById<TextView>(R.id.textViewForgotPassword).setOnClickListener {
            showResetPasswordDialog()
        }

    }

    private fun showResetPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.forgot_password))

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        builder.setView(input)

        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            val email = input.text.toString()
            sendPasswordResetEmail(email)
            dialog.dismiss()
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }

        builder.show()
    }


    private fun startEmailConnexionInActivity(isSignUp: Boolean) {
        val providers = listOf(AuthUI.IdpConfig.EmailBuilder().build())

        val builder = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setTheme(R.style.LoginTheme)
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false, true)
            .setLogo(R.drawable.clipart1519255)

        if (isSignUp) {
            builder.setAlwaysShowSignInMethodScreen(true)
        }

        startActivityForResult(builder.build(), RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleResponseAfterSignIn(requestCode, resultCode, data)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun handleResponseAfterSignIn(requestCode: Int, resultCode: Int, data: Intent?) {
        val response = IdpResponse.fromResultIntent(data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                //userViewModel.createUser() // Assurez-vous que cette méthode est définie dans votre ViewModel
                viewModel.createUser()
                showSnackBar(getString(R.string.connection_succeed))
                startMainActivity()
            } else {
                response?.error?.let { error ->
                    when (error.errorCode) {
                        ErrorCodes.NO_NETWORK -> showSnackBar(getString(R.string.error_no_internet))
                        ErrorCodes.UNKNOWN_ERROR -> showSnackBar(getString(R.string.error_unknown_error))
                        else -> showSnackBar(getString(R.string.error_authentication_failed))
                    }
                } ?: showSnackBar(getString(R.string.error_authentication_canceled))
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun sendPasswordResetEmail(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showSnackBar(getString(R.string.reset_password_email_sent))
                } else {
                    showSnackBar(getString(R.string.error_sending_reset_email))
                }
            }
    }


}