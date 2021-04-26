package de.onlinehome.mann.martin.exenbot.spam;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.onlinehome.mann.martin.exenbot.Exenbot;
import de.onlinehome.mann.martin.exenbot.YamlUtil;
import net.dv8tion.jda.api.entities.Member;

public class SpamStateList {
	
	private List<SpamState> list = new ArrayList<>();
	
	ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
	
	public void updateSpamState(Member member, String lastMsg) {
		for(SpamState spamState : list) {
			if(spamState.getMemberId() == member.getIdLong()) {
				spamState.updateSpamState(lastMsg);
				return;
			}
		}
		list.add(new SpamState(YamlUtil.getSpamStateDataByMember(member)));
	}
	
	public void setMuteEnd(Member member, long muteEnd) {
		for(SpamState spamState : list) {
			if(spamState.getMemberId() == member.getIdLong()) {
				spamState.setMuteEnd(muteEnd);
			}
		}
		list.add(new SpamState(YamlUtil.getSpamStateDataByMember(member)));
	}
	
	public int getSpamState(Member member) {
		for (SpamState spamState : list) {
			if(spamState.getMemberId() == member.getIdLong()) {
				return spamState.getSpamState();
			}
		}
		list.add(new SpamState(YamlUtil.getSpamStateDataByMember(member)));
		return 0;
	}
	
	public int getWarnings(Member member) {
		for (SpamState spamState : list) {
			if(spamState.getMemberId() == member.getIdLong()) {
				return spamState.getWarningCount();
			}
		}
		list.add(new SpamState(YamlUtil.getSpamStateDataByMember(member)));
		return 0;
	}
	
	public long getMuteEnd(Member member) {
		for (SpamState spamState : list) {
			if(spamState.getMemberId() == member.getIdLong()) {
				return spamState.getMuteEnd();
			}
		}
		list.add(new SpamState(YamlUtil.getSpamStateDataByMember(member)));
		return 0;
	}
	
	public void resetSpamState(Member member) {
		for (SpamState spamState : list) {
			if(spamState.getMemberId() == member.getIdLong()) {
				spamState.resetSpamState();
			}
		}
		list.add(new SpamState(YamlUtil.getSpamStateDataByMember(member)));
	}
	
	public void resetWarnings(Member member) {
		for (SpamState spamState : list) {
			if(spamState.getMemberId() == member.getIdLong()) {
				spamState.resetWarningCount();
			}
		}
		list.add(new SpamState(YamlUtil.getSpamStateDataByMember(member)));
	}
	
	public void resetMuteEnd(Member member) {
		for (SpamState spamState : list) {
			if(spamState.getMemberId() == member.getIdLong()) {
				spamState.resetMuteEnd();
			}
		}
		list.add(new SpamState(YamlUtil.getSpamStateDataByMember(member)));
	}
	
	public void startUnmuteTest() {
		scheduler.scheduleWithFixedDelay(() -> {
			for (SpamState spamState : list) {
				if(spamState.shouldUnmute()) {
					spamState.unmute(Exenbot.shardMan.getGuildById(820741090855616582L));
				}
			}
		}, 1L, 1L, TimeUnit.MINUTES);
	}
	
}
