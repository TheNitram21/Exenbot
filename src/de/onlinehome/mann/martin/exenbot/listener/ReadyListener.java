package de.onlinehome.mann.martin.exenbot.listener;

import de.onlinehome.mann.martin.exenbot.Exenbot;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {
	
	@Override
	public void onReady(ReadyEvent event) {
		Exenbot.logger.sendLogInfo("", "Bot online.", Exenbot.shardMan.getGuilds());
	}
	
}
