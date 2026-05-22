package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.downsteps1.R
import com.example.downsteps1.ui.VerifyCodeActivity
import com.example.downsteps1.data.AuthRepository
import com.example.downsteps1.data.remote.FirebaseAuthRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ResetPasswordActivity : BaseActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var etResetEmail: TextInputEditText
    private lateinit var btnSendReset: MaterialButton

    private val authRepository: AuthRepository = FirebaseAuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)
        etResetEmail = findViewById(R.id.etResetEmail)
        btnSendReset = findViewById(R.id.btnSendReset)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnSendReset.setOnClickListener {
            handleResetPassword()
        }
    }

    private fun handleResetPassword() {
        val email = etResetEmail.text?.toString()?.trim().orEmpty()

        if (email.isEmpty()) {
            etResetEmail.error = getString(R.string.email_required)
            return
        }

        btnSendReset.isEnabled = false

        authRepository.sendResetCode(email) { isSuccess, errorMessage ->
            btnSendReset.isEnabled = true
            if (isSuccess) {

                Toast.makeText(
                    this,
                    getString(R.string.reset_link_sent),
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            } else {
                etResetEmail.error = errorMessage ?: getString(R.string.reset_email_failed)
            }
        }
    }
}