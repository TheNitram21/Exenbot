package de.onlinehome.mann.martin.exenbot.commands.types;

import org.json.JSONObject;

import net.dv8tion.jda.api.Permission;

public interface ServerCommand {
	
	public static final Permission neededPermission = null;
	
	public String performCommand(JSONObject json);
	public Permission getNeededPermission();
	
}
