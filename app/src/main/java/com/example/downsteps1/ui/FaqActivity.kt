package com.example.downsteps1.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.common.ui.BaseActivity
import com.example.downsteps1.model.FaqItem
import com.example.downsteps1.ui.adapter.FaqAdapter
import com.google.android.material.textfield.TextInputEditText

class FaqActivity : BaseActivity() {

    private lateinit var adapter: FaqAdapter
    private val allFaqs = mutableListOf<FaqItem>()

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
        val recyclerFaq = findViewById<RecyclerView>(R.id.recyclerFaq)

        allFaqs.addAll(getFaqList())

        adapter = FaqAdapter(allFaqs)
        recyclerFaq.layoutManager = LinearLayoutManager(this)
        recyclerFaq.adapter = adapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim()?.lowercase().orEmpty()

                val filtered = if (query.isEmpty()) {
                    allFaqs
                } else {
                    allFaqs.filter { faq ->
                        faq.question.lowercase().contains(query) ||
                                faq.answer.lowercase().contains(query) ||
                                faq.keywords.lowercase().contains(query)
                    }
                }

                adapter.updateList(filtered)
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })
    }

    private fun getFaqList(): List<FaqItem> {
        return listOf(
            FaqItem(
                question = getString(R.string.what_is_down_syndrome),
                answer = getString(R.string.down_syndrome_answer),
                keywords = getString(R.string.faq_keywords_down_syndrome)
            ),
            FaqItem(
                question = getString(R.string.help_child_communication),
                answer = getString(R.string.help_child_communication_answer),
                keywords = getString(R.string.faq_keywords_communication)
            ),
            FaqItem(
                question = getString(R.string.why_early_assessment),
                answer = getString(R.string.why_early_assessment_answer),
                keywords = getString(R.string.faq_keywords_assessment)
            ),

            FaqItem(
                question = getString(R.string.faq_daily_challenges_question),
                answer = getString(R.string.faq_daily_challenges_answer),
                keywords = getString(R.string.faq_daily_challenges_keywords)
            ),
            FaqItem(
                question = getString(R.string.faq_child_level_question),
                answer = getString(R.string.faq_child_level_answer),
                keywords = getString(R.string.faq_child_level_keywords)
            ),
            FaqItem(
                question = getString(R.string.faq_speech_exercises_question),
                answer = getString(R.string.faq_speech_exercises_answer),
                keywords = getString(R.string.faq_speech_exercises_keywords)
            ),
            FaqItem(
                question = getString(R.string.faq_behavior_question),
                answer = getString(R.string.faq_behavior_answer),
                keywords = getString(R.string.faq_behavior_keywords)
            ),
            FaqItem(
                question = getString(R.string.faq_centers_question),
                answer = getString(R.string.faq_centers_answer),
                keywords = getString(R.string.faq_centers_keywords)
            ),
            FaqItem(
                question = getString(R.string.faq_sos_question),
                answer = getString(R.string.faq_sos_answer),
                keywords = getString(R.string.faq_sos_keywords)
            ),
            FaqItem(
                question = getString(R.string.faq_profile_question),
                answer = getString(R.string.faq_profile_answer),
                keywords = getString(R.string.faq_profile_keywords)
            )
        )
    }
}