package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.common.ui.SystemBarHelper
import com.example.downsteps1.ui.adapter.CentersAdapter
import com.example.downsteps1.ui.model.CenterModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.example.downsteps1.common.AlgeriaStates
import com.example.downsteps1.common.AlgeriaState
import com.google.android.flexbox.FlexboxLayout

class CentersAndAssociationsActivity : BaseActivity() {

    private lateinit var recyclerCenters: RecyclerView
    private lateinit var etSearch: TextInputEditText
    private lateinit var adapter: CentersAdapter
    private lateinit var tvNoResults: TextView

    private lateinit var btnFilter: LinearLayout
    private lateinit var btnCategory: LinearLayout
    private lateinit var btnState: LinearLayout

    private lateinit var tvCategoryButton: TextView
    private lateinit var tvStateButton: TextView

    private var selectedCategory: String = "All Categories"
    private var selectedState: String = "All States"

    private var allCenters: List<CenterModel> = emptyList()

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_centers_and_associations)

        BottomNavHelper.setup(this, "home")
        SystemBarHelper.makeTransparent(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupRecycler()
        setupListeners()

        updateTopFilterTexts()
        loadCentersFromFirebase()
    }


    private fun initViews() {
        findViewById<ImageView>(R.id.btnBackChallange1).setOnClickListener { finish() }

        recyclerCenters = findViewById(R.id.recyclerCenters)
        etSearch = findViewById(R.id.etSearch)
        tvNoResults = findViewById(R.id.tvNoResults)

        btnFilter = findViewById(R.id.btnFilter)
        btnCategory = findViewById(R.id.btnCategory)
        btnState = findViewById(R.id.btnState)

        tvCategoryButton = findViewById(R.id.tvCategoryButton)
        tvStateButton = findViewById(R.id.tvStateButton)
    }

    private fun setupRecycler() {
        recyclerCenters.layoutManager = LinearLayoutManager(this)
        adapter = CentersAdapter(allCenters)
        recyclerCenters.adapter = adapter
    }

    private fun setupListeners() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCenters()
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        btnCategory.setOnClickListener { showCategoryBottomSheet() }
        btnState.setOnClickListener { showStateBottomSheet() }
        btnFilter.setOnClickListener { showFilterBottomSheet() }
    }

    private fun showFilterBottomSheet() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_sheet_filter)

        dialog.behavior.isDraggable = false
        dialog.behavior.peekHeight = 1600
        dialog.behavior.state =
            com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED

        val btnClose = dialog.findViewById<ImageView>(R.id.btnCloseFilterSheet)
        val btnApply = dialog.findViewById<MaterialButton>(R.id.btnApplyFilter)
        val btnReset = dialog.findViewById<MaterialButton>(R.id.btnResetFilter)

        val itemAllCategories = dialog.findViewById<LinearLayout>(R.id.itemAllCategories)
        val itemAssociation = dialog.findViewById<LinearLayout>(R.id.itemAssociation)
        val itemCenter = dialog.findViewById<LinearLayout>(R.id.itemCenter)

        val ivAllCategories = dialog.findViewById<ImageView>(R.id.ivAllCategories)
        val tvAllCategories = dialog.findViewById<TextView>(R.id.tvAllCategories)
        val ivAssociation = dialog.findViewById<ImageView>(R.id.ivAssociation)
        val tvAssociation = dialog.findViewById<TextView>(R.id.tvAssociation)
        val ivCenter = dialog.findViewById<ImageView>(R.id.ivCenter)
        val tvCenter = dialog.findViewById<TextView>(R.id.tvCenter)

        val filterStatesContainer =
            dialog.findViewById<FlexboxLayout>(R.id.filterStatesContainer)

        var tempCategory = selectedCategory
        var tempState = selectedState
        var isExpanded = false

        fun color(id: Int) = ContextCompat.getColor(this, id)

        fun styleCategoryCard(
            item: LinearLayout?,
            icon: ImageView?,
            text: TextView?,
            selected: Boolean
        ) {
            if (selected) {
                item?.setBackgroundResource(R.drawable.bg_filter_card_selected)
                icon?.setColorFilter(color(R.color.btn_primary))
                text?.setTextColor(color(R.color.btn_primary))
            } else {
                item?.setBackgroundResource(R.drawable.bg_filter_card)
                icon?.setColorFilter(color(R.color.input_icon))
                text?.setTextColor(color(R.color.text_title))
            }
        }

        fun updateCategorySelection() {
            styleCategoryCard(
                itemAllCategories,
                ivAllCategories,
                tvAllCategories,
                tempCategory == "All Categories"
            )
            styleCategoryCard(
                itemAssociation,
                ivAssociation,
                tvAssociation,
                tempCategory == "Association"
            )
            styleCategoryCard(
                itemCenter,
                ivCenter,
                tvCenter,
                tempCategory == "Center"
            )
        }

        fun addStateChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
            val chip = TextView(this)
            chip.text = text
            chip.gravity = android.view.Gravity.CENTER
            chip.setTextAppearance(R.style.Text_Label)

            chip.setBackgroundResource(
                if (isSelected) R.drawable.bg_state_chip_selected
                else R.drawable.bg_state_chip
            )

            chip.setTextColor(
                color(
                    if (isSelected) R.color.btn_primary
                    else if (text == "All States" || text.contains("Show")) R.color.input_icon
                    else R.color.text_title
                )
            )

            val params = FlexboxLayout.LayoutParams(
                0,
                resources.getDimensionPixelSize(R.dimen.state_chip_height)
            )
            params.flexBasisPercent = 0.31f
            params.setMargins(0, 0, 8, 12)
            chip.layoutParams = params

            chip.setOnClickListener { onClick() }

            filterStatesContainer?.addView(chip)
        }

        fun renderFilterStates() {
            filterStatesContainer?.removeAllViews()

            val statesToShow = if (isExpanded) {
                AlgeriaStates.all
            } else {
                AlgeriaStates.all.take(5)
            }

            if (isExpanded) {
                addStateChip(if (resources.configuration.locales[0].language == "ar") "▲ عرض أقل" else "▲ Show Less", false) {
                    isExpanded = false
                    renderFilterStates()
                }
            }

            statesToShow.forEach { state ->
                addStateChip(
                    text = if (resources.configuration.locales[0].language == "ar") state.nameAr else state.nameEn,
                    isSelected = tempState == state.nameEn
                ) {
                    tempState = state.nameEn
                    renderFilterStates()
                }
            }

            if (!isExpanded) {
                addStateChip(if (resources.configuration.locales[0].language == "ar") "▼ عرض المزيد" else "▼ Show More", false) {
                    isExpanded = true
                    renderFilterStates()
                }
            }
        }

        btnClose?.setOnClickListener {
            dialog.dismiss()
        }

        itemAllCategories?.setOnClickListener {
            tempCategory = "All Categories"
            updateCategorySelection()
        }

        itemAssociation?.setOnClickListener {
            tempCategory = "Association"
            updateCategorySelection()
        }

        itemCenter?.setOnClickListener {
            tempCategory = "Center"
            updateCategorySelection()
        }

        btnApply?.setOnClickListener {
            selectedCategory = tempCategory
            selectedState = tempState
            updateTopFilterTexts()
            filterCenters()
            dialog.dismiss()
        }

        btnReset?.setOnClickListener {
            selectedCategory = "All Categories"
            selectedState = "All States"
            updateTopFilterTexts()
            filterCenters()
            dialog.dismiss()
        }

        updateCategorySelection()
        renderFilterStates()
        dialog.show()
    }

    private fun showCategoryBottomSheet() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_sheet_category)

        val btnClose = dialog.findViewById<ImageView>(R.id.btnCloseCategorySheet)
        val btnApply = dialog.findViewById<MaterialButton>(R.id.btnApplyCategory)

        val itemAllCategories = dialog.findViewById<LinearLayout>(R.id.itemAllCategories)
        val itemAssociation = dialog.findViewById<LinearLayout>(R.id.itemAssociation)
        val itemCenter = dialog.findViewById<LinearLayout>(R.id.itemCenter)

        val ivAllCategories = dialog.findViewById<ImageView>(R.id.ivAllCategories)
        val tvAllCategories = dialog.findViewById<TextView>(R.id.tvAllCategories)
        val ivRadioAllCategories = dialog.findViewById<ImageView>(R.id.ivRadioAllCategories)

        val ivAssociation = dialog.findViewById<ImageView>(R.id.ivAssociation)
        val tvAssociation = dialog.findViewById<TextView>(R.id.tvAssociation)
        val ivRadioAssociation = dialog.findViewById<ImageView>(R.id.ivRadioAssociation)

        val ivCenter = dialog.findViewById<ImageView>(R.id.ivCenter)
        val tvCenter = dialog.findViewById<TextView>(R.id.tvCenter)
        val ivRadioCenter = dialog.findViewById<ImageView>(R.id.ivRadioCenter)

        var tempCategory = selectedCategory

        fun color(id: Int) = ContextCompat.getColor(this, id)

        fun updateCategorySelection() {
            if (tempCategory == "All Categories") {
                itemAllCategories?.setBackgroundResource(R.drawable.bg_filter_option_selected)
                tvAllCategories?.setTextColor(color(R.color.btn_primary))
                ivAllCategories?.setColorFilter(color(R.color.btn_primary))
                ivRadioAllCategories?.setImageResource(R.drawable.ic_radio_selected)
                ivRadioAllCategories?.setColorFilter(color(R.color.btn_primary))
            } else {
                itemAllCategories?.setBackgroundResource(R.drawable.bg_filter_option)
                tvAllCategories?.setTextColor(color(R.color.text_title))
                ivAllCategories?.setColorFilter(color(R.color.input_icon))
                ivRadioAllCategories?.setImageResource(R.drawable.ic_radio_unselected)
                ivRadioAllCategories?.setColorFilter(color(R.color.input_icon))
            }

            if (tempCategory == "Association") {
                itemAssociation?.setBackgroundResource(R.drawable.bg_filter_option_selected)
                tvAssociation?.setTextColor(color(R.color.btn_primary))
                ivAssociation?.setColorFilter(color(R.color.btn_primary))
                ivRadioAssociation?.setImageResource(R.drawable.ic_radio_selected)
                ivRadioAssociation?.setColorFilter(color(R.color.btn_primary))
            } else {
                itemAssociation?.setBackgroundResource(R.drawable.bg_filter_option)
                tvAssociation?.setTextColor(color(R.color.text_title))
                ivAssociation?.setColorFilter(color(R.color.input_icon))
                ivRadioAssociation?.setImageResource(R.drawable.ic_radio_unselected)
                ivRadioAssociation?.setColorFilter(color(R.color.input_icon))
            }

            if (tempCategory == "Center") {
                itemCenter?.setBackgroundResource(R.drawable.bg_filter_option_selected)
                tvCenter?.setTextColor(color(R.color.btn_primary))
                ivCenter?.setColorFilter(color(R.color.btn_primary))
                ivRadioCenter?.setImageResource(R.drawable.ic_radio_selected)
                ivRadioCenter?.setColorFilter(color(R.color.btn_primary))
            } else {
                itemCenter?.setBackgroundResource(R.drawable.bg_filter_option)
                tvCenter?.setTextColor(color(R.color.text_title))
                ivCenter?.setColorFilter(color(R.color.input_icon))
                ivRadioCenter?.setImageResource(R.drawable.ic_radio_unselected)
                ivRadioCenter?.setColorFilter(color(R.color.input_icon))
            }
        }

        btnClose?.setOnClickListener { dialog.dismiss() }

        itemAllCategories?.setOnClickListener {
            tempCategory = "All Categories"
            updateCategorySelection()
        }
        itemAssociation?.setOnClickListener {
            tempCategory = "Association"
            updateCategorySelection()
        }
        itemCenter?.setOnClickListener {
            tempCategory = "Center"
            updateCategorySelection()
        }

        btnApply?.setOnClickListener {
            selectedCategory = tempCategory
            updateTopFilterTexts()
            filterCenters()
            dialog.dismiss()
        }

        updateCategorySelection()
        dialog.show()
    }

    private fun showStateBottomSheet() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_sheet_state)

        dialog.behavior.isDraggable = false
        dialog.behavior.peekHeight = 1600
        dialog.behavior.state =
            com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED

        val btnClose = dialog.findViewById<ImageView>(R.id.btnCloseStateSheet)
        val btnApply = dialog.findViewById<MaterialButton>(R.id.btnApplyState)
        val statesContainer = dialog.findViewById<FlexboxLayout>(R.id.statesContainer)
        val etSearchState = dialog.findViewById<TextInputEditText>(R.id.etSearchState)

        var isExpanded = false

        fun addChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
            val chip = TextView(this)
            chip.text = text
            chip.gravity = android.view.Gravity.CENTER
            chip.setTextAppearance(R.style.Text_Label)

            chip.setBackgroundResource(
                if (isSelected) R.drawable.bg_state_chip_selected
                else R.drawable.bg_state_chip
            )

            chip.setTextColor(
                ContextCompat.getColor(
                    this,
                    if (isSelected) R.color.btn_primary
                    else if (text == "All States" || text.contains("Show")) R.color.input_icon
                    else R.color.text_title
                )
            )

            val params = FlexboxLayout.LayoutParams(
                0,
                resources.getDimensionPixelSize(R.dimen.state_chip_height)
            )
            params.flexBasisPercent = 0.31f
            params.setMargins(0, 0, 8, 12)
            chip.layoutParams = params

            chip.setOnClickListener { onClick() }

            statesContainer?.addView(chip)
        }

        fun renderStates() {
            statesContainer?.removeAllViews()

            val searchText = etSearchState?.text?.toString()?.trim()?.lowercase().orEmpty()

            val states = if (searchText.isNotEmpty()) {
                AlgeriaStates.all.filter {
                    it.nameEn.lowercase().contains(searchText) ||
                            it.nameAr.contains(searchText)
                }
            } else {
                if (isExpanded) AlgeriaStates.all else AlgeriaStates.all.take(6)
            }

            if (isExpanded && searchText.isEmpty()) {
                addChip(if (resources.configuration.locales[0].language == "ar") "▲ عرض أقل" else "▲ Show Less", false) {
                    isExpanded = false
                    renderStates()
                }
            }

            states.forEach { state ->
                addChip(
                    text = if (resources.configuration.locales[0].language == "ar") state.nameAr else state.nameEn,
                    isSelected = selectedState == state.nameEn
                ) {
                    selectedState = state.nameEn
                    updateTopFilterTexts()
                    filterCenters()
                    renderStates()
                }
            }

            if (!isExpanded && searchText.isEmpty()) {
                addChip(if (resources.configuration.locales[0].language == "ar") "▼ عرض المزيد" else "▼ Show More", false) {
                    isExpanded = true
                    renderStates()
                }
            }
        }

        btnClose?.setOnClickListener {
            dialog.dismiss()
        }

        etSearchState?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                renderStates()
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })

        btnApply?.setOnClickListener {
            dialog.dismiss()
        }

        renderStates()
        dialog.show()
    }

    private fun updateTopFilterTexts() {
        val isArabic = resources.configuration.locales[0].language == "ar"

        tvCategoryButton.text =
            if (selectedCategory == "All Categories") {
                if (isArabic) "الفئة" else "Category"
            } else {
                when (selectedCategory) {
                    "Association" -> if (isArabic) "جمعية" else "Association"
                    "Center" -> if (isArabic) "مركز" else "Center"
                    else -> selectedCategory
                }
            }

        tvStateButton.text =
            if (selectedState == "All States") {
                if (isArabic) "كل الولايات" else "All States"
            } else {
                val state = AlgeriaStates.all.find { it.nameEn == selectedState }
                if (isArabic) state?.nameAr ?: selectedState else selectedState
            }
    }

    private fun loadCentersFromFirebase() {
        db.collection("centers")
            .get()
            .addOnSuccessListener { snapshot ->

                allCenters = snapshot.documents.mapNotNull { doc ->
                    val imageName = doc.getString("imageName") ?: "mfatih_eldjana_ass"

                    CenterModel(
                        name = doc.getString("name") ?: "",
                        location = doc.getString("location") ?: "",
                        state = doc.getString("state") ?: "",
                        phone = doc.getString("phone") ?: "",
                        mapQuery = doc.getString("mapQuery") ?: "",
                        category = doc.getString("category") ?: "",
                        imageName = imageName,
                        imageRes = getImageResource(imageName)
                    )
                }

                filterCenters()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading centers", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getImageResource(imageName: String): Int {

        if (imageName.isBlank()) {
            return R.drawable.ic_center_default
        }

        val resourceId = resources.getIdentifier(
            imageName,
            "drawable",
            packageName
        )

        return if (resourceId != 0) {
            resourceId
        } else {
            R.drawable.ic_center_default
        }
    }

    private fun filterCenters() {

        val query = etSearch.text
            ?.toString()
            ?.trim()
            ?.lowercase()
            .orEmpty()

        val filteredList = allCenters.filter { center ->

            val matchesSearch =
                query.isEmpty() ||

                        center.name
                            .lowercase()
                            .split(" ")
                            .any { it.startsWith(query) } ||

                        center.location
                            .lowercase()
                            .split(" ")
                            .any { it.startsWith(query) } ||

                        center.state
                            .lowercase()
                            .split(" ")
                            .any { it.startsWith(query) } ||

                        center.category
                            .lowercase()
                            .split(" ")
                            .any { it.startsWith(query) } ||

                        center.phone
                            .startsWith(query)

            val matchesCategory =
                selectedCategory == "All Categories" ||
                        center.category == selectedCategory

            val matchesState =
                selectedState == "All States" ||
                        center.state == selectedState

            matchesSearch && matchesCategory && matchesState
        }

        adapter.updateList(filteredList)

        tvNoResults.visibility =
            if (filteredList.isEmpty()) View.VISIBLE else View.GONE

        recyclerCenters.visibility =
            if (filteredList.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { view ->
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is TextInputEditText) {
                val outRect = android.graphics.Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    hideKeyboard()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }


}