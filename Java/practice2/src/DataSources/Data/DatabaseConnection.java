package DataSources.Data;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import io.github.cdimascio.dotenv.DotenvEntry;

import java.sql.*;
import java.util.Set;

public class DatabaseConnection {

    public static boolean connectionStatus;
    public static Connection CONNECTION;

    public DatabaseConnection(){

    }

    public static Boolean connect(String database) {
        Dotenv vault = Dotenv.load();
        String user = vault.get("MARIADB_USER");
        String url = vault.get("MARIADB_DATABASE_URL");
        //Connection dbConnection;

        try{
            Class<?> driver = Class.forName(vault.get("MARIADB_JDBC_DRIVER"));
            CONNECTION = DriverManager.getConnection(url, user,"");

            PreparedStatement setDatabase = CONNECTION.prepareStatement("USE " + database + ";");
            setDatabase.execute();
            connectionStatus = true;
            System.out.println("Using: " + database + " | Connection statud: " + connectionStatus);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("Connection failed OR closed connection.");
        } catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("Class.forName failed to recognize driver class.");
        }

        return connectionStatus;

    }
}
