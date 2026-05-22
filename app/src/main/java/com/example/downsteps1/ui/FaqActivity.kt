package com.example.downsteps1.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.common.ui.BaseActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

class FaqActivity : BaseActivity() {

    private data class FaqSearchItem(
        val card: MaterialCardView,
        val keywords: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faq)

        BottomNavHelper.setup(this, "home")

        findViewById<LinearLayout>(R.id.backContainer).setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.faqPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etSearch = findViewById<TextInputEditText>(R.id.etSearch)

        val cardFaq1 = findViewById<MaterialCardView>(R.id.cardFaq1)
        val headerFaq1 = findViewById<LinearLayout>(R.id.headerFaq1)
        val contentFaq1 = findViewById<LinearLayout>(R.id.contentFaq1)
        val iconFaq1 = findViewById<ImageView>(R.id.iconFaq1)

        val cardFaq2 = findViewById<MaterialCardView>(R.id.cardFaq2)
        val headerFaq2 = findViewById<LinearLayout>(R.id.headerFaq2)
        val contentFaq2 = findViewById<LinearLayout>(R.id.contentFaq2)
        val iconFaq2 = findViewById<ImageView>(R.id.iconFaq2)

        val cardFaq3 = findViewById<MaterialCardView>(R.id.cardFaq3)
        val headerFaq3 = findViewById<LinearLayout>(R.id.headerFaq3)
        val contentFaq3 = findViewById<LinearLayout>(R.id.contentFaq3)
        val iconFaq3 = findViewById<ImageView>(R.id.iconFaq3)

        val faqItems = listOf(
            FaqSearchItem(
                cardFaq1,
                getString(R.string.faq_keywords_down_syndrome)
            ),
            FaqSearchItem(
                cardFaq2,
                getString(R.string.faq_keywords_communication)
            ),
            FaqSearchItem(
                cardFaq3,
                getString(R.string.faq_keywords_assessment)
            )
        )

        headerFaq1.setOnClickListener {
            toggleFaq(contentFaq1, iconFaq1)
        }

        headerFaq2.setOnClickListener {
            toggleFaq(contentFaq2, iconFaq2)
        }

        headerFaq3.setOnClickListener {
            toggleFaq(contentFaq3, iconFaq3)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val query = s?.toString()?.trim()?.lowercase().orEmpty()

                faqItems.forEach { item ->
                    val words = item.keywords.lowercase().split(" ")

                    val isMatch = query.isEmpty() || words.any { word ->
                        word.startsWith(query)
                    }

                    item.card.visibility = if (isMatch) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })
    }

    private fun toggleFaq(content: LinearLayout, icon: ImageView) {
        if (content.visibility == View.GONE) {
            content.visibility = View.VISIBLE
            icon.animate().rotation(180f).setDuration(200).start()
        } else {
            content.visibility = View.GONE
            icon.animate().rotation(0f).setDuration(200).start()
        }
    }
}