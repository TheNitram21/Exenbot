package de.onlinehome.mann.martin.exenbot.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.onlinehome.mann.martin.exenbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TempTalkCommand extends ListenerAdapter implements ServerCommand {
	
	public TempTalkCommand(Permission neededPermission) {
		this.neededPermission = neededPermission;
	}

	private Permission neededPermission = null;

	private Map<Long, Member> tempTalks = new HashMap<>();

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		String[] args = message.getContentDisplay().split(" ", 3);

		try {
			if (args[1].equals("create")) {
				tempTalks.put(channel.getGuild().getCategoryById(820741090855616586L)
						.createVoiceChannel("Temptalk " + args[2]).complete().getIdLong(), m);
			} else if (args[1].equals("delete")) {
				if (tempTalks.get(m.getVoiceState().getChannel().getIdLong()).equals(m)) {
					tempTalks.remove(m.getVoiceState().getChannel().getIdLong());
					m.getVoiceState().getChannel().delete().queue();
				}
			} else if(args[1].equals("size")) {
				if(tempTalks.get(m.getVoiceState().getChannel().getIdLong()).equals(m)) {
					m.getVoiceState().getChannel().getManager().setUserLimit(Integer.parseInt(args[2])).queue();
				}
			}
		} catch (NullPointerException e) {
			channel.sendMessage(
					new EmbedBuilder().setTitle("Fehler").setDescription("Du bist in keinem Temptalk!").build())
					.complete().delete().queueAfter(7, TimeUnit.SECONDS);
		} catch (ArrayIndexOutOfBoundsException e) {
			channel.sendMessage(
					new EmbedBuilder().setTitle("Fehler").setDescription("Zu wenig Argumente!").build())
					.complete().delete().queueAfter(7, TimeUnit.SECONDS);
		} catch (IllegalArgumentException e) {
			channel.sendMessage(
					new EmbedBuilder().setTitle("Fehler").setDescription("Größe darf maximal 99 sein!").build())
					.complete().delete().queueAfter(7, TimeUnit.SECONDS);
		} catch (NumberFormatException e) {
			channel.sendMessage(
					new EmbedBuilder().setTitle("Fehler").setDescription("`" + args[2] + "` ist keine Zahl!").build())
					.complete().delete().queueAfter(7, TimeUnit.SECONDS);
		}
		
		message.delete().queue();
	}

	@Override
	public Permission getNeededPermission() {
		return neededPermission;
	}
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		VoiceChannel channel = event.getChannelLeft();
		
		if(channel.getMembers().size() == 0) {
			if(tempTalks.containsKey(channel.getIdLong())) {
				tempTalks.remove(channel.getIdLong());
				channel.delete().queue();
			}
		}
	}

}
