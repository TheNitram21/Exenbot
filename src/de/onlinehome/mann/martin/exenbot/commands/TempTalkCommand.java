package de.onlinehome.mann.martin.exenbot.commands;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import de.onlinehome.mann.martin.exenbot.Exenbot;
import de.onlinehome.mann.martin.exenbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
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
	public String performCommand(JSONObject json) {
		Guild guild = Exenbot.shardMan.getGuildById(json.getJSONObject("d").getString("guild_id"));
		Member m = guild
				.getMemberById(json.getJSONObject("d").getJSONObject("member").getJSONObject("user").getString("id"));

		String mainArgument = json.getJSONObject("d").getJSONObject("data").getJSONArray("options").getJSONObject(0)
				.getString("name");

		try {
			if (mainArgument.equals("create")) {
				VoiceChannel vc = guild.getCategoriesByName("Sprachkanäle", true).get(0)
						.createVoiceChannel(
								"Temptalk | " + json.getJSONObject("d").getJSONObject("data").getJSONArray("options")
										.getJSONObject(0).getJSONArray("options").getJSONObject(0).getString("value"))
						.complete();
				tempTalks.put(vc.getIdLong(), m);
				return "Temptalk erstellt!";
			} else if (mainArgument.equals("delete")) {
				if (tempTalks.get(m.getVoiceState().getChannel().getIdLong()).equals(m)
						|| m.hasPermission(Permission.MANAGE_CHANNEL)) {
					tempTalks.remove(m.getVoiceState().getChannel().getIdLong());
					m.getVoiceState().getChannel().delete().queue();
					return "Temptalk gelöscht!";
				}
				return "Keine Berechtigungen -> Du bist nicht der Ersteller des Temptalks.";
			} else if (mainArgument.equals("size")) {
				if (tempTalks.get(m.getVoiceState().getChannel().getIdLong()).equals(m)
						|| m.hasPermission(Permission.MANAGE_CHANNEL)) {
					m.getVoiceState().getChannel().getManager()
							.setUserLimit(json.getJSONObject("d").getJSONObject("data").getJSONArray("options")
									.getJSONObject(0).getJSONArray("options").getJSONObject(0).getInt("value"))
							.queue();
					return "Größe gesetzt!";
				}
				return "Keine Berechtigungen -> Du bist nicht der Ersteller des Temptalks.";
			} else
				return "Unbekanntes Argument";
		} catch (NullPointerException e) {
			return "Du bist in keinem Temptalk!";
		} catch (ArrayIndexOutOfBoundsException e) {
			return "Zu wenig Argumente!";
		} catch (IllegalArgumentException e) {
			return "Größe darf minimal 0 und maximal 99 sein!";
		}
	}

	@Override
	public Permission getNeededPermission() {
		return neededPermission;
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		VoiceChannel channel = event.getChannelLeft();

		if (channel.getMembers().size() == 0) {
			if (tempTalks.containsKey(channel.getIdLong())) {
				tempTalks.remove(channel.getIdLong());
				channel.delete().queue();
			}
		}
	}

}
