package de.onlinehome.mann.martin.exenbot.listener;

import de.onlinehome.mann.martin.exenbot.Exenbot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		Member member = event.getMember();
		TextChannel channel = event.getChannel();

		if (!event.getAuthor().isBot())
			Exenbot.spamStates.updateSpamState(member, message);

		if (Exenbot.spamStates.getSpamState(member) >= Exenbot.MAX_SAME_MESSAGES_BEFORE_WARNING) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Verwarnung");
			builder.setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl());
			builder.setColor(0xdf0101);
			builder.addField("Grund: Spam", "Hallo " + member.getAsMention() + ",\ndu hast *5 gleiche Nachrichten* "
					+ "hintereinander geschickt. Bitte unterlasse den Spam ab jetzt, sonst könntest du einen *Chat-Mute* "
					+ "erhalten.", false);
			channel.sendMessage(builder.build()).queue();
			Exenbot.spamStates.resetSpamState(member);
		}
		if (Exenbot.spamStates.getWarnings(member) >= Exenbot.MAX_WARNINGS_BEFORE_MUTE) {
			event.getGuild().removeRoleFromMember(member, Exenbot.getNiceOneRole()).queue();
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("MUTE");
			builder.setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl());
			builder.setColor(0xdf0101);
			builder.addField("Grund: Spam", "Hallo " + member.getAsMention()
					+ ",\ndu hast ***15** gleiche Nachrichten* hintereinander geschickt. Deshalb bekommst du jetzt einen "
					+ "*Chat-Mute*. Bei noch stärkerem Verstoß könntest du einen Bann erhalten. Dies liegt aber in der "
					+ "Hand des Teams.", false);
			channel.sendMessage(builder.build()).queue();
			Exenbot.spamStates.setMuteEnd(member, System.currentTimeMillis() + 86400000);
			Exenbot.spamStates.resetWarnings(member);
		}
	}

}
