package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.Utils
import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.math.roundToInt

class CurrencyConversionTest {


    @Test
    fun testConvertDollarToEuro() {
        assertEquals(81, Utils.convertDollarToEuro(100))
        assertEquals(41, Utils.convertDollarToEuro(50))
        assertEquals(0, Utils.convertDollarToEuro(0))
    }
    @Test
    fun testConvertEuroToDollar() {
        assertEquals(123, Utils.convertEuroToDollar(100))
        assertEquals(62, Utils.convertEuroToDollar(50))
        assertEquals(0, Utils.convertEuroToDollar(0))
    }


}