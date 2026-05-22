package com.example.downsteps1.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.downsteps1.R
import com.example.downsteps1.common.ui.BaseActivity
import com.example.downsteps1.data.AuthRepository
import com.example.downsteps1.data.remote.FirebaseAuthRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SignupActivity : BaseActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnCreate: MaterialButton
    private lateinit var btnGoLogin: MaterialButton

    private val authRepository: AuthRepository = FirebaseAuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        ivBack = findViewById(R.id.ivBack)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmailSignup)
        etPassword = findViewById(R.id.etPasswordSignup)
        cbTerms = findViewById(R.id.cbTerms)
        btnCreate = findViewById(R.id.btnCreate)
        btnGoLogin = findViewById(R.id.btnGoLogin)

        cbTerms.buttonTintList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(this, R.color.btn_primary),
                ContextCompat.getColor(this, R.color.input_icon)
            )
        )

        ivBack.imageTintList = ColorStateList.valueOf(
            ContextCompat.getColor(this, R.color.btn_primary)
        )
    }

    private fun setupClickListeners() {
        ivBack.setOnClickListener {
            finish()
        }

        btnGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        cbTerms.setOnClickListener {
            showTermsDialog()
        }

        btnCreate.setOnClickListener {
            handleSignup()
        }
    }

    private fun showTermsDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.terms_conditions))
            .setMessage(getString(R.string.terms_conditions_message))
            .setPositiveButton(getString(R.string.i_agree)) { dialog, _ ->
                cbTerms.isChecked = true
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                cbTerms.isChecked = false
                dialog.dismiss()
            }
            .show()
    }

    private fun handleSignup() {
        val name = etName.text?.toString()?.trim().orEmpty()
        val email = etEmail.text?.toString()?.trim().orEmpty()
        val password = etPassword.text?.toString()?.trim().orEmpty()

        if (name.isEmpty()) {
            etName.error = getString(R.string.name_required)
            return
        }

        if (email.isEmpty()) {
            etEmail.error = getString(R.string.email_required)
            return
        }

        if (password.isEmpty()) {
            etPassword.error = getString(R.string.password_required)
            return
        }

        if (password.length < 6) {
            etPassword.error = getString(R.string.password_min_length)
            return
        }

        if (!cbTerms.isChecked) {
            Toast.makeText(this, getString(R.string.accept_terms), Toast.LENGTH_SHORT).show()
            return
        }

        authRepository.signup(name, email, password) { isSuccess, errorMessage ->
            if (isSuccess) {
                Toast.makeText(this, getString(R.string.account_created_successfully), Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ChildInformationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    errorMessage ?: getString(R.string.signup_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}