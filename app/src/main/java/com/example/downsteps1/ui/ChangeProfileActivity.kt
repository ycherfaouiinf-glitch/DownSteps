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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ChangeProfileActivity : BaseActivity() {

    // 1. تعريف المتغيرات (تأكد من إضافة etConfirmPassword هنا)
    private lateinit var etParentName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etConfirmPassword: EditText // هذا ما كان ينقصك في الصورة
    private lateinit var btnSaveProfile: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_change_profil)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.changeProfilePage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        loadParentData()

        findViewById<LinearLayout>(R.id.backContainer).setOnClickListener {
            finish()
        }

        btnSaveProfile.setOnClickListener {
            updateProfileWithReAuth()
        }
    }

    private fun initializeViews() {
        etParentName = findViewById(R.id.etParentName)
        etEmail = findViewById(R.id.etEmail)
        etConfirmPassword = findViewById(R.id.etConfirmPassword) // ربط الحقل بالـ XML
        btnSaveProfile = findViewById(R.id.btnSaveProfile)
    }

    private fun loadParentData() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            etEmail.setText(user.email)
            FirebaseFirestore.getInstance().collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        etParentName.setText(document.getString("name"))
                    }
                }
        }
    }

    private fun updateProfileWithReAuth() {
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        val newName = etParentName.text.toString().trim()
        val newEmail = etEmail.text.toString().trim()
        val currentPassword = etConfirmPassword.text.toString().trim()

        if (user != null) {
            // 1. الفحص: هل المستخدم مسجل عبر جوجل؟ [cite: 688, 898]
            val isGoogleUser = user.providerData.any { it.providerId == "google.com" }

            if (isGoogleUser) {
                // تنبيه مستخدم جوجل إذا حاول تغيير الإيميل[cite: 688, 899]
                if (newEmail != user.email) {
                    Toast.makeText(this, "Google accounts cannot change email from here. Please manage it in your Google settings.", Toast.LENGTH_LONG).show()
                    return // توقف عن التنفيذ
                }

                // إذا كان مستخدم جوجل ويريد تغيير الاسم فقط، نسمح له بدون كلمة سر
                updateOnlyName(user.uid, newName)
                return
            }

            // 2. إذا كان مستخدم "إيميل وكلمة سر" (المسار الطبيعي لإعادة المصادقة)
            if (newName.isEmpty() || newEmail.isEmpty() || currentPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields and enter password to confirm", Toast.LENGTH_SHORT).show()
                return
            }

            btnSaveProfile.isEnabled = false
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

            user.reauthenticate(credential).addOnCompleteListener { reAuthTask ->
                if (reAuthTask.isSuccessful) {
                    user.updateEmail(newEmail).addOnCompleteListener { emailTask ->
                        if (emailTask.isSuccessful) {
                            updateFirestoreData(user.uid, newName, newEmail)
                        } else {
                            btnSaveProfile.isEnabled = true
                            Toast.makeText(this, "Email error: ${emailTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    btnSaveProfile.isEnabled = true
                    Toast.makeText(this, "Incorrect password!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // دالة مساعدة لتحديث الاسم فقط (لمستخدمي جوجل)
    private fun updateOnlyName(uid: String, newName: String) {
        FirebaseFirestore.getInstance().collection("users").document(uid)
            .set(hashMapOf("name" to newName), SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Name updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    // دالة مساعدة لتحديث الاسم والإيميل في Firestore
    private fun updateFirestoreData(uid: String, name: String, email: String) {
        val updates = hashMapOf("name" to name, "email" to email)
        FirebaseFirestore.getInstance().collection("users").document(uid)
            .set(updates, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}