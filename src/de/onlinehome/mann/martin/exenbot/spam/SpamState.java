package de.onlinehome.mann.martin.exenbot.spam;

import static de.onlinehome.mann.martin.exenbot.YamlUtil.saveYAML;

import de.onlinehome.mann.martin.exenbot.Exenbot;
import de.onlinehome.mann.martin.exenbot.YamlUtil.YamlData.SpamStateData;
import net.dv8tion.jda.api.entities.Guild;

public class SpamState {
	
	private SpamStateData data;
	
	public SpamState(SpamStateData data) {
		this.data = data;
	}
	
	public void updateSpamState(String lastMsg) {
		if((System.currentTimeMillis() - data.getLastMsgSentWhen()) >= 600000) {
			data.setMsgCount(1);
		}
		
		if(data.getLastMsg().equals(lastMsg)) {
			data.setMsgCount(data.getMsgCount() + 1);
		} else {
			data.setLastMsg(lastMsg);
			data.setMsgCount(1);
		}
		
		if(data.getMsgCount() >= Exenbot.MAX_SAME_MESSAGES_BEFORE_WARNING) {
			data.setWarningCount(data.getWarningCount() + 1);
		}
		
		data.setLastMsgSentWhen(System.currentTimeMillis());
		
		saveYAML();
	}
	
	public void setMuteEnd(long muteEnd) {
		data.setMuteEnd(muteEnd);
		saveYAML();
	}
	
	public int getSpamState() {
		return data.getMsgCount();
	}
	
	public int getWarningCount() {
		return data.getWarningCount();
	}
	
	public long getMemberId() {
		return data.getMemberId();
	}
	
	public long getMuteEnd() {
		return data.getMuteEnd();
	}
	
	public void resetSpamState() {
		data.setMsgCount(0);
		saveYAML();
	}
	
	public void resetWarningCount() {
		data.setWarningCount(0);
		saveYAML();
	}
	
	public void resetMuteEnd() {
		data.setMuteEnd(-1);
		saveYAML();
	}
	
	public boolean shouldUnmute() {
		if(data.getMuteEnd() != -1 && System.currentTimeMillis() >= data.getMuteEnd()) {
			return true;
		} else
			return false;
	}
	
	public void unmute(Guild guild) {
		guild.addRoleToMember(getMemberId(), Exenbot.getNiceOneRole()).queue();
	}
	
}
