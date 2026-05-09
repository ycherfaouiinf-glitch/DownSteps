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

    private val allCenters = listOf(
        CenterModel(
            name = "Mafatih Al Jannah Association for Down Syndrome",
            location = "Algeria - Bordj Bou Arreridj",
            state = "Bordj Bou Arreridj",
            phone = "0661809255",
            mapQuery = "Bordj Bou Arreridj",
            imageRes = R.drawable.mfatih_eldjana_ass,
            category = "Association"
        ),
        CenterModel(
            name = "Al Rahma Association for Down Syndrome Care",
            location = "Algeria - Oran",
            state = "Oran",
            phone = "0666666666",
            mapQuery = "Oran",
            imageRes = R.drawable.mfatih_eldjana_ass,
            category = "Association"
        ),
        CenterModel(
            name = "Al Ishraq Autism Center",
            location = "Algeria - Constantine",
            state = "Constantine",
            phone = "0777777777",
            mapQuery = "Constantine",
            imageRes = R.drawable.mfatih_eldjana_ass,
            category = "Center"
        )
    )

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
        filterCenters()
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

        val itemAllStates = dialog.findViewById<LinearLayout>(R.id.itemAllStates)
        val itemOran = dialog.findViewById<LinearLayout>(R.id.itemOran)
        val itemConstantine = dialog.findViewById<LinearLayout>(R.id.itemConstantine)
        val itemAlgiers = dialog.findViewById<LinearLayout>(R.id.itemAlgiers)
        val itemBba = dialog.findViewById<LinearLayout>(R.id.itemBba)
        val itemShowMore = dialog.findViewById<LinearLayout>(R.id.itemShowMore)

        val ivAllStates = dialog.findViewById<ImageView>(R.id.ivAllStates)
        val tvAllStates = dialog.findViewById<TextView>(R.id.tvAllStates)
        val ivOran = dialog.findViewById<ImageView>(R.id.ivOran)
        val tvOran = dialog.findViewById<TextView>(R.id.tvOran)
        val ivConstantine = dialog.findViewById<ImageView>(R.id.ivConstantine)
        val tvConstantine = dialog.findViewById<TextView>(R.id.tvConstantine)
        val ivAlgiers = dialog.findViewById<ImageView>(R.id.ivAlgiers)
        val tvAlgiers = dialog.findViewById<TextView>(R.id.tvAlgiers)
        val ivBba = dialog.findViewById<ImageView>(R.id.ivBba)
        val tvBba = dialog.findViewById<TextView>(R.id.tvBba)
        val ivShowMore = dialog.findViewById<ImageView>(R.id.ivShowMore)
        val tvShowMore = dialog.findViewById<TextView>(R.id.tvShowMore)

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
            styleCategoryCard(itemAllCategories, ivAllCategories, tvAllCategories, tempCategory == "All Categories")
            styleCategoryCard(itemAssociation, ivAssociation, tvAssociation, tempCategory == "Association")
            styleCategoryCard(itemCenter, ivCenter, tvCenter, tempCategory == "Center")
        }

        fun styleStateChip(
            item: LinearLayout?,
            icon: ImageView?,
            text: TextView?,
            selected: Boolean
        ) {
            if (selected) {
                item?.setBackgroundResource(R.drawable.bg_state_chip_selected)
                icon?.setColorFilter(color(R.color.btn_primary))
                text?.setTextColor(color(R.color.btn_primary))
            } else {
                item?.setBackgroundResource(R.drawable.bg_state_chip)
                icon?.setColorFilter(color(R.color.input_icon))
                text?.setTextColor(color(R.color.text_title))
            }
        }

        fun updateStateSelection() {
            styleStateChip(itemAllStates, ivAllStates, tvAllStates, tempState == "All States")
            styleStateChip(itemOran, ivOran, tvOran, tempState == "Oran")
            styleStateChip(itemConstantine, ivConstantine, tvConstantine, tempState == "Constantine")
            styleStateChip(itemAlgiers, ivAlgiers, tvAlgiers, tempState == "Algiers")
            styleStateChip(itemBba, ivBba, tvBba, tempState == "Bordj Bou Arreridj")
        }

        fun updateShowMoreState() {
            if (isExpanded) {
                itemShowMore?.setBackgroundResource(R.drawable.bg_state_chip_selected)
                ivShowMore?.setImageResource(R.drawable.ic_arrow_up)
                ivShowMore?.setColorFilter(color(R.color.btn_primary))
                tvShowMore?.text = "Show Less"
                tvShowMore?.setTextColor(color(R.color.btn_primary))
            } else {
                itemShowMore?.setBackgroundResource(R.drawable.bg_state_chip)
                ivShowMore?.setImageResource(R.drawable.ic_arrow_down)
                ivShowMore?.setColorFilter(color(R.color.input_icon))
                tvShowMore?.text = "Show More"
                tvShowMore?.setTextColor(color(R.color.input_icon))
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

        itemAllStates?.setOnClickListener {
            tempState = "All States"
            updateStateSelection()
        }
        itemOran?.setOnClickListener {
            tempState = "Oran"
            updateStateSelection()
        }
        itemConstantine?.setOnClickListener {
            tempState = "Constantine"
            updateStateSelection()
        }
        itemAlgiers?.setOnClickListener {
            tempState = "Algiers"
            updateStateSelection()
        }
        itemBba?.setOnClickListener {
            tempState = "Bordj Bou Arreridj"
            updateStateSelection()
        }

        itemShowMore?.setOnClickListener {
            isExpanded = !isExpanded
            updateShowMoreState()
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
        updateStateSelection()
        updateShowMoreState()
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

        val btnClose = dialog.findViewById<ImageView>(R.id.btnCloseStateSheet)
        val btnApply = dialog.findViewById<MaterialButton>(R.id.btnApplyState)

        val itemAllStates = dialog.findViewById<LinearLayout>(R.id.itemAllStates)
        val itemOran = dialog.findViewById<LinearLayout>(R.id.itemOran)
        val itemConstantine = dialog.findViewById<LinearLayout>(R.id.itemConstantine)
        val itemAlgiers = dialog.findViewById<LinearLayout>(R.id.itemAlgiers)
        val itemBba = dialog.findViewById<LinearLayout>(R.id.itemBba)
        val itemAnnaba = dialog.findViewById<LinearLayout>(R.id.itemAnnaba)
        val itemSetif = dialog.findViewById<LinearLayout>(R.id.itemSetif)
        val itemTlemcen = dialog.findViewById<LinearLayout>(R.id.itemTlemcen)
        val itemShowMore = dialog.findViewById<LinearLayout>(R.id.itemShowMore)

        val ivAllStates = dialog.findViewById<ImageView>(R.id.ivAllStates)
        val tvAllStates = dialog.findViewById<TextView>(R.id.tvAllStates)
        val ivOran = dialog.findViewById<ImageView>(R.id.ivOran)
        val tvOran = dialog.findViewById<TextView>(R.id.tvOran)
        val ivConstantine = dialog.findViewById<ImageView>(R.id.ivConstantine)
        val tvConstantine = dialog.findViewById<TextView>(R.id.tvConstantine)
        val ivAlgiers = dialog.findViewById<ImageView>(R.id.ivAlgiers)
        val tvAlgiers = dialog.findViewById<TextView>(R.id.tvAlgiers)
        val ivBba = dialog.findViewById<ImageView>(R.id.ivBba)
        val tvBba = dialog.findViewById<TextView>(R.id.tvBba)
        val ivAnnaba = dialog.findViewById<ImageView>(R.id.ivAnnaba)
        val tvAnnaba = dialog.findViewById<TextView>(R.id.tvAnnaba)
        val ivSetif = dialog.findViewById<ImageView>(R.id.ivSetif)
        val tvSetif = dialog.findViewById<TextView>(R.id.tvSetif)
        val ivTlemcen = dialog.findViewById<ImageView>(R.id.ivTlemcen)
        val tvTlemcen = dialog.findViewById<TextView>(R.id.tvTlemcen)
        val ivShowMore = dialog.findViewById<ImageView>(R.id.ivShowMore)
        val tvShowMore = dialog.findViewById<TextView>(R.id.tvShowMore)
        val etSearchState = dialog.findViewById<TextInputEditText>(R.id.etSearchState)

        var tempState = selectedState
        var isExpanded = false

        fun color(id: Int) = ContextCompat.getColor(this, id)

        fun styleStateChip(
            item: LinearLayout?,
            icon: ImageView?,
            text: TextView?,
            selected: Boolean
        ) {
            if (selected) {
                item?.setBackgroundResource(R.drawable.bg_state_chip_selected)
                icon?.setColorFilter(color(R.color.btn_primary))
                text?.setTextColor(color(R.color.btn_primary))
            } else {
                item?.setBackgroundResource(R.drawable.bg_state_chip)
                icon?.setColorFilter(color(R.color.input_icon))
                text?.setTextColor(color(R.color.text_title))
            }
        }

        fun updateStateSelection() {
            styleStateChip(itemAllStates, ivAllStates, tvAllStates, tempState == "All States")
            styleStateChip(itemOran, ivOran, tvOran, tempState == "Oran")
            styleStateChip(itemConstantine, ivConstantine, tvConstantine, tempState == "Constantine")
            styleStateChip(itemAlgiers, ivAlgiers, tvAlgiers, tempState == "Algiers")
            styleStateChip(itemBba, ivBba, tvBba, tempState == "Bordj Bou Arreridj")
            styleStateChip(itemAnnaba, ivAnnaba, tvAnnaba, tempState == "Annaba")
            styleStateChip(itemSetif, ivSetif, tvSetif, tempState == "Setif")
            styleStateChip(itemTlemcen, ivTlemcen, tvTlemcen, tempState == "Tlemcen")
        }

        fun updateShowMoreState() {
            if (isExpanded) {
                itemAnnaba?.visibility = View.VISIBLE
                itemSetif?.visibility = View.VISIBLE
                itemTlemcen?.visibility = View.VISIBLE

                itemShowMore?.setBackgroundResource(R.drawable.bg_state_chip_selected)
                ivShowMore?.setImageResource(R.drawable.ic_arrow_up)
                ivShowMore?.setColorFilter(color(R.color.btn_primary))
                tvShowMore?.text = "Show Less"
                tvShowMore?.setTextColor(color(R.color.btn_primary))
            } else {
                itemAnnaba?.visibility = View.GONE
                itemSetif?.visibility = View.GONE
                itemTlemcen?.visibility = View.GONE

                itemShowMore?.setBackgroundResource(R.drawable.bg_state_chip)
                ivShowMore?.setImageResource(R.drawable.ic_arrow_down)
                ivShowMore?.setColorFilter(color(R.color.input_icon))
                tvShowMore?.text = "Show More"
                tvShowMore?.setTextColor(color(R.color.input_icon))
            }
        }

        btnClose?.setOnClickListener { dialog.dismiss() }

        itemAllStates?.setOnClickListener { tempState = "All States"; updateStateSelection() }
        itemOran?.setOnClickListener { tempState = "Oran"; updateStateSelection() }
        itemConstantine?.setOnClickListener { tempState = "Constantine"; updateStateSelection() }
        itemAlgiers?.setOnClickListener { tempState = "Algiers"; updateStateSelection() }
        itemBba?.setOnClickListener { tempState = "Bordj Bou Arreridj"; updateStateSelection() }
        itemAnnaba?.setOnClickListener { tempState = "Annaba"; updateStateSelection() }
        itemSetif?.setOnClickListener { tempState = "Setif"; updateStateSelection() }
        itemTlemcen?.setOnClickListener { tempState = "Tlemcen"; updateStateSelection() }

        fun filterStateOptions(query: String) {
            if (query.isBlank()) {
                itemShowMore?.visibility = View.VISIBLE
                updateShowMoreState()
                listOf(itemAllStates, itemOran, itemConstantine, itemAlgiers, itemBba).forEach {
                    it?.visibility = View.VISIBLE
                }
                return
            }

            itemShowMore?.visibility = View.GONE
            val stateItems = listOf(
                "All States" to itemAllStates,
                "Oran" to itemOran,
                "Constantine" to itemConstantine,
                "Algiers" to itemAlgiers,
                "Bordj Bou Arreridj" to itemBba,
                "Annaba" to itemAnnaba,
                "Setif" to itemSetif,
                "Tlemcen" to itemTlemcen
            )
            stateItems.forEach { (stateName, itemView) ->
                itemView?.visibility = if (stateName
                        .lowercase()
                        .split(" ")
                        .any { it.startsWith(query.lowercase()) }) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

        itemShowMore?.setOnClickListener {
            isExpanded = !isExpanded
            updateShowMoreState()
        }

        etSearchState?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterStateOptions(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        btnApply?.setOnClickListener {
            selectedState = tempState
            updateTopFilterTexts()
            filterCenters()
            dialog.dismiss()
        }

        updateStateSelection()
        updateShowMoreState()
        dialog.show()




    }

    private fun updateTopFilterTexts() {
        tvCategoryButton.text =
            if (selectedCategory == "All Categories") "Category" else selectedCategory

        tvStateButton.text =
            if (selectedState == "All States") "All States" else selectedState
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