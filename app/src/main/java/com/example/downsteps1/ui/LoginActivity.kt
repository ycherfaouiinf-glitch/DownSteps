package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.downsteps1.R
import com.example.downsteps1.data.AuthRepository
import com.example.downsteps1.data.remote.FirebaseAuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.gms.auth.api.signin.GoogleSignIn

class LoginActivity : BaseActivity() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnGoSignup: TextView
    private lateinit var tvForgot: TextView
    private lateinit var btnGoogle: MaterialButton

    // تعريف الـ Repository الذي كان مفقوداً في الكود الأخير
    private val authRepository: AuthRepository = FirebaseAuthRepository()

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!

                // الحل هنا: استخراج الـ idToken من الكائن account
                val token = account.idToken

                if (token != null) {
                    // نمرر الـ token الذي استخرجناه الآن
                    authRepository.loginWithGoogle(token) { isSuccess, isNewUser ->
                        if (isSuccess) {
                            if (isNewUser) {
                                startActivity(Intent(this, ChildInformationActivity::class.java))
                            } else {
                                startActivity(Intent(this, HomeActivity::class.java))
                            }
                            finish()
                        } else {
                            showError("Login Failed")
                        }
                    }
                }
            } catch (e: ApiException) {
                showError("Google Sign-In Error: ${e.message}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        setupClickListeners()
    }


    override fun onStart() {
        super.onStart()

        val currentUser = com.google.firebase.auth.FirebaseAuth
            .getInstance()
            .currentUser

        if (currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun initializeViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoSignup = findViewById(R.id.btnGoSignup)
        tvForgot = findViewById(R.id.tvForgot)
        btnGoogle = findViewById(R.id.btnGoogle) // ربط زر جوجل
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            handleLogin()
        }

        btnGoSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        tvForgot.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        btnGoogle.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun handleLogin() {
        val email = etEmail.text?.toString()?.trim().orEmpty()
        val password = etPassword.text?.toString()?.trim().orEmpty()

        if (email.isEmpty()) {
            etEmail.error = getString(R.string.email_required)
            return
        }
        if (password.isEmpty()) {
            etPassword.error = getString(R.string.password_required)
            return
        }

        authRepository.login(email, password) { isSuccess, errorMessage ->
            if (isSuccess) {
                navigateToHome()
            } else {
                showError(errorMessage ?: getString(R.string.login_failed))
            }
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToChildInfo() {
        Toast.makeText(this, getString(R.string.complete_profile), Toast.LENGTH_SHORT).show()

        val intent = Intent(this, ChildInformationActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}