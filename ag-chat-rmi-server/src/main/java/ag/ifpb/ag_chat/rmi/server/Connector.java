package ag.ifpb.ag_chat.rmi.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connector {
	private static Connection connection = null;

    public static Connection init() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
            		"jdbc:postgresql://localhost:5432/chatrmi", "postgres", "avantasia");
        } catch (SQLException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }
    
    public static void close() throws SQLException{
    		if (connection != null) connection.close();
    }
}
