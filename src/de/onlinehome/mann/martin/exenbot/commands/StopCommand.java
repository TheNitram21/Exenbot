package de.onlinehome.mann.martin.exenbot.commands;

import org.json.JSONObject;

import de.onlinehome.mann.martin.exenbot.Exenbot;
import de.onlinehome.mann.martin.exenbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

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
	public String performCommand(JSONObject json) {
		String stoppingArgument = json.getJSONObject("d").getJSONObject("data").getJSONArray("options").getJSONObject(0)
				.getString("value");

		Guild g = Exenbot.shardMan.getGuildById(json.getJSONObject("d").getString("guild_id"));
		Member m = g.getMemberById(json.getJSONObject("d").getJSONObject("member").getJSONObject("user").getString("id"));
		
		for (Member member : g.getMembers()) {
			System.out.println(member);
		}

		if (!m.hasPermission(Permission.MANAGE_SERVER))
			return "Keine Berechtigungen";

		if (stoppingArgument.equals("restart")) {
			new Thread(() -> {
				Exenbot.stop("Kurzer Restart");
			}).start();
		} else if (stoppingArgument.equals("maintenance")) {
			new Thread(() -> {
				Exenbot.stop("Wartungsarbeiten");
			}).start();
		}

		return "Stoppe Bot...";
	}

}
