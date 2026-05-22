package com.example.downsteps1.data


import com.google.firebase.firestore.FirebaseFirestore

object CenterSeeder {

    fun addElAmelClinic() {

        val db = FirebaseFirestore.getInstance()

        val clinic = hashMapOf(
            "name" to "عيادة وعي للتكفل الأرطوفوني",
            "location" to "Laghouat, Algeria",
            "state" to "Laghouat",
            "phone" to "0676831772",
            "mapQuery" to "عيادة وعي للتكفل الأرطوفوني Laghouat",
            "category" to "Center",
            "imageName" to ""
        )

        db.collection("centers")
            .document("clinic_el_waay")
            .set(clinic)
    }
}