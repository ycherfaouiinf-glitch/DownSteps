package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.Calendar

class EditChildActivity : BaseActivity() {

    private lateinit var etChildName: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var btnSaveChildInfo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_child)

        // إعداد القائمة السفلية
        BottomNavHelper.setup(this, "profile")

        initializeViews()
        loadCurrentChildData() // جلب البيانات الحالية لعرضها قبل التعديل

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }
        etBirthDate.setOnClickListener { showDatePicker(etBirthDate) }

        btnSaveChildInfo.setOnClickListener {
            updateChildDataInFirestore()
        }
    }

    private fun initializeViews() {
        etChildName = findViewById(R.id.etChildName)
        etBirthDate = findViewById(R.id.etBirthDate)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        btnSaveChildInfo = findViewById(R.id.btnSaveChildInfo)
    }

    // دالة لجلب البيانات الحالية من Firestore ووضعها في الحقول
    private fun loadCurrentChildData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        etChildName.setText(document.getString("childName"))
                        etBirthDate.setText(document.getString("birthDate"))
                        val gender = document.getString("gender")
                        if (gender == "Male") rbMale.isChecked = true
                        else if (gender == "Female") rbFemale.isChecked = true
                    }
                }
        }
    }

    // دالة تحديث البيانات في Firestore
    private fun updateChildDataInFirestore() {
        val name = etChildName.text.toString().trim()
        val date = etBirthDate.text.toString().trim()
        val gender = if (rbMale.isChecked) "Male" else if (rbFemale.isChecked) "Female" else ""

        if (name.isEmpty() || date.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            btnSaveChildInfo.isEnabled = false

            val updatedData = hashMapOf(
                "childName" to name,
                "birthDate" to date,
                "gender" to gender
            )

            FirebaseFirestore.getInstance().collection("users").document(userId)
                .set(updatedData, SetOptions.merge()) // [cite: 90, 131, 318]
                .addOnSuccessListener {
                    Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    btnSaveChildInfo.isEnabled = true
                    Toast.makeText(this, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, y, m, d ->
            val date = String.format("%02d/%02d/%04d", d, m + 1, y)
            editText.setText(date)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}