package com.example.downsteps1.ui

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.example.downsteps1.R
import com.example.downsteps1.common.ui.BaseActivity
import com.google.android.material.button.MaterialButton

class SosWidgetActivationActivity : BaseActivity() {

    private lateinit var tvActivationState: TextView
    private lateinit var btnActivateWidget: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.activity_sos_widget_activation)

        WindowCompat.getInsetsController(window, window.decorView)
            ?.isAppearanceLightStatusBars = true

        getSharedPreferences("sos_prefs", MODE_PRIVATE)
            .edit()
            .putBoolean("show_sos_activation_after_tests", false)
            .apply()

        tvActivationState = findViewById(R.id.tvActivationState)
        btnActivateWidget = findViewById(R.id.btnActivateWidget)

        updateActivationState()

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            goHome()
        }

        btnActivateWidget.setOnClickListener {
            activateWidgetFeature()
        }

        findViewById<MaterialButton>(R.id.btnOpenSosSettings).setOnClickListener {
            startActivity(Intent(this, SosActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnGoHome).setOnClickListener {
            goHome()
        }
    }

    private fun activateWidgetFeature() {
        getSharedPreferences("sos_prefs", MODE_PRIVATE)
            .edit()
            .putBoolean("sos_widget_enabled", true)
            .apply()

        SosWidgetProvider.refreshAll(this)
        requestAddWidgetToHomeScreen()

        Toast.makeText(
            this,
            "SOS Widget activated",
            Toast.LENGTH_SHORT
        ).show()

        updateActivationState()
    }

    private fun requestAddWidgetToHomeScreen() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val appWidgetManager = AppWidgetManager.getInstance(this)

            if (appWidgetManager.isRequestPinAppWidgetSupported) {
                val provider = ComponentName(
                    this,
                    SosWidgetProvider::class.java
                )

                appWidgetManager.requestPinAppWidget(
                    provider,
                    null,
                    null
                )
            } else {
                Toast.makeText(
                    this,
                    "Please add the SOS Widget manually from your home screen widgets",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                this,
                "Please add the SOS Widget manually from your home screen widgets",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun updateActivationState() {
        val isEnabled = getSharedPreferences(
            "sos_prefs",
            MODE_PRIVATE
        ).getBoolean("sos_widget_enabled", false)

        tvActivationState.text = if (isEnabled) {
            "Status: Enabled ✅"
        } else {
            "Status: Disabled"
        }

        btnActivateWidget.text = if (isEnabled) {
            "SOS Widget Enabled"
        } else {
            "Enable SOS Widget"
        }

        btnActivateWidget.isEnabled = !isEnabled
    }

    private fun goHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}