package de.onlinehome.mann.martin.exenbot.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.onlinehome.mann.martin.exenbot.YamlUtil;
import de.onlinehome.mann.martin.exenbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class StreamPlanCommand implements ServerCommand {

	private final Permission neededPermission;

	private List<Stream> streams = new ArrayList<>();

	public StreamPlanCommand(Permission neededPermission) {
		this.neededPermission = neededPermission;
	}

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		String[] args = message.getContentDisplay().split(" ");

		if (args.length == 1) {
			EmbedBuilder builder = new EmbedBuilder().setTitle("Streamplan");

			for (Stream stream : streams) {
				builder.addField(stream.when, stream.what, false);
			}

			channel.sendMessage(builder.build()).queue();
		} else if (args[1].equals("add")) {
			if (m.hasPermission(Permission.MANAGE_SERVER)) {
				try {
					SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh-mm-ss");
					Date date;
					date = format.parse(args[2] + args[3]);
					YamlUtil.newStream(date.getTime(), args[4]);
				} catch (ParseException e) {
					channel.sendMessage(
							"Falsches Format! Richtig wäre: 'dd.MM.yyyy hh-mm-ss'. Beispiel: '12.03.2030 00:00:00'")
							.queue();
				}
			} else
				channel.sendMessage(new EmbedBuilder().setTitle("Keine Berechtigungen").setColor(0xdf0101)
						.addField("",
								"Dazu hast du keine Berechtigungen. Falls du denkst, dass das ein Fehler ist, "
										+ "frage bitte einen Administrator.",
								false)
						.build()).complete().delete().queueAfter(7, TimeUnit.SECONDS);
		}
	}

	@Override
	public Permission getNeededPermission() {
		return neededPermission;
	}

	private class Stream {
		public String when;
		public String what;
	}

}
