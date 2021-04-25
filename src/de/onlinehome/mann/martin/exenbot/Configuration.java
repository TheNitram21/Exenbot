package de.onlinehome.mann.martin.exenbot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Configuration {
	
	private static String token = "";
	
	public static void read() throws FileNotFoundException {
		File file = new File("config.cfg");
		
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = null;
			while((line = reader.readLine()) != null) {
				String[] splittedLine = new String[2];
				splittedLine = line.split(": ", 2);
				
				if(splittedLine[0].equals("TOKEN"))
					token = splittedLine[1];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getToken() {
		return token;
	}
	
}
