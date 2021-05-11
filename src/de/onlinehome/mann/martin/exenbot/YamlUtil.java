package de.onlinehome.mann.martin.exenbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import de.onlinehome.mann.martin.exenbot.YamlUtil.YamlData.SpamStateData;
import net.dv8tion.jda.api.entities.Member;

public class YamlUtil {

	private static File file;
	private static Yaml yaml;
	private static YamlData data;
	
	public static void load() throws IOException {
		file = new File("datenbank-exenbot.yml");

		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(FlowStyle.BLOCK);
		options.setPrettyFlow(true);
		yaml = new Yaml(options);

		if (file.exists())
			data = yaml.load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
		else {
			file.createNewFile();
			data = new YamlData();
		}

		saveYAML();
	}

	public static void saveYAML() {
		try {
			yaml.dump(data, new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
		} catch (YAMLException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static List<SpamStateData> getSpamStateDatas() {
		return data.spamStateDatas;
	}
	
	public static SpamStateData getSpamStateDataByMember(Member member) {
		for (SpamStateData spamStateData : getSpamStateDatas()) {
			if(spamStateData.getMemberId() == member.getIdLong()) {
				return spamStateData;
			}
		}
		SpamStateData temp = new SpamStateData();
		temp.setMemberId(member.getIdLong());
		getSpamStateDatas().add(temp);
		return temp;
	}
	
	public static class YamlData {
		private List<SpamStateData> spamStateDatas;
		
		public YamlData() {
			spamStateDatas = new ArrayList<>();
		}
		
		public List<SpamStateData> getSpamStateDatas() {
			return spamStateDatas;
		}

		public void setSpamStateDatas(List<SpamStateData> spamStateDatas) {
			this.spamStateDatas = spamStateDatas;
		}

		public static class SpamStateData {
			private long memberId;
			private String lastMsg;
			private long lastMsgSentWhen;
			private int msgCount;
			private int warningCount;
			private long muteEnd;
			
			public SpamStateData() {
				memberId = 0L;
				lastMsg = "";
				lastMsgSentWhen = -1;
				msgCount = 0;
				warningCount = 0;
				muteEnd = -1;
			}

			public long getMemberId() {
				return memberId;
			}
			
			public void setMemberId(long memberId) {
				this.memberId = memberId;
			}

			public String getLastMsg() {
				return lastMsg;
			}

			public void setLastMsg(String lastMsg) {
				this.lastMsg = lastMsg;
			}

			public long getLastMsgSentWhen() {
				return lastMsgSentWhen;
			}

			public void setLastMsgSentWhen(long lastMsgSentWhen) {
				this.lastMsgSentWhen = lastMsgSentWhen;
			}

			public int getMsgCount() {
				return msgCount;
			}

			public void setMsgCount(int msgCount) {
				this.msgCount = msgCount;
			}

			public int getWarningCount() {
				return warningCount;
			}

			public void setWarningCount(int warningCount) {
				this.warningCount = warningCount;
			}

			public long getMuteEnd() {
				return muteEnd;
			}

			public void setMuteEnd(long muteEnd) {
				this.muteEnd = muteEnd;
			}
		}
	}
	
}
