package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.downsteps1.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ChildInformationActivity : BaseActivity() {

    private lateinit var imgChild: ImageView
    private lateinit var framePhoto: View
    private lateinit var btnPickImage: View

    private lateinit var cardFemale: MaterialCardView
    private lateinit var cardMale: MaterialCardView

    private var selectedGender: String? = null
    private var selectedImageUri: String? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it.toString()
                showSelectedImage(it)
            }
        }

    private val takePhotoPreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp: Bitmap? ->
            bmp?.let {
                selectedImageUri = null
                showSelectedBitmap(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_child_information)

        setupHideKeyboardOnTouch(findViewById(R.id.childInfoPage))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.childInfoPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val auth = FirebaseAuth.getInstance()

        imgChild = findViewById(R.id.imgChildCircle)
        framePhoto = findViewById(R.id.frameChildPhoto)
        btnPickImage = findViewById(R.id.btnPickImage)

        cardFemale = findViewById(R.id.cardFemale)
        cardMale = findViewById(R.id.cardMale)

        val etChildName = findViewById<TextInputEditText>(R.id.etChildName)
        val etBirthDate = findViewById<TextInputEditText>(R.id.etBirthDate)
        val btnSave = findViewById<View>(R.id.btnSaveContinue)

        setupDefaultChildIcon()

        btnPickImage.setOnClickListener { openGalleryAndCamera() }
        framePhoto.setOnClickListener { openGalleryAndCamera() }

        cardFemale.setOnClickListener { selectGender("FEMALE") }
        cardMale.setOnClickListener { selectGender("MALE") }

        etChildName.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus()
                hideKeyboard(v)
                true
            } else {
                false
            }
        }

        etBirthDate.setOnClickListener {
            openDatePicker(etBirthDate)
        }

        findViewById<View>(R.id.backContainer).setOnClickListener {
            finish()
        }

        // --- Update Save Button Functionality for Firestore ---
                btnSave.setOnClickListener {
                    val name = etChildName.text?.toString()?.trim()
                    val birthDate = etBirthDate.text?.toString()?.trim()
                    val gender = selectedGender // Ensure this variable gets the value from your selection logic
                    val userId = FirebaseAuth.getInstance().currentUser?.uid

                    // Check if fields are not empty
                    if (name.isNullOrEmpty() || birthDate.isNullOrEmpty() || gender.isNullOrEmpty()) {
                        Toast.makeText(this, "Please fill in all the information", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (userId != null) {
                        // Create a data map to be saved
                        val childData = hashMapOf(
                            "childName" to name,
                            "birthDate" to birthDate,
                            "gender" to gender,
                            "isProfileComplete" to true // Flag to indicate user finished setup
                        )

                        // Reference to Cloud Firestore
                        val db = FirebaseFirestore.getInstance()

                        // Saving to "users" collection using the unique UID
                        db.collection("users").document(userId)
                            .set(childData, SetOptions.merge())
                            .addOnSuccessListener {
                                Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, LanguageTestActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                // Continue the onboarding flow even if Firestore save fails.
                                Toast.makeText(this, "Continuing to tests...", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, LanguageTestActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                    } else {
                        Toast.makeText(this, "Information completed. Starting tests...", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LanguageTestActivity::class.java))
                        finish()
                    }
                }
    }

    private fun setupDefaultChildIcon() {
        imgChild.scaleType = ImageView.ScaleType.CENTER_INSIDE
        imgChild.setPadding(28, 28, 28, 28)
    }

    private fun showSelectedImage(uri: Uri) {
        imgChild.setImageURI(uri)
        imgChild.scaleType = ImageView.ScaleType.CENTER_CROP
        imgChild.setPadding(0, 0, 0, 0)
    }

    private fun showSelectedBitmap(bitmap: Bitmap) {
        imgChild.setImageBitmap(bitmap)
        imgChild.scaleType = ImageView.ScaleType.CENTER_CROP
        imgChild.setPadding(0, 0, 0, 0)
    }

    private fun openDatePicker(etBirthDate: TextInputEditText) {
        val today = java.util.Calendar.getInstance()
        val maxDate = today.timeInMillis

        val minCalendar = java.util.Calendar.getInstance()
        minCalendar.add(java.util.Calendar.YEAR, -100)
        val minDate = minCalendar.timeInMillis

        val constraints = com.google.android.material.datepicker.CalendarConstraints.Builder()
            .setStart(minDate)
            .setEnd(maxDate)
            .build()

        val picker = com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select birth date")
            .setCalendarConstraints(constraints)
            .build()

        picker.show(supportFragmentManager, "BIRTH_DATE")

        picker.addOnPositiveButtonClickListener { millis ->
            val selectedDate = java.util.Calendar.getInstance()
            selectedDate.timeInMillis = millis

            val age = today.get(java.util.Calendar.YEAR) -
                    selectedDate.get(java.util.Calendar.YEAR)

            if (age in 0..100) {
                val sdf = java.text.SimpleDateFormat(
                    "dd/MM/yyyy",
                    java.util.Locale.getDefault()
                )
                etBirthDate.setText(sdf.format(java.util.Date(millis)))
            } else {
                etBirthDate.error = "Age must be between 0 and 100"
            }
        }
    }

    private val pickImageWithCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                if (data?.extras?.get("data") != null) {
                    val bitmap = data.extras?.get("data") as Bitmap
                    selectedImageUri = null
                    showSelectedBitmap(bitmap)
                } else {
                    val uri: Uri? = data?.data
                    uri?.let {
                        selectedImageUri = it.toString()
                        showSelectedImage(it)
                    }
                }
            }
        }

    private fun openGalleryAndCamera() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)

        val chooser = Intent.createChooser(galleryIntent, "Select Image")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

        pickImageWithCamera.launch(chooser)
    }

    private fun selectGender(gender: String) {
        selectedGender = gender

        if (gender == "FEMALE") {
            cardFemale.strokeWidth = 6
            cardFemale.setStrokeColor(getColor(R.color.pink_select))

            cardMale.strokeWidth = 2
            cardMale.setStrokeColor(getColor(R.color.default_stroke))
        } else {
            cardMale.strokeWidth = 6
            cardMale.setStrokeColor(getColor(R.color.blue_select))

            cardFemale.strokeWidth = 2
            cardFemale.setStrokeColor(getColor(R.color.default_stroke))
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setupHideKeyboardOnTouch(rootView: View) {
        if (rootView !is TextInputEditText) {
            rootView.setOnTouchListener { _, _ ->
                currentFocus?.let { hideKeyboard(it) }
                false
            }
        }

        if (rootView is android.view.ViewGroup) {
            for (i in 0 until rootView.childCount) {
                setupHideKeyboardOnTouch(rootView.getChildAt(i))
            }
        }
    }

    // Function to save data to Cloud Firestore (Without image upload for now)
    private fun saveDataToFirestore(userId: String, name: String, gender: String, birthDate: String, imageUrl: String) {
        val db = FirebaseFirestore.getInstance()

        val childData = mapOf(
            "name" to name,
            "gender" to gender,
            "birthDate" to birthDate,
            "imageUri" to imageUrl
        )

        val userUpdates = mapOf(
            "child" to childData
        )

        db.collection("users").document(userId)
            .set(userUpdates, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Child saved successfully!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LanguageTestActivity::class.java))
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Failed to save: ${error.message}", Toast.LENGTH_SHORT).show()
                findViewById<View>(R.id.btnSaveContinue).isEnabled = true
            }
    }
}