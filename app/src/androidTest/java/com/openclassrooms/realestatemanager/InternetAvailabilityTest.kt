package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InternetAvailabilityTest {

    @Test
    fun testIsInternetAvailable() {

        val context = ApplicationProvider.getApplicationContext<Context>()

        val isInternetAvailable = Utils.isInternetAvailable(context)

        assertTrue("Internet should be available", isInternetAvailable)
    }
}

