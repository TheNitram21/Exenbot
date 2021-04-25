package de.onlinehome.mann.martin.exenbot.commands;

import java.util.concurrent.TimeUnit;

import de.onlinehome.mann.martin.exenbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand implements ServerCommand {

	public HelpCommand(Permission neededPermission) {
		this.neededPermission = neededPermission;
	}
	
	@Override
	public Permission getNeededPermission() {
		return neededPermission;
	}

	private Permission neededPermission;
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		EmbedBuilder builder = new EmbedBuilder().setTitle("Hilfe").setColor(0x2efe2e);
		builder.addField(".settings", "Einstellungen.\nNur möglich, wenn du Admin bist.", false);
		channel.sendMessage(builder.build()).complete().delete().queueAfter(30, TimeUnit.SECONDS);
		message.delete().queue();
	}

}
