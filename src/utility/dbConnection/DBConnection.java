package utility.dbConnection;

import utility.popUp.AlertPopUp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection conn;

    /**
     * Singleton design pattern is implemented to avoid creation of multiple instances of DBConnetion
     */
    private DBConnection() {
    }

    public static Connection getDBConnection() {

        if (conn == null) {
            DBConnection.setConnection();
        }
        return conn;
    }

    /**
     * local mysql database credentials
     */
    private static void setConnection() {

        String url = "jdbc:mysql://localhost/";
        String dbname = "rms";
        String ssl = "?useSSL=false";
        String username = "root";
        String password = "root";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url + dbname + ssl, username, password);
        } catch (SQLException | ClassNotFoundException exception) {
            AlertPopUp.connectionError(exception);
        }

    }
}
