package com.openclassrooms.realestatemanager

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class DateFormatTest {


    @Test
    fun testGetTodayDate() {
        val expectedFormat = SimpleDateFormat("dd/MM/yyyy").format(Date())
        assertEquals(expectedFormat, getTodayDate())
    }

    companion object {
        fun getTodayDate(): String {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            return dateFormat.format(Date())
        }
    }
}