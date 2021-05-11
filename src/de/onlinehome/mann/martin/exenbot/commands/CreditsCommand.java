package de.onlinehome.mann.martin.exenbot.commands;

import org.json.JSONObject;

import de.onlinehome.mann.martin.exenbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;

public class CreditsCommand implements ServerCommand {

	public CreditsCommand(Permission neededPermission) {
		this.neededPermission = neededPermission;
	}

	private Permission neededPermission;

	@Override
	public String performCommand(JSONObject json) {
		return "**Das Team**: Eid.exe, Nitram21, Füchs | **Exenbot**: Nitram21 | **slive**: TJC-Team, Link: https://slivebot.de/ | "
				+ "**Ravebot**: Rapha#2626, .Peda | abgelehnt#6565, Link: https://rave-music.de/";
	}

	@Override
	public Permission getNeededPermission() {
		return neededPermission;
	}

}
