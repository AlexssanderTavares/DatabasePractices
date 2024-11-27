package DataSources

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

class DataSource {

    companion object {
        private var connectionStatus: Boolean = false
        val CONNECTION: Connection = this.access()

        fun access(): Connection {
            //val url: String = "jdbc:mysql://localhost:3306"
            val url: String = "jdbc:mariadb://localhost:3306"
            val user: String = "alexssander"
            val password: String = ""
            lateinit var dbConnection: Connection

            try {
                val mariaDbDriver = Class.forName("org.mariadb.jdbc.Driver")
                //val mysqlDriver = Class.forName("com.mysql.cj.jdbc.Driver")
                dbConnection = DriverManager.getConnection(url, user, password)

            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                println(e.message)
                println("Class.forName fails to execute")

            } catch (e: SQLException) {
                e.printStackTrace()
                println(e.message)
                println("Failed to connect")
            }

            val setDatabase = "use CFBCurso;"
            var query: PreparedStatement = dbConnection.prepareStatement(setDatabase)

            try {
                val checkOpen = dbConnection.isClosed
                query.execute()
                this.connectionStatus = true

            } catch (e: SQLException) {
                e.printStackTrace()
                println(e.message)
                println("Database not found or doesn't exist | ${query.execute().toString()}")
            }
            println("Using CFBCurso | Status: ${this.connectionStatus}")
            return dbConnection
        }

        fun checkConnection(): Boolean{
            println("Connection status: ${this.connectionStatus}")
            return this.connectionStatus
        }
    }
}