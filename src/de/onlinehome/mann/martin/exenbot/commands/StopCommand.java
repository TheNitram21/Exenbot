package de.onlinehome.mann.martin.exenbot.commands;

import de.onlinehome.mann.martin.exenbot.Exenbot;
import de.onlinehome.mann.martin.exenbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class StopCommand implements ServerCommand {

	public StopCommand(Permission neededPermission) {
		this.neededPermission = neededPermission;
	}

	@Override
	public Permission getNeededPermission() {
		return neededPermission;
	}

	private Permission neededPermission;

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		String[] args = message.getContentDisplay().split(" ");
		try {
			if (args[1].equals("maintenance")) {
				Exenbot.stop("Wartungsarbeiten");
			} else if (args[1].equals("restart")) {
				Exenbot.stop("Kurzer Restart");
			} else {
				Exenbot.stop("");
			}
		} catch (ArrayIndexOutOfBoundsException e) { }

		message.delete().queue();
	}

}
