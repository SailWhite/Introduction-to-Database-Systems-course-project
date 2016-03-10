package ksp;

import java.sql.*;

public class Connector {
	Connection con;
	Statement stmt;
	public Connector() throws Exception {
		try{
		 	String userName = "fudanu02";
	   		String password = "1f45buoi";
        	String url = "jdbc:mysql://10.141.208.26/fudandbd02";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
    		con = DriverManager.getConnection (url, userName, password);
            stmt = con.createStatement();
        } catch(Exception e) {
			System.err.println("Unable to open mysql jdbc connection. The error is as follows,\n");
    		System.err.println(e.getMessage());
			throw(e);
		}
	}

	public ResultSet executeQuery(String sql) throws Exception {
	    return stmt.executeQuery(sql);
	}
	
	public boolean execute(String sql) throws Exception {
	    return stmt.execute(sql);
	}
	
	public void closeConnection() throws Exception {
		con.close();
	}
}
