package de.onlinehome.mann.martin.exenbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import javax.security.auth.login.LoginException;

import de.onlinehome.mann.martin.exenbot.commands.TempTalkCommand;
import de.onlinehome.mann.martin.exenbot.listener.JoinListener;
import de.onlinehome.mann.martin.exenbot.listener.MessageListener;
import de.onlinehome.mann.martin.exenbot.logging.DiscordLogger;
import de.onlinehome.mann.martin.exenbot.spam.SpamStateList;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Exenbot {

	private DefaultShardManagerBuilder builder;
	public static ShardManager shardMan;
	public static CommandManager cmdMan;
	public static DiscordLogger logger;

	public static TempTalkCommand tempTalkCommand = new TempTalkCommand(Permission.VOICE_SPEAK);

	public static SpamStateList spamStates = new SpamStateList();

	public static final int MAX_SAME_MESSAGES_BEFORE_WARNING = 5;
	public static final int MAX_WARNINGS_BEFORE_MUTE = 3;
	private static Role NICEONEROLE;
	private static Role MEMBERROLE;

	private static Activity[] activities = { Activity.watching("Eidexe13"),
			Activity.watching("sich Discord-Channels an."), Activity.listening("der Konsole"), Activity.listening("@Exenbot") };

	public static void main(String[] args) {
		try {
			new Exenbot();
		} catch (LoginException | IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
	}

	public Exenbot() throws LoginException, IllegalArgumentException, IOException {
		YamlUtil.load();
		Configuration.read();

		builder = DefaultShardManagerBuilder.createDefault(Configuration.getToken());

		builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
		builder.addEventListeners(new MessageListener(), new JoinListener(), tempTalkCommand, new ListenerAdapter() {
			@Override
			public void onReady(ReadyEvent event) {
				NICEONEROLE = shardMan.getGuildById(820741090855616582l).getRolesByName("Nice One", false).get(0);
				MEMBERROLE = shardMan.getGuildById(820741090855616582l).getRolesByName("Member", false).get(0);
				spamStates.startUnmuteTest();
				Exenbot.logger.sendLogInfo("", "Bot online.", Exenbot.shardMan.getGuilds());
			}
		});

		shardMan = builder.build();
		cmdMan = new CommandManager();
		logger = new DiscordLogger();

		consoleListener();
		activitySwitcher();
	}

	public void consoleListener() {
		new Thread(() -> {
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				while ((line = reader.readLine()) != null) {
					if (line.equalsIgnoreCase("exit") && shardMan != null) {
						stop("");
					}
				}
			} catch (IOException e) {
				logger.sendLogError("Beim Lesen der Konsole ist ein Fehler aufgetreten.", e, shardMan.getGuilds());
			}
		}).start();
	}

	public static void stop(String reason) {
		try {
			if(reason.isBlank() || reason == null) {
				logger.sendLogInfo("", "Bot stoppt...\n**Grund**: ---", shardMan.getGuilds(), 0xdf0101);
			} else {
				logger.sendLogInfo("", "Bot stoppt...\n**Grund**: " + reason, shardMan.getGuilds(), 0xdf0101);
			}
			
			for (int i = 5; i > 0; i--) {
				if (i != 1)
					System.out.println("Bot stops in " + i + " seconds.");
				if (i == 1)
					System.out.println("Bot stops in " + i + " second.");
				Thread.sleep(1000);
			}
			YamlUtil.saveYAML();
			shardMan.setStatus(OnlineStatus.OFFLINE);
			shardMan.shutdown();
			System.exit(0);
		} catch (InterruptedException e) {
			logger.sendLogError("", e, shardMan.getGuilds());
		}
	}

	public static void activitySwitcher() {
		new Thread(() -> {
			long time = System.currentTimeMillis();

			while (true) {
				if (System.currentTimeMillis() >= time + 10000) {
					Random r = new Random();
					int i = r.nextInt(activities.length);

					shardMan.setActivity(activities[i]);

					time = System.currentTimeMillis();
				}
			}
		}).start();
	}

	public static CommandManager getCmdMan() {
		return cmdMan;
	}

	public static Role getNiceOneRole() {
		return NICEONEROLE;
	}

	public static Role getMemberRole() {
		return MEMBERROLE;
	}

}
