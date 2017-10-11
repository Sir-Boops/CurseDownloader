package me.boops.cursedownloader;

import java.net.URL;

import me.boops.cursedownloader.Config.Config;
import me.boops.cursedownloader.initals.InitExtract;
import me.boops.cursedownloader.initals.InitFetch;
import me.boops.cursedownloader.initals.InitHelp;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		// Check for settings
		
		for(int i = 0; i < args.length; i++) {
			
			// Check for dest
			if(args[i].equalsIgnoreCase("--folder-name")) {
				
				// Set full path for the folder
				Config.folder = (args[i + 1] + "\\");
				
			}
			
		}
		
		// Check for a run command
		
		for(int i = 0; i < args.length; i++) {
			
			// Check if they want help
			if(args[i].equalsIgnoreCase("--help")) {
				
				new InitHelp();
				return;
				
			}
			
			// Check for extract flag
			if(args[i].equalsIgnoreCase("--extract")) {
				
				new InitExtract(args[i + 1]);
				return;
				
			}
			
			// Check for fetch
			if(args[i].equalsIgnoreCase("--fetch")) {
				
				new InitFetch(new URL(args[i + 1]));
				return;
				
			}
			
			if((i + 1) == args.length) {
				
				// If all else fails
				System.out.println("--help for help");
				return;
				
			}
			
		}
		
		// If no args are set
		if(args.length == 0) {
			System.out.println("--help for help");
			return;
		}
		
	}
}
