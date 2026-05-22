package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.downsteps1.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordActivity : BaseActivity() {

    private lateinit var etCurrentPass: EditText
    private lateinit var etNewPass: EditText
    private lateinit var etConfirmNewPass: EditText
    private lateinit var btnSavePassword: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_change_password)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.changePasswordPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()

        findViewById<LinearLayout>(R.id.backContainer).setOnClickListener {
            finish()
        }

        btnSavePassword.setOnClickListener {
            handleChangePassword()
        }
    }

    private fun initializeViews() {
        etCurrentPass = findViewById(R.id.etCurrentPassword)
        etNewPass = findViewById(R.id.etNewPassword)
        etConfirmNewPass = findViewById(R.id.etConfirmNewPassword)
        btnSavePassword = findViewById(R.id.btnSavePassword)
    }

    private fun handleChangePassword() {
        val user = FirebaseAuth.getInstance().currentUser
        val currentPass = etCurrentPass.text.toString().trim()
        val newPass = etNewPass.text.toString().trim()
        val confirmPass = etConfirmNewPass.text.toString().trim()

        // 1. التحقق من أن المستخدم ليس مسجل عبر جوجل
        val isGoogleUser = user?.providerData?.any { it.providerId == "google.com" } ?: false
        if (isGoogleUser) {
            Toast.makeText(this, getString(R.string.google_password_settings), Toast.LENGTH_LONG).show()
            return
        }

        // 2. التحقق من تعبئة الحقول
        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
            return
        }

        // 3. التحقق من تطابق كلمة السر الجديدة
        if (newPass != confirmPass) {
            etConfirmNewPass.error = getString(R.string.passwords_do_not_match)
            return
        }

        if (newPass.length < 6) {
            etNewPass.error = getString(R.string.password_min_length)
            return
        }

        if (user != null && user.email != null) {
            btnSavePassword.isEnabled = false

            // 4. إعادة المصادقة أولاً (Re-authentication) لضمان الأمان
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPass)

            user.reauthenticate(credential).addOnCompleteListener { reAuthTask ->
                if (reAuthTask.isSuccessful) {
                    // 5. تحديث كلمة السر في Firebase Auth
                    user.updatePassword(newPass).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(this, getString(R.string.password_updated_successfully), Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            btnSavePassword.isEnabled = true
                            Toast.makeText(
                                this,
                                getString(R.string.error_message, updateTask.exception?.message ?: ""),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    btnSavePassword.isEnabled = true
                    etCurrentPass.error = getString(R.string.incorrect_current_password)
                    Toast.makeText(this, getString(R.string.current_password_wrong), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}