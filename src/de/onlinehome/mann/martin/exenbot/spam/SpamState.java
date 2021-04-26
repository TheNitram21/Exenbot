package de.onlinehome.mann.martin.exenbot.spam;

import de.onlinehome.mann.martin.exenbot.Exenbot;
import net.dv8tion.jda.api.entities.Member;

public class SpamState {
	
	public final Member member;
	private String lastMsg;
	private long lastMsgSentWhen;
	private int msgCount;
	private long lastWarningGotWhen;
	private int warningCount;
	
	public SpamState(Member member) {
		this.member = member;
		this.lastMsg = "";
		this.lastMsgSentWhen = System.currentTimeMillis();
		this.msgCount = 1;
	}
	
	public void updateSpamState(String lastMsg) {
		if((lastMsgSentWhen - System.currentTimeMillis()) >= 600000) {
			msgCount = 0;
		}
		if((lastWarningGotWhen - System.currentTimeMillis()) >= 86400000) {
			member.getGuild().addRoleToMember(member, member.getGuild().getRolesByName("Nice One", false).get(0));
			warningCount = 0;
		}
		
		lastMsgSentWhen = System.currentTimeMillis();
		
		if(this.lastMsg.equals(lastMsg))
			msgCount++;
		else
			msgCount = 1;
		
		if(msgCount >= Exenbot.MAX_SAME_MESSAGES_BEFORE_WARNING) {
			warningCount++;
			lastWarningGotWhen = System.currentTimeMillis();
		}
		
		this.lastMsg = lastMsg;
	}
	
	public int getSpamState() {
		return msgCount;
	}
	
	public int getWarnings() {
		return warningCount;
	}
	
	public void resetSpamState() {
		msgCount = 0;
	}
	
	public void resetWarnings() {
		warningCount = 0;
	}
	
}
