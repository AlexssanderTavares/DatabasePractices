package DataSources.Data

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException



class DatabaseConnection {

    companion object{
        var connectionStatus: Boolean = false
        val CONNECTION: Connection = this.connect("Practice2")


        fun connect(database: String) : Connection{
            val vault: Dotenv = dotenv()
            val user: String? = vault["MARIADB_USER"]
            val url: String? = vault["MARIADB_DATABASE_URL"]

            lateinit var dbConnection: Connection

            try{
                val driver = Class.forName(vault["MARIADB_JDBC_DRIVER"])
                dbConnection = DriverManager.getConnection(url, user, "")
            }catch (e: SQLException){
                e.message
                e.printStackTrace()
                println("Connection failed")
            }catch (e: ClassNotFoundException){
                e.message
                e.printStackTrace()
                println("Class.forName failed to recognize driver class")
            }

            val setDatabase: PreparedStatement = dbConnection.prepareStatement("USE $database;")

            try{
                val checkOpen = dbConnection.isClosed()
                setDatabase.execute()
                this.connectionStatus = true
                println("Using: $database | Connection Status: ${connectionStatus}")
            }catch (e: SQLException) {
                e.message
                e.printStackTrace()
                println("Database is closed and must be opened before query executions OR database don't exist.")
            }

            return dbConnection

        }

    }
    fun checkStatus() : Boolean {
        return connectionStatus
    }
}