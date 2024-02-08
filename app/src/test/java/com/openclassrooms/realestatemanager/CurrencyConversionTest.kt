package com.openclassrooms.realestatemanager

import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.math.roundToInt

class CurrencyConversionTest {


    @Test
    fun testConvertDollarToEuro() {
        assertEquals(81, convertDollarToEuro(100))
        assertEquals(41, convertDollarToEuro(50))
        assertEquals(0, convertDollarToEuro(0))
    }

    @Test
    fun testConvertEuroToDollar() {
        assertEquals(123, convertEuroToDollar(100))
        assertEquals(62, convertEuroToDollar(50))
        assertEquals(0, convertEuroToDollar(0))
    }

    // Helper methods to match the signature in your question
    companion object {
        fun convertDollarToEuro(dollars: Int): Int {
            return (dollars * 0.812).roundToInt().toInt()
        }

        fun convertEuroToDollar(euros: Int): Int {
            return (euros * 1.23).roundToInt().toInt()
        }
    }




}