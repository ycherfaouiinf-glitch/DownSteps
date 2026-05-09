package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.google.android.material.button.MaterialButton

class AboutAppActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about_app)

        BottomNavHelper.setup(this, "settings")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.aboutAppPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backContainer = findViewById<LinearLayout>(R.id.backContainer)
        val btnBackSettings = findViewById<MaterialButton>(R.id.btnBackSettings)

        backContainer.setOnClickListener {
            finish()
        }

        btnBackSettings.setOnClickListener {
            finish()
        }
    }
}