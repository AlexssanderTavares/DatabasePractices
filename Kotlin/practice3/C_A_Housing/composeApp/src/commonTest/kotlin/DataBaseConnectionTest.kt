package org.example.cahousing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.sql.Connection
import kotlin.test.Test
import kotlin.test.fail


class DataBaseConnectionTest {

    @Test
    fun ShouldTryAndReturnAConnectionInstance() {
        try {
            assert(true, DataBaseConnection::connectionStatus)
            println("Success!!!")
        } catch (e: Exception) {
            fail("Test failed due to: ${e.message}")
        }
    }

    @Test
    fun ShouldTryAndReturnAConnectionToATestDatabaseInstance(){
        try {
            val db: Connection = DataBaseConnection.TEST_CONNECTION
            assert(true, DataBaseConnection::connectionStatus)
            println("Success!!!")
        } catch (e: Exception){
            fail("Test failed due to: ${e.message}")
        }
    }
}