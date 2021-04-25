package de.onlinehome.mann.martin.exenbot.litesql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.onlinehome.mann.martin.exenbot.Exenbot;

public class LiteSQL {
	private static Connection conn;
	private static Statement stmt;
	
	
	public static void connect() {
		conn = null;
		
		try {
			File file = new File("datenbank-exenbot.db");
			if(!file.exists()) {
				file.createNewFile();
			}
			
			String url = "jdbc:sqlite:" + file.getPath();
			conn = DriverManager.getConnection(url);
			
			System.out.println("Mit Datenbank verbunden. Pfad: " + file.getAbsolutePath());
			
			stmt = conn.createStatement();
			
		} catch (SQLException | IOException e) {
			Exenbot.logger.sendLogError("Beim Verbinden zur Datenbank ist ein Fehler aufgetreten.", e, null);
		}
		
		
	}
	
	public static void disconnect() {
		try {
			if(conn != null) {
				conn.close();
				System.out.println("Verbindung zur Datenbank getrennt.");
			}
		} catch (SQLException e) {
			Exenbot.logger.sendLogError("Beim Trennen der Verbindung zur Datenbank ist ein Fehler aufgetreten.", e, null);
		}
		
		
	}
	
	public static int onUpdate(String sql) {
		try {
			stmt.execute(sql);
			return stmt.getUpdateCount();
		} catch (SQLException e) {
			Exenbot.logger.sendLogError("Beim Verändern zur Datenbank ist ein Fehler aufgetreten.", e, null);
			return 0;
		}
		
		
	}
	
	public static ResultSet onQuery(String sql) {
		try {
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			Exenbot.logger.sendLogError("Beim Auslesen zur Datenbank ist ein Fehler aufgetreten.", e, null);
		}
		return null;
	}
}
