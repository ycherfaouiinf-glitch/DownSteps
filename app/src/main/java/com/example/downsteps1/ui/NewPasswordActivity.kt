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
                    etNewPassword.error =
                        getString(R.string.enter_new_password)
                }

                newPassword.length < 6 -> {
                    etNewPassword.error =
                        getString(R.string.password_min_length)
                }

                confirmPassword.isEmpty() -> {
                    etConfirmPassword.error =
                        getString(R.string.confirm_your_password)
                }

                newPassword != confirmPassword -> {
                    etConfirmPassword.error =
                        getString(R.string.passwords_do_not_match)
                }

                else -> {
                    Toast.makeText(
                        this,
                        getString(R.string.password_changed_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }
            }
        }
    }
}