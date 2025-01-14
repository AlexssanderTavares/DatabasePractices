package com.example.lib

import java.lang.ClassNotFoundException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DataBaseConnection {

    companion object{
        var connectionStatus: Boolean = false
        val CONNECTION: Connection = connect()

        fun connect():Connection{
            val vault: Dotenv = Dotenv()

            val user = vault["MYSQL_USER"]
            val url = vault["MYSQL_URL"]

            lateinit var dbConnection: Connection

            try{
                val driver = Class.forName(vault["MYSQL_DRIVER"])
                dbConnection = DriverManager.getConnection(url, user, "")
            } catch (e: SQLException){
                println("Failed to connect")
                println(e.message)
            } catch (e: ClassNotFoundException){

                println("Driver Class not found")
                println(e.message)
            }
            val setDatabase = dbConnection.prepareStatement("USE C_A_Housing;")

            try {
                setDatabase.execute()
                connectionStatus = true
                println("Using C_A_housing database, connection success!")
            } catch (e: SQLException) {
                println("Database is closed OR don't exist")
                println(e.message)
            }
            return dbConnection
        }
    }
}