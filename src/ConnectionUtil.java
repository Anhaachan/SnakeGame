
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	public static Connection getConnection()
	{
		
		try {
			String url = "jdbc:postgresql://localhost:5432/snakegame";
			String dbname = "postgres";
			String password = "3030895";
			Connection con = DriverManager.getConnection(url,dbname,password);
			return con;
		}
		catch(  SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}