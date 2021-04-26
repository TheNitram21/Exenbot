package de.onlinehome.mann.martin.exenbot.commands;

import static de.onlinehome.mann.martin.exenbot.YamlUtil.saveYAML;

import java.util.concurrent.TimeUnit;

import de.onlinehome.mann.martin.exenbot.YamlUtil;
import de.onlinehome.mann.martin.exenbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SettingsCommand implements ServerCommand {

	public SettingsCommand(Permission neededPermission) {
		this.neededPermission = neededPermission;
	}

	@Override
	public Permission getNeededPermission() {
		return neededPermission;
	}

	private Permission neededPermission = null;

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		String[] args = message.getContentDisplay().split(" ");
		Guild guild = channel.getGuild();

		if (args.length == 1) {
			channel.sendMessage(new EmbedBuilder().setColor(0x00B2ee).setTitle("Settings")
					.addField("Prefix", "Ändere den Prefix von Commands.", true).build()).complete().delete()
					.queueAfter(7, TimeUnit.SECONDS);
		} else {
			if (args[1].equals("prefix")) {
				try {
					YamlUtil.setPrefix(guild, args[2]);
					channel.sendMessage(
							new EmbedBuilder().setTitle("Neuer Prefix!")
									.addField("",
											"Prefix für " + guild.getName() + " ist nun " + YamlUtil.getPrefix(guild)
													+ ".",
											false)
									.build())
							.complete().delete().queueAfter(7, TimeUnit.SECONDS);
				} catch (ArrayIndexOutOfBoundsException e) {
					channel.sendMessage(new EmbedBuilder().setTitle("Syntaxfehler!").setColor(0xdf0101)
							.addField("", "Zu wenig Argumente!", false).build()).complete().delete()
							.queueAfter(7, TimeUnit.SECONDS);
				}
			}
		}

		saveYAML();
		message.delete().queue();
	}

}
