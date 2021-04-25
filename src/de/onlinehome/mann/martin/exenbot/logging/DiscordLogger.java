package de.onlinehome.mann.martin.exenbot.logging;

import java.time.LocalDateTime;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class DiscordLogger {

	public void sendLogError(String msg, Exception e, List<Guild> guilds) {
		sendLogError(msg, e, guilds, 0xdf0101);
	}

	public void sendLogError(String msg, Exception e, List<Guild> guilds, int color) {
		for (Guild guild : guilds) {
			List<TextChannel> channels = guild.getTextChannelsByName("bot-log", false);
			for (TextChannel channel : channels) {
				EmbedBuilder builder = new EmbedBuilder().setTitle("ERROR").setColor(color)
						.setDescription("**" + e.getClass().getName() + "**\n" + msg).setFooter(getDateAndTimeNow());
				channel.sendMessage(builder.build()).queue();
			}
		}
	}

	public void sendLogInfo(String title, String msg, List<Guild> guilds) {
		sendLogInfo(title, msg, guilds, 0x00ff44);
	}

	public void sendLogInfo(String title, String msg, List<Guild> guilds, int color) {
		for (Guild guild : guilds) {
			List<TextChannel> channels = guild.getTextChannelsByName("bot-log", false);
			for (TextChannel channel : channels) {
				EmbedBuilder builder = new EmbedBuilder().setTitle("INFO").setColor(color)
						.setFooter(getDateAndTimeNow());
				if(title.equals(""))
					builder.setDescription(msg);
				else
					builder.setDescription("**" + title + "**\n" + msg);
				channel.sendMessage(builder.build()).queue();
			}
		}
	}

	public String getDateAndTimeNow() {
		LocalDateTime now = LocalDateTime.now();
		return now.getDayOfMonth() + "." + now.getMonthValue() + "." + now.getYear() + " " + now.getHour() + ":"
				+ now.getMinute() + ":" + now.getSecond();
	}

}