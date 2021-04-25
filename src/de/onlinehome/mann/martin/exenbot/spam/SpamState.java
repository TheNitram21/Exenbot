package de.onlinehome.mann.martin.exenbot.spam;

import net.dv8tion.jda.api.entities.Member;

public class SpamState {
	
	public final Member member;
	private String lastMsg;
	private long lastMsgSentWhen;
	private int msgCount;
	
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
		
		lastMsgSentWhen = System.currentTimeMillis();
		
		if(this.lastMsg.equals(lastMsg))
			msgCount++;
		else
			msgCount = 1;
		
		this.lastMsg = lastMsg;
	}
	
	public int getSpamState() {
		return msgCount;
	}
	
	public void resetSpamState() {
		msgCount = 0;
	}
	
}
