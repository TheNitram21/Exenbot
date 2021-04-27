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
		builder.setDescription("**Commands für jeden**");
		builder.addField(".help", "Zeigt diese Liste an.", false);
		builder.addField(".temptalk <create|delete|size> [Name (*create*) bzw. Größe (*size*)]",
				"Erstellt, löscht oder setzt die Größe eines Temptalks.", false);
		if (m.hasPermission(Permission.MANAGE_SERVER)) {
			builder.addField("", "**Admincommands**", false);
			builder.addField(".settings", "Einstellungen.", false);
			builder.addField(".stop [maintenance|restart]", "Stoppt den Bot.", false);
		}
		channel.sendMessage(builder.build()).complete().delete().queueAfter(30, TimeUnit.SECONDS);
		message.delete().queue();
	}

}
