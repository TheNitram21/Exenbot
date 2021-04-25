package de.onlinehome.mann.martin.exenbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.security.auth.login.LoginException;

import de.onlinehome.mann.martin.exenbot.listener.JoinListener;
import de.onlinehome.mann.martin.exenbot.listener.MessageListener;
import de.onlinehome.mann.martin.exenbot.listener.ReadyListener;
import de.onlinehome.mann.martin.exenbot.litesql.LiteSQL;
import de.onlinehome.mann.martin.exenbot.litesql.SQLManager;
import de.onlinehome.mann.martin.exenbot.logging.DiscordLogger;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Exenbot {

	private DefaultShardManagerBuilder builder;
	public static ShardManager shardMan;
	public static CommandManager cmdMan;
	public static DiscordLogger logger;

	public static final int MAX_SAME_MESSAGES_BEFORE_WARNING = 5;

	public static void main(String[] args) {
		try {
			new Exenbot();
		} catch (LoginException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public Exenbot() throws LoginException, IllegalArgumentException {
		LiteSQL.connect();
		SQLManager.onCreate();

		builder = DefaultShardManagerBuilder
				.createDefault("ODM0MDM5Njk3MzE0NTQ1NzM1.YH7F_w.o9IVGhU4ItK5tpZhdXr0WgYnI4M");

		builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
		builder.addEventListeners(new MessageListener(), new JoinListener(), new ReadyListener());

		shardMan = builder.build();
		cmdMan = new CommandManager();
		logger = new DiscordLogger();

		consoleListener();
	}

	public void consoleListener() {
		String line = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			while ((line = reader.readLine()) != null) {
				if (line.equalsIgnoreCase("exit") && shardMan != null) {
					stop();
				}
			}
		} catch (IOException e) {
			logger.sendLogError("Beim Lesen der Konsole ist ein Fehler aufgetreten.", e, shardMan.getGuilds());
		}
	}

	public static void stop() {
		try {
			logger.sendLogInfo("", "Bot stoppt...", shardMan.getGuilds(), 0xdf0101);
			for (int i = 5; i > 0; i--) {
				if (i != 1)
					System.out.println("Bot stops in " + i + " seconds.");
				if (i == 1)
					System.out.println("Bot stops in " + i + " second.");
				Thread.sleep(1000);
			}
			shardMan.setStatus(OnlineStatus.OFFLINE);
			shardMan.shutdown();
			System.exit(0);
		} catch (InterruptedException e) {
			logger.sendLogError("", e, shardMan.getGuilds());
		}
	}

	public static CommandManager getCmdMan() {
		return cmdMan;
	}

}
