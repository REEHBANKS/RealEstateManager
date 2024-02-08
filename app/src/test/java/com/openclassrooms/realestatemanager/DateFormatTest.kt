package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.Utils
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class DateFormatTest {


    @Test
    fun testGetTodayDate() {
        val expectedFormat = SimpleDateFormat("dd/MM/yyyy").format(Date())
        assertEquals(expectedFormat, Utils.getTodayDate())
    }


}