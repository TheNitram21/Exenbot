package de.onlinehome.mann.martin.exenbot;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import de.onlinehome.mann.martin.exenbot.commands.CreditsCommand;
import de.onlinehome.mann.martin.exenbot.commands.StopCommand;
import de.onlinehome.mann.martin.exenbot.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.RawGatewayEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SlashCommandManager extends ListenerAdapter {

	private static OkHttpClient client = new OkHttpClient();
	private boolean printResponses;

	public ConcurrentHashMap<String, ServerCommand> commands;
	public static final Permission DEFAULT_PERMISSION = Permission.MESSAGE_WRITE;

	public SlashCommandManager(boolean printResponses) {
		this.commands = new ConcurrentHashMap<>();

		this.commands.put("stop", new StopCommand(Permission.MANAGE_SERVER));
		this.commands.put("temptalk", Exenbot.tempTalkCommand);
		this.commands.put("credits", new CreditsCommand(DEFAULT_PERMISSION));

		this.printResponses = printResponses;
	}

	public static void activateSlashCommands(boolean printResponses, String... json) {
		for (String string : json) {
			Request request = new Request.Builder().url(
					"https://discord.com/api/v8/applications/834039697314545735/guilds/820741090855616582/commands")
					.addHeader("Content-Type", "application/json")
					.addHeader("Authorization", "Bot " + Configuration.getToken())
					.post(RequestBody.create(MediaType.parse("application/json"), string)).build();

			try (Response response = client.newCall(request).execute()) {
				if (printResponses)
					System.out.println(response.body().string());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onRawGateway(RawGatewayEvent event) {
		if (event.getType().equals("INTERACTION_CREATE")) {
			JSONObject interactionPackage = new JSONObject(event.getPackage().toString());
			String id = interactionPackage.getJSONObject("d").get("id").toString();
			String token = interactionPackage.getJSONObject("d").get("token").toString();
			String name = interactionPackage.getJSONObject("d").getJSONObject("data").get("name").toString();

			String callback = this.commands.get(name).performCommand(interactionPackage);

			JSONObject json = new JSONObject("{\"type\": 4, \"data\": {\"content\": \"" + callback + "\"}}");

			Request request = new Request.Builder()
					.url("https://discord.com/api/v8/interactions/" + id + "/" + token + "/callback")
					.addHeader("Content-Type", "application/json")
					.addHeader("Authorization", "Bot " + Configuration.getToken())
					.post(RequestBody.create(MediaType.parse("application/json, charset=utf-8"), json.toString()))
					.build();

			try (Response response = client.newCall(request).execute()) {
				if (printResponses)
					System.out.println(response.body().string());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
