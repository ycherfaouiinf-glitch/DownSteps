package com.example.downsteps1.common.ui

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val darkModeEnabled = getSharedPreferences("settings_prefs", MODE_PRIVATE)
            .getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (darkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        super.onCreate(savedInstanceState)
        SystemBarHelper.makeTransparent(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        configureTransparentBottomNav()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        configureTransparentBottomNav()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        configureTransparentBottomNav()
    }

    private fun configureTransparentBottomNav() {
        configureBackButtons()
        val bottomNav = findViewById<MaterialCardView>(R.id.customBottomNav) ?: return

        bottomNav.setCardBackgroundColor(0x66FFFFFF)
        bottomNav.strokeColor = 0xBBFFFFFF.toInt()
        bottomNav.cardElevation = resources.displayMetrics.density * 8f
        bottomNav.translationZ = resources.displayMetrics.density * 8f

        bottomNav.post {
            val safeBottomPadding = bottomNav.height + (resources.displayMetrics.density * 40f).toInt()
            val root = window.decorView.rootView as? ViewGroup ?: return@post
            addBottomPaddingToScrollableContent(root, safeBottomPadding)
        }
    }


    private fun configureBackButtons() {
        val backIds = listOf(R.id.backContainer, R.id.btnBack, R.id.btnBackChallange1)

        backIds.forEach { id ->
            findViewById<View>(id)?.let { backView ->
                backView.isClickable = true
                backView.isFocusable = true
                backView.setOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    private fun addBottomPaddingToScrollableContent(view: View, bottomPadding: Int) {
        when (view) {
            is NestedScrollView -> {
                view.clipToPadding = false
                if (view.paddingBottom < bottomPadding) {
                    view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, bottomPadding)
                }
            }
            is ScrollView -> {
                view.clipToPadding = false
                if (view.paddingBottom < bottomPadding) {
                    view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, bottomPadding)
                }
            }
            is RecyclerView -> {
                view.clipToPadding = false
                if (view.paddingBottom < bottomPadding) {
                    view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, bottomPadding)
                }
            }
        }

        if (view is ViewGroup) {
            view.children.forEach { child -> addBottomPaddingToScrollableContent(child, bottomPadding) }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val currentFocusedView = currentFocus

            if (currentFocusedView is TextInputEditText) {
                val outRect = Rect()
                currentFocusedView.getGlobalVisibleRect(outRect)

                val touchedOutside = !outRect.contains(
                    event.rawX.toInt(),
                    event.rawY.toInt()
                )

                if (touchedOutside) {
                    currentFocusedView.clearFocus()
                    hideKeyboard(currentFocusedView)
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
