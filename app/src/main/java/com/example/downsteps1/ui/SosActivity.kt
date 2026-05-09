package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper

class SosActivity : BaseActivity() {

    private lateinit var etPrimaryNumber: EditText
    private lateinit var etSecondaryNumber1: EditText
    private lateinit var etSecondaryNumber2: EditText
    private lateinit var etContactName: EditText
    private lateinit var prefs: SharedPreferences

    private lateinit var btnSaveSos: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sos)
        BottomNavHelper.setup(this, "sos")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sosPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prefs = getSharedPreferences("sos_prefs", MODE_PRIVATE)

        // Views
        etPrimaryNumber = findViewById(R.id.etPrimaryNumber)
        etSecondaryNumber1 = findViewById(R.id.etSecondaryNumber1)
        etSecondaryNumber2 = findViewById(R.id.etSecondaryNumber2)
        etContactName = findViewById(R.id.etContactName)

        btnSaveSos = findViewById(R.id.btnSaveSos)



        loadSavedData()

        btnSaveSos.setOnClickListener {
            saveSosData()
        }

    }

    private fun saveSosData() {
        val primary = etPrimaryNumber.text.toString().trim()
        val secondary1 = etSecondaryNumber1.text.toString().trim()
        val secondary2 = etSecondaryNumber2.text.toString().trim()
        val contactName = etContactName.text.toString().trim()

        if (primary.isEmpty()) {
            etPrimaryNumber.error = "Primary number is required"
            etPrimaryNumber.requestFocus()
            return
        }

        prefs.edit()
            .putString("primary_number", primary)
            .putString("secondary_number_1", secondary1)
            .putString("secondary_number_2", secondary2)
            .putString("contact_name", contactName)
            .apply()

        SosWidgetProvider.requestRefresh(this)
        Toast.makeText(this, "SOS contacts saved", Toast.LENGTH_SHORT).show()
    }

    private fun loadSavedData() {
        etPrimaryNumber.setText(prefs.getString("primary_number", ""))
        etSecondaryNumber1.setText(prefs.getString("secondary_number_1", ""))
        etSecondaryNumber2.setText(prefs.getString("secondary_number_2", ""))
        etContactName.setText(prefs.getString("contact_name", ""))
    }

    private fun openDialerWithPrimaryNumber() {
        val primaryNumber = prefs.getString("primary_number", "")?.trim().orEmpty()

        if (primaryNumber.isEmpty()) {
            Toast.makeText(this, "Please save a primary SOS number first", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$primaryNumber")
        }
        startActivity(intent)
    }
}