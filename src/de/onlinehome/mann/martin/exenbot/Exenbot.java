package de.onlinehome.mann.martin.exenbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;
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
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Exenbot {

	private DefaultShardManagerBuilder builder;
	public static ShardManager shardMan;
	public static SlashCommandManager cmdMan;
	public static DiscordLogger logger;

	public static TempTalkCommand tempTalkCommand = new TempTalkCommand(Permission.VOICE_SPEAK);

	public static SpamStateList spamStates = new SpamStateList();

	public static final int MAX_SAME_MESSAGES_BEFORE_WARNING = 5;
	public static final int MAX_WARNINGS_BEFORE_MUTE = 3;
	private static Role NICEONEROLE;
	private static Role MEMBERROLE;

	private static final Activity[] activities = { Activity.watching("Eidexe13"),
			Activity.watching("sich Discord-Channels an."), Activity.listening("der Konsole"),
			Activity.listening("@Exenbot") };

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
		SlashCommandManager.activateSlashCommands(false,
				"{ \"name\": \"stop\", \"description\": \"Stoppt den Bot\", \"options\": [ { \"name\": \"reason\", \"description\": \"Warum stoppt der Bot?\", \"type\": 3, \"required\": true, \"choices\": [ { \"name\": \"Restart\", \"value\": \"restart\" }, { \"name\": \"Maintenance\", \"value\": \"maintenance\" } ] } ] }",
				"{ \"name\": \"credits\", \"description\": \"Schickt die Credits\", \"options\": [] }",
				"{ \"name\": \"temptalk\", \"description\": \"Verwaltet temporäre Sprachkanäle\", \"options\": [ { \"name\": \"create\", \"description\": \"Erstellt einen Temptalk\", \"value\": \"create\", \"type\": 1, \"required\": false, \"options\": [ { \"name\": \"Name\", \"description\": \"Der Name des Temptalks\", \"type\": 3, \"value\": \"talkName\", \"required\": true } ] }, { \"name\": \"delete\", \"description\": \"Löscht den Temptalk, in dem du bist\", \"type\": 1, \"value\": \"delete\" }, { \"name\": \"size\", \"description\": \"Setzt die Größe des Temptalks\", \"value\": \"size\", \"type\": 1, \"required\": false, \"options\": [ { \"name\": \"newsize\", \"description\": \"Setzt die Größe des Temptalks in dem du bist\", \"type\": 4, \"value\": \"talkNewSize\", \"required\": true } ] } ] }");

		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("[Exenbot" + DiscordLogger.getDateAndTimeNow() + "] An ungaught exception occured in thread " + t.getName() + " | Stack-Trace:");
				e.printStackTrace();
				logger.sendLogError("Ungefangene Exception im Thread " + t.getName(), (Exception) e, shardMan.getGuilds());
			}
		});
		
		builder = DefaultShardManagerBuilder.createDefault(Configuration.getToken());

		builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
		builder.setRawEventsEnabled(true);
		builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		builder.addEventListeners(new MessageListener(), new JoinListener(), tempTalkCommand, new SlashCommandManager(),
				new ListenerAdapter() {
					@Override
					public void onGuildReady(GuildReadyEvent event) {
						NICEONEROLE = shardMan.getGuildById(820741090855616582l).getRolesByName("Nice One", false)
								.get(0);
						MEMBERROLE = shardMan.getGuildById(820741090855616582l).getRolesByName("Member", false).get(0);
						spamStates.startUnmuteTest();
						event.getGuild().loadMembers();
						logger.sendLogInfo("", "Bot online.", Exenbot.shardMan.getGuilds());
					}
					@Override
					public void onGuildJoin(GuildJoinEvent event) {
						event.getGuild().loadMembers();
					}
				});

		shardMan = builder.build();
		cmdMan = new SlashCommandManager();
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
			if (reason.isBlank() || reason == null) {
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

	public static SlashCommandManager getCmdMan() {
		return cmdMan;
	}

	public static Role getNiceOneRole() {
		return NICEONEROLE;
	}

	public static Role getMemberRole() {
		return MEMBERROLE;
	}

}
