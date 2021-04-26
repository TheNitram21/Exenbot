package de.onlinehome.mann.martin.exenbot.spam;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Member;

public class SpamStateList {
	
	private List<SpamState> list = new ArrayList<>();
	
	public void add(Member member) {
		list.add(new SpamState(member));
	}
	
	public void updateSpamState(Member member, String lastMsg) {
		for (SpamState spamState : list) {
			if(spamState.member.getIdLong() == member.getIdLong()) {
				spamState.updateSpamState(lastMsg);
				return;
			}
		}
		add(member);
	}
	
	public int getSpamState(Member member) {
		for (SpamState spamState : list) {
			if(spamState.member.getIdLong() == member.getIdLong()) {
				return spamState.getSpamState();
			}
		}
		add(member);
		return 0;
	}
	
	public int getWarnings(Member member) {
		for (SpamState spamState : list) {
			if(spamState.member.getIdLong() == member.getIdLong()) {
				return spamState.getWarnings();
			}
		}
		add(member);
		return 0;
	}
	
	public void resetSpamState(Member member) {
		for (SpamState spamState : list) {
			if(spamState.member.getIdLong() == member.getIdLong()) {
				spamState.resetSpamState();
			}
		}
	}
	
	public void resetWarnings(Member member) {
		for (SpamState spamState : list) {
			if(spamState.member.getIdLong() == member.getIdLong()) {
				spamState.resetWarnings();
			}
		}
	}
	
}
