package Test;

import DataSources.Data.DatabaseConnection;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

public class DatabaseConnectionTest {

    private Dotenv vault;

    @Before
    public void setup(){
        vault = Dotenv.load();
    }
    @Test
    public void ShouldReturnDatabaseConnection(){
        assert DatabaseConnection.connect(true);
        assert DatabaseConnection.connect(false);
    }
}
