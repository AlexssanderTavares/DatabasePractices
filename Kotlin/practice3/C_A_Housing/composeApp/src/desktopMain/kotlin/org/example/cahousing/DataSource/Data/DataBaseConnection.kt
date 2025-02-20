package org.example.cahousing

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.ClassNotFoundException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import javax.swing.text.DefaultFormatter

class DataBaseConnection {

    companion object {

        lateinit var CONNECTION: Connection
        lateinit var TEST_CONNECTION: Connection
        var connectionStatus: Boolean = false

        init {
            CONNECTION = connect()
            TEST_CONNECTION = connect(true)
        }

        private fun connect(testEnvironment: Boolean = false): Connection {
            lateinit var dbConnection: Connection
            val vault: Dotenv = dotenv()
            val user = vault["MYSQL_USER"]
            val url = vault["MYSQL_URL"]

            try {
                val driver = Class.forName(vault["MYSQL_DRIVER"])
                dbConnection = DriverManager.getConnection(url, user, "")
            } catch (e: SQLException) {
                println("Failed to connect")
                println(e.message)
            } catch (e: ClassNotFoundException) {
                println("Driver Class not found")
                println(e.message)
            }
            lateinit var setDataBase: PreparedStatement
            if(!testEnvironment) {
                setDataBase = dbConnection.prepareStatement("USE C_A_Housing;")
            } else{
                setDataBase = dbConnection.prepareStatement("USE C_A_Housing_Test;")
            }
            try {
                setDataBase.execute()
                connectionStatus = true
                if(!testEnvironment) {
                    println("Using C_A_housing database, connection success!")
                }else{
                    println("Using C_A_Housing_Test database, connection success!")
                }
            } catch (e: SQLException) {
                println("Database is closed OR don't exist")
                println(e.message)
            }
            return dbConnection //returned
        }

    }
}
