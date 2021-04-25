package de.onlinehome.mann.martin.exenbot.litesql;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class SQLUtil {

	public static String getPrefix(Guild guild) {
		long guildID = guild.getIdLong();
		ResultSet set = LiteSQL.onQuery("SELECT prefix FROM settings WHERE guildID = " + guildID);
		String prefix = "?";

		try {
			prefix = set.getString("prefix");
		} catch (SQLException e) {
		}

		return prefix == null ? "?" : prefix;
	}

	public static void setPrefix(Guild guild, String prefix) {
		long guildID = guild.getIdLong();
		ResultSet set = LiteSQL.onQuery("SELECT * FROM settings WHERE guildID = " + guildID);

		try {
			if (set.next()) {
				LiteSQL.onUpdate("UPDATE settings SET prefix = '" + prefix + "' WHERE guildID = " + guildID);
			} else {
				LiteSQL.onUpdate("INSERT INTO settings(guildID, prefix) VALUES(" + guildID + ", '" + prefix + "')");
			}
		} catch (SQLException e) {
			LiteSQL.onUpdate("INSERT INTO settings(guildID, prefix) VALUES(" + guildID + ", '" + prefix + "')");
		}
	}

	public static TextChannel getRequestChannel(Guild guild) {
		long guildID = guild.getIdLong();
		ResultSet set = LiteSQL.onQuery("SELECT requestChannelID FROM settings WHERE guildID = " + guildID);
		TextChannel tc = null;

		try {
			tc = guild.getTextChannelById(set.getLong("requestChannelID"));
		} catch (SQLException e) {
		}

		if (tc == null)
			tc = guild.getDefaultChannel();

		return tc;
	}

	public static void setRequestChannel(Guild guild, TextChannel tc) {
		long guildID = guild.getIdLong();
		long tcID = tc.getIdLong();
		ResultSet set = LiteSQL.onQuery("SELECT * FROM settings WHERE guildID = " + guildID);

		try {
			if (set.next()) {
				LiteSQL.onUpdate("UPDATE settings SET requestChannelID = " + tcID + " WHERE guildID = " + guildID);
			} else {
				LiteSQL.onUpdate(
						"INSERT INTO settings(guildID, requestChannelID) VALUES(" + guildID + ", " + tcID + ")");
			}
		} catch (SQLException e) {
			LiteSQL.onUpdate("INSERT INTO settings(guildID, requestChannelID) VALUES(" + guildID + ", " + tcID + ")");
		}
	}

}
