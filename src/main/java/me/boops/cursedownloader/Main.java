package me.boops.cursedownloader;

import me.boops.cursedownloader.initial.InitGUI;
import me.boops.cursedownloader.initial.InitHELP;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		// By default here we want to use the GUI unless --no-gui is passed
		
		for(int i = 0; i < args.length; i++) {
			
			if(args[i].toLowerCase().equals("--help")) {
				
				// Print the help menu and exit
				new InitHELP();
				
			}
			
			if(args[i].toLowerCase().equals("--no-gui")) {
				
				// Run the program without a GUI
				
			}
			
		}
		
		// Run the program with a GUI as normal
		new InitGUI();
		
	}
}
