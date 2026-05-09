package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.downsteps1.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class NewPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val etNewPassword = findViewById<TextInputEditText>(R.id.etNewPassword)
        val etConfirmPassword = findViewById<TextInputEditText>(R.id.etConfirmPassword)
        val btnSavePassword = findViewById<MaterialButton>(R.id.btnSavePassword)

        btnBack.setOnClickListener {
            finish()
        }

        btnSavePassword.setOnClickListener {
            val newPassword = etNewPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            when {
                newPassword.isEmpty() -> {
                    etNewPassword.error = "Enter new password"
                }

                newPassword.length < 6 -> {
                    etNewPassword.error = "Password must be at least 6 characters"
                }

                confirmPassword.isEmpty() -> {
                    etConfirmPassword.error = "Confirm your password"
                }

                newPassword != confirmPassword -> {
                    etConfirmPassword.error = "Passwords do not match"
                }

                else -> {
                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }
            }
        }
    }
}