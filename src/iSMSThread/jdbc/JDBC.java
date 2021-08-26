/**
 * 
 */

package iSMSThread.jdbc;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * @author gorba
 *
 */
public class JDBC {
	
	String Driver = "com.mysql.jdbc.Driver";
	
	static String path;
	
	String user;
	
	String password;
	
	Connection connection = null;
	Statement statement=null;
	
	String dbname;
	
	public JDBC(String pwd, String user, String chemin) {
		password = pwd;
		this.user = user;
		path = chemin;  
	}
	
	/*
	 * Pour les requ�tes Selections
	 */
	public ResultSet executeQuery(String sql) {
		try {
			this.getCon();
			statement = (Statement) connection.createStatement();
			ResultSet rs = statement.executeQuery(sql); 
			return rs;
		}catch(SQLException sqle){ 
			   System.out.println("Erreur SQL executeQuery : "+sqle.getMessage()+" Cause : "+sqle.getCause()); 
			   //Cf. Comment g�rer les erreurs ? 
	    } catch (Exception ex) {
			Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
		} 
		return null;
	}
	
	/*
	 * Pour les requ�tes de modifications
	 */
	public int executeUpdate(String sql) { 
		try {
			this.getCon();
			statement = (Statement) connection.createStatement();
			int rs = statement.executeUpdate(sql); 
			return rs;
		}catch(SQLException sqle){ 
			   System.out.println("Erreur SQL executeUpdate : "); 
			   //Cf. Comment g�rer les erreurs ? 
		}catch (Exception ex) { 
			Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
		} 
		return -1;// Update n'a pas réussi
		
	}
	
	
    public void setCon() {
		try {
			Class.forName(Driver); // chargement du pilote
		} catch (java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException:   ");
			System.err.println(e.getMessage());
		}
		try {
			connection = (Connection) DriverManager.getConnection(path, this.user, password); 
		} 
		catch(SQLException sqle){ 
			   System.out.println("Erreur SQL setCon : "); 
			   //Cf. Comment g�rer les erreurs ? 
			} 
			catch(Exception e){ 
			   System.out.println("Autre erreur : "); 
			   e.printStackTrace(); 
			} 
    }

    public void getCon() {
        if (connection == null ) {
            setCon();
        } else{
			try {
				if(connection.isClosed())  setCon();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

  public void closeConnection() throws SQLException {
	    if(statement!=null){try{statement.close();}catch(Exception e){e.printStackTrace();}}  
	    if(connection!=null){try{connection.close();}catch(Exception e){e.printStackTrace();}} 
  }
}
