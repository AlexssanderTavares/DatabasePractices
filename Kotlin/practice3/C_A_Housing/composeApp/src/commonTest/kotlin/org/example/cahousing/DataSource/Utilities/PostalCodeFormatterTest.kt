package org.example.cahousing.DataSource.Utilities

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail


class PostalCodeFormatterTest {

    private val formatter: PostalCodeFormatter = PostalCodeFormatter()

    @Test
    fun toCepFormatMustReturnCEPFormattedString() {
        try {
            println("Trying to convert a invalid CEP to a valid one...")
            assertEquals("11.715-550" , formatter.toCepFormat("11715550"))
            println("In this case must ignore if CEP is formatted...")
            assertEquals("11.715-550", formatter.toCepFormat("11.715-550"))
            println("Test Succeed")
        }catch (e: Exception) {
            fail("Test failed due to: ${e.message}")
        }
    }

    @Test
    fun isValid() {
        try {
            println("Trying to validate CEP pattern...")
            assertEquals(true, formatter.isValid("11715550"))
            assertEquals(true, formatter.isValid("11.715-550"))
            println("Test Succeed")
        }catch (e: Exception){
            fail("Test failed due to: ${e.message}")
        }
    }
}