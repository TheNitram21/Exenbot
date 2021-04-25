package de.onlinehome.mann.martin.exenbot.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinListener extends ListenerAdapter {
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Du bist " + event.getGuild().getName() + " beigetreten!");
		builder.addField("Glückwunsch! Aber es gibt ein paar Regeln...",
				"**REGELN**\n"
				+ "**1.**: Kein Spamming. Der Bot wird, falls diese Regeln gebrochen wird, eine Erinnerung schicken.\n"
				+ "**2.**: Beleidigen, beschimpfen, lügen und ähnliches wird nicht gedulded.\n"
				+ "**3.**: Werbung für sich und andere Discord-Server wird nur im Channel #eigenwerbung gedulded.\n\n"
				+ "Das sind die Regeln. Hältst du sie ein, darfst du hier bleiben.",
				false);
		
		event.getUser().openPrivateChannel().complete().sendMessage(builder.build()).queue();
		event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("Nice One", false).get(0));
		
		
	}
	
}
