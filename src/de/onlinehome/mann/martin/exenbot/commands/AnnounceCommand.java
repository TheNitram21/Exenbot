package de.onlinehome.mann.martin.exenbot.commands;

import java.util.concurrent.TimeUnit;

import de.onlinehome.mann.martin.exenbot.YamlUtil;
import de.onlinehome.mann.martin.exenbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class AnnounceCommand implements ServerCommand {

	public AnnounceCommand(Permission neededPermission) {
		this.neededPermission = neededPermission;
	}

	private Permission neededPermission = null;
	private EmbedBuilder builder = new EmbedBuilder();
	private boolean mentionEveryone = false;

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		String[] args = message.getContentDisplay().split(" ", 3);

		if (args[1].equals("reset")) {
			builder = new EmbedBuilder();
			builder.setAuthor(m.getEffectiveName(), null, m.getUser().getAvatarUrl());
		} else if (args[1].equals("message")) {
			if (args.length == 2) {
				channel.sendMessage(new EmbedBuilder().setTitle("**message**")
						.setDescription("Setzt die Nachricht der Ankündigung.\nSyntax: *"
								+ YamlUtil.getPrefix(channel.getGuild()) + "announce message <Nachricht>*")
						.build()).complete().delete().queueAfter(30, TimeUnit.SECONDS);
			} else
				builder.setDescription(args[2]);
		} else if (args[1].equals("send")) {
			if (args.length == 2) {
				channel.sendMessage(new EmbedBuilder().setTitle("**send**")
						.setDescription("Sendet die Ankündigung.\nSyntax: *"
								+ YamlUtil.getPrefix(channel.getGuild()) + "announce send <Kanal>*")
						.build()).complete().delete().queueAfter(30, TimeUnit.SECONDS);
			} else {
				TextChannel channelToSend = message.getMentionedChannels().get(0);
				channelToSend.sendMessage(builder.build()).queue();
				if(mentionEveryone)
					channelToSend.sendMessage("@everyone").complete().delete().queue();
			}
		} else if (args[1].equals("footer")) {
			if (args.length == 2) {
				channel.sendMessage(new EmbedBuilder().setTitle("**message**")
						.setDescription("Setzt den Footer (kleine Schrift unten) der Ankündigung.\nSyntax: *"
								+ YamlUtil.getPrefix(channel.getGuild()) + "announce footer <Footer>*")
						.build()).complete().delete().queueAfter(30, TimeUnit.SECONDS);
			} else
				builder.setFooter(args[2]);
		} else if (args[1].equals("mention")) {
			if (args.length == 2) {
				channel.sendMessage(new EmbedBuilder().setTitle("**mention**")
						.setDescription("Stellt ein, ob die Ankündigung alle pingen soll.\nSyntax: *"
								+ YamlUtil.getPrefix(channel.getGuild()) + "announce mention <true|false>*")
						.build()).complete().delete().queueAfter(30, TimeUnit.SECONDS);
			} else {
				if(args[2].equals("true"))
					mentionEveryone = true;
				else
					mentionEveryone = false;
			}
		}
		channel.sendMessage("Erfolg!").complete().delete().queueAfter(7, TimeUnit.SECONDS);

		message.delete().queue();
	}

	@Override
	public Permission getNeededPermission() {
		return neededPermission;
	}

}
