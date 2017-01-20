package com.dbsoftware.bungeeutilisals.bungee.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
	
	private static String host;
	private static int port;
	private static String database;
	private static String username;
	private static String password;
	private static Connection connection;
  
	public DatabaseManager(String host, int port, String database, String username, String password){
		DatabaseManager.host = host;
		DatabaseManager.port = port;
		DatabaseManager.database = database;
		DatabaseManager.username = username;
		DatabaseManager.password = password;
	}
  
	public void openConnection(){
		if (!isConnected()) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
				this.prepareDatabase();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
  
	public void prepareDatabase(){
		if (isConnected()) {
			try {
				Statement st = getConnection().createStatement();
				st.executeUpdate("CREATE TABLE IF NOT EXISTS Friends (Player VARCHAR(32) NOT NULL, Friends VARCHAR(32) NOT NULL)");
				st.executeUpdate("CREATE TABLE IF NOT EXISTS Requests (Player VARCHAR(32) NOT NULL, Requests VARCHAR(32) NOT NULL)");
				st.executeUpdate("CREATE TABLE IF NOT EXISTS PlayerInfo (Player TEXT NOT NULL, IP VARCHAR(32) NOT NULL, Bans INT NOT NULL, Warns INT NOT NULL, Mutes INT NOT NULL, Kicks INT NOT NULL)");
				st.executeUpdate("CREATE TABLE IF NOT EXISTS Bans (BannedBy VARCHAR(32) NOT NULL, Banned TEXT NOT NULL, BanTime LONG NOT NULL, Reason TEXT NOT NULL)");
				st.executeUpdate("CREATE TABLE IF NOT EXISTS Mutes (MutedBy VARCHAR(32) NOT NULL, Muted TEXT NOT NULL, MuteTime LONG NOT NULL, Reason TEXT NOT NULL)");
				st.executeUpdate("CREATE TABLE IF NOT EXISTS IPBans (BannedBy VARCHAR(32) NOT NULL, Banned TEXT NOT NULL, Reason TEXT NOT NULL)");
				st.executeUpdate("CREATE TABLE IF NOT EXISTS Staffs (Name VARCHAR(32) NOT NULL)");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
  
	public boolean isConnected(){
		boolean connected = false;
		try {
			connected = (connection != null) && (!connection.isClosed());
		} catch (SQLException e) {
	    	e.printStackTrace();
	    }
		return connected;
	}
	
	public Connection getConnection(){
		return connection;
	}
	
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection = null;
		}
	}
}