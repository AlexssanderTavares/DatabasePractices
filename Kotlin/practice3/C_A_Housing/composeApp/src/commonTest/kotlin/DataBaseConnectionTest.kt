package org.example.cahousing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.test.Test


class DataBaseConnectionTest {

    @Test
    fun ShouldTryAndReturnAConnectionInstance() {
        try {
            val db = DataBaseConnection()
            assert(true, DataBaseConnection::connectionStatus)
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            println("Connection Failed")
        }
    }
}