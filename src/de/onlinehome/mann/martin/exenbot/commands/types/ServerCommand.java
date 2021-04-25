package de.onlinehome.mann.martin.exenbot.commands.types;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public interface ServerCommand {
	
	public static final Permission neededPermission = null;
	
	public void performCommand(Member m, TextChannel channel, Message message);
	public Permission getNeededPermission();
	
}
