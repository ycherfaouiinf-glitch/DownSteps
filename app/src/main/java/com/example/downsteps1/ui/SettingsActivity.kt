package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.content.SharedPreferences
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.ui.SosActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : BaseActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        BottomNavHelper.setup(this, "settings")
        prefs = getSharedPreferences("settings_prefs", MODE_PRIVATE)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        // Rows
        val rowChangeProfile = findViewById<LinearLayout>(R.id.rowChangeProfile)
        val rowChangePassword = findViewById<LinearLayout>(R.id.rowChangePassword)
        val rowLanguage = findViewById<LinearLayout>(R.id.rowLanguage)
        val rowThemeMode = findViewById<LinearLayout>(R.id.rowThemeMode)
        val rowHelpSupport = findViewById<LinearLayout>(R.id.rowHelpSupport)
        val rowAboutApp = findViewById<LinearLayout>(R.id.rowAboutApp)

        // Texts
        val tvCurrentLanguage = findViewById<TextView>(R.id.tvCurrentLanguage)
        val tvCurrentMode = findViewById<TextView>(R.id.tvCurrentMode)

        // Switches
        val switchDarkMode = findViewById<SwitchCompat>(R.id.switchDarkMode)
        val switchReminder = findViewById<SwitchCompat>(R.id.switchReminder)
        val switchGeneralNotif = findViewById<SwitchCompat>(R.id.switchGeneralNotif)

        // Button
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        tvCurrentLanguage.text = "English"
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        switchDarkMode.isChecked = isDarkMode
        tvCurrentMode.text = if (isDarkMode) "Dark mode" else "Light mode"



        rowChangeProfile.setOnClickListener {
            startActivity(Intent(this, ChangeProfileActivity::class.java))
        }

        rowChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        rowLanguage.setOnClickListener {
            startActivity(Intent(this, LanguageActivity::class.java))
        }

        rowThemeMode.setOnClickListener {
            switchDarkMode.toggle()
        }

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()
            tvCurrentMode.text = if (isChecked) "Dark mode" else "Light mode"
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Daily reminders enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Daily reminders disabled", Toast.LENGTH_SHORT).show()
            }
        }

        switchGeneralNotif.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "General notifications enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "General notifications disabled", Toast.LENGTH_SHORT).show()
            }
        }

        rowHelpSupport.setOnClickListener {
            startActivity(Intent(this, HelpSupportActivity::class.java))
        }

        rowAboutApp.setOnClickListener {
            startActivity(Intent(this, AboutAppActivity::class.java))
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}