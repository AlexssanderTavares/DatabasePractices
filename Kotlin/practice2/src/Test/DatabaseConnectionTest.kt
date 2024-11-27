package Test

import DataSources.Data.DatabaseConnection
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.sql.Connection

@RunWith(JUnit4::class)
class DatabaseConnectionTest {
        private lateinit var vault: Dotenv
    @Before
    fun setup(){
        this.vault = dotenv()
    }

    @Test
    fun ShouldCheckEnvironmentVariablesIfNotNull(){
        val user: String? = vault["MARIADB_USER"]
        val url: String? = vault["MARIADB_DATABASE_URL"]
        val driver: String? = vault["MARIADB_JDBC_DRIVER"]
        val db: String? = vault["SET_DATABASE"]

        println("User: $user | URL: $url | Driver: $driver | Database: $db")

        assertEquals("alexssander", user)
        assertEquals("jdbc:mariadb://localhost:3306", url)
        assertEquals("org.mariadb.jdbc.Driver", driver)
        assertEquals("Practice2", db)
    }

    @Test
    fun ShouldConnectAndChangeItStatusToTrue(){
        val dbStatus: Boolean = DatabaseConnection.connectionStatus


        assertEquals(true, dbStatus)
    }
}