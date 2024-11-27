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
    fun ShouldConnectAndChangeItStatusToTrue(){
        val dbStatus: Boolean = DatabaseConnection.connectionStatus


        assertEquals(true, dbStatus)
    }
}