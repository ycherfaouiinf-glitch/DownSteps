package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.downsteps1.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class VerifyCodeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_code)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val etCode = findViewById<TextInputEditText>(R.id.etVerificationCode)
        val btnVerify = findViewById<MaterialButton>(R.id.btnVerifyCode)
        val tvResendCode = findViewById<TextView>(R.id.tvResendCode)

        btnBack.setOnClickListener {
            finish()
        }

        btnVerify.setOnClickListener {
            val code = etCode.text.toString().trim()

            if (code.isEmpty()) {
                etCode.error = getString(R.string.enter_code)
                return@setOnClickListener
            }

            if (code.length < 4) {
                etCode.error = getString(R.string.code_too_short)
                return@setOnClickListener
            }

            startActivity(Intent(this, NewPasswordActivity::class.java))
        }

        tvResendCode.setOnClickListener {
            Toast.makeText(
                this,
                getString(R.string.verification_code_sent_again),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}