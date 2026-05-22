package com.example.downsteps1.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.downsteps1.R
import com.example.downsteps1.common.ui.BaseActivity
import com.google.android.material.button.MaterialButton
import java.util.Locale

class LanguageActivity : BaseActivity() {

    private lateinit var radioGroupLanguage: RadioGroup
    private lateinit var rbEnglish: RadioButton
    private lateinit var rbArabic: RadioButton
    private lateinit var btnSaveLanguage: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_language)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.languagePage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        radioGroupLanguage = findViewById(R.id.radioGroupLanguage)
        rbEnglish = findViewById(R.id.rbEnglish)
        rbArabic = findViewById(R.id.rbArabic)
        btnSaveLanguage = findViewById(R.id.btnSaveLanguage)

        val currentLanguage = getSharedPreferences("settings_prefs", MODE_PRIVATE)
            .getString("app_language", "en")

        if (currentLanguage == "ar") {
            rbArabic.isChecked = true
        } else {
            rbEnglish.isChecked = true
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnSaveLanguage.setOnClickListener {
            val selectedLanguage = if (rbArabic.isChecked) "ar" else "en"

            getSharedPreferences("settings_prefs", MODE_PRIVATE)
                .edit()
                .putString("app_language", selectedLanguage)
                .apply()

            setLocale(selectedLanguage)

            Toast.makeText(this, "Language saved", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }
}