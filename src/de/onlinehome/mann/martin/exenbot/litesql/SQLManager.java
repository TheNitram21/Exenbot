package de.onlinehome.mann.martin.exenbot.litesql;

public class SQLManager {

	public static void onCreate() {
		
		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS settings(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, guildID INTEGER, prefix STRING, requestChannelID INTEGER)");
		
	}
}
