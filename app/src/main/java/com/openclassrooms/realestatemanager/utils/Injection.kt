package com.openclassrooms.realestatemanager.utils

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Injection : Application() {

    override fun onCreate() {
        super.onCreate()
        setupFirestore()
    }

    private fun setupFirestore() {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings
    }

}