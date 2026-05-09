package com.example.downsteps1.common.navigation

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.downsteps1.R
import com.example.downsteps1.ui.HomeActivity
import com.example.downsteps1.ui.ProfilActivity
import com.example.downsteps1.ui.SettingsActivity
import com.example.downsteps1.ui.SosActivity

object BottomNavHelper {

    private const val HOME = "home"
    private const val SOS = "sos"
    private const val PROFILE = "profile"
    private const val SETTINGS = "settings"

    fun setup(activity: AppCompatActivity, selectedItem: String) {
        val homeTab = activity.findViewById<View>(R.id.homeTab) ?: return
        val sosTab = activity.findViewById<View>(R.id.sosTab) ?: return
        val profileTab = activity.findViewById<View>(R.id.profileTab) ?: return
        val settingsTab = activity.findViewById<View>(R.id.settingsTab) ?: return

        val navHome = activity.findViewById<ImageView>(R.id.navHome) ?: return
        val navSos = activity.findViewById<ImageView>(R.id.navSos) ?: return
        val navProfile = activity.findViewById<ImageView>(R.id.navProfile) ?: return
        val navSettings = activity.findViewById<ImageView>(R.id.navSettings) ?: return

        val txtHome = activity.findViewById<TextView>(R.id.txtHome) ?: return
        val txtSos = activity.findViewById<TextView>(R.id.txtSos) ?: return
        val txtProfile = activity.findViewById<TextView>(R.id.txtProfile) ?: return
        val txtSettings = activity.findViewById<TextView>(R.id.txtSettings) ?: return

        resetColors(activity, navHome, navSos, navProfile, navSettings, txtHome, txtSos, txtProfile, txtSettings)

        when (selectedItem) {
            HOME -> setSelected(activity, navHome, txtHome)
            SOS -> setSelected(activity, navSos, txtSos)
            PROFILE -> setSelected(activity, navProfile, txtProfile)
            SETTINGS -> setSelected(activity, navSettings, txtSettings)
        }

        homeTab.setOnClickListener { navigateTo(activity, HomeActivity::class.java) }
        sosTab.setOnClickListener { navigateTo(activity, SosActivity::class.java) }
        profileTab.setOnClickListener { navigateTo(activity, ProfilActivity::class.java) }
        settingsTab.setOnClickListener { navigateTo(activity, SettingsActivity::class.java) }
    }

    private fun navigateTo(activity: AppCompatActivity, target: Class<out AppCompatActivity>) {
        if (activity.javaClass == target) return

        val intent = Intent(activity, target).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        activity.startActivity(intent)
    }

    private fun resetColors(
        activity: AppCompatActivity,
        navHome: ImageView,
        navSos: ImageView,
        navProfile: ImageView,
        navSettings: ImageView,
        txtHome: TextView,
        txtSos: TextView,
        txtProfile: TextView,
        txtSettings: TextView
    ) {
        val normalColor = ContextCompat.getColor(activity, R.color.text_label)

        navHome.setColorFilter(normalColor)
        navSos.setColorFilter(normalColor)
        navProfile.setColorFilter(normalColor)
        navSettings.setColorFilter(normalColor)

        txtHome.setTextColor(normalColor)
        txtSos.setTextColor(normalColor)
        txtProfile.setTextColor(normalColor)
        txtSettings.setTextColor(normalColor)
    }

    private fun setSelected(activity: AppCompatActivity, icon: ImageView, text: TextView) {
        val selectedColor = ContextCompat.getColor(activity, R.color.btn_primary)
        icon.setColorFilter(selectedColor)
        text.setTextColor(selectedColor)
    }
}
