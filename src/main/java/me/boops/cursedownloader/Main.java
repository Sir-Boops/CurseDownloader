package me.boops.cursedownloader;

import me.boops.cursedownloader.initals.InitExtract;
import me.boops.cursedownloader.initals.InitFetch;
import me.boops.cursedownloader.initals.InitHelp;

public class Main {
	
	public static void main(String[] args) throws Exception{
		
		// Check for help, extract then fetch
		
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
				
				new InitFetch(args[i + 1]);
				return;
				
			}
			
			if((i + 1) == args.length) {
				
				// If all else fails
				System.out.println("--help for help");
				return;
				
			}
			
			
		}
	}
}
