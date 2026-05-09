package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper

class HelpSupportActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_help_support)
        BottomNavHelper.setup(this, "settings")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.helpSupportPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }
    }
}