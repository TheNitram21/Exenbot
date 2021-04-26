package de.onlinehome.mann.martin.exenbot;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import de.onlinehome.mann.martin.exenbot.commands.*;
import de.onlinehome.mann.martin.exenbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandManager {

	public ConcurrentHashMap<String, ServerCommand> commands;

	public CommandManager() {
		this.commands = new ConcurrentHashMap<>();

		this.commands.put("settings", new SettingsCommand(Permission.MANAGE_SERVER));
		this.commands.put("help", new HelpCommand(Permission.MESSAGE_WRITE));
		this.commands.put("stop", new StopCommand(Permission.MANAGE_SERVER));
	}

	public boolean perform(String command, Member m, TextChannel channel, Message message) {

		ServerCommand cmd;
		if ((cmd = this.commands.get(command.toLowerCase())) != null) {
			if (m.hasPermission(cmd.getNeededPermission())) {
				cmd.performCommand(m, channel, message);
				return true;
			} else {
				channel.sendMessage(new EmbedBuilder().setTitle("Keine Berechtigungen").setColor(0xdf0101)
						.addField("",
								"Dazu hast du keine Berechtigungen. Falls du denkst, dass das ein Fehler ist, "
										+ "frage bitte einen Administrator.",
								false)
						.build()).complete().delete().queueAfter(7, TimeUnit.SECONDS);
				return true;
			}
		}

		return false;
	}
}
