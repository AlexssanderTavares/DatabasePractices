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
import java.sql.SQLException
import javax.swing.text.DefaultFormatter

class DataBaseConnection {

    companion object {
        lateinit var CONNECTION: Connection

        init {
            CONNECTION = connect()
        }

        var connectionStatus: Boolean = false

        fun connect(): Connection {
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
            val setDatabase = dbConnection.prepareStatement("USE C_A_Housing;")

            try {
                setDatabase.execute()
                connectionStatus = true
                println("Using C_A_housing datadabase, connection success!")
            } catch (e: SQLException) {
                println("Database is closed OR don't exist")
                println(e.message)
            }
            return dbConnection //returned
        }

    }
}
