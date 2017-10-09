package me.boops.cursedownloader.initial;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import me.boops.cursedownloader.GUIs.GUIExtractPack;

public class InitGUI {
	
	public InitGUI() {
		
		// Set Window options
		JFrame frame = new JFrame("MainWindow");
		frame.setSize(350, 200);
		frame.setResizable(false);
		frame.setTitle("CurseDownloader");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(2,0));
		
		// Create the panel and set options
		JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		// Add the panels to the frame
		frame.add(panelTop);
		frame.add(panelBottom);
		
		// Add the label and set options
		JLabel chooseLabel = new JLabel();
		chooseLabel.setText("Welcome, Please choose an option.");
		
		// Add the label to the top frame
		panelTop.add(chooseLabel);
		
		// Add and setup the buttons
		JButton extract = new JButton();
		JButton fetch = new JButton();
		extract.setText("Extract a pack");
		fetch.setText("Fetch a modpack");
		
		extract.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				
				// Off to the extraction window
				frame.setVisible(false);
				new GUIExtractPack();
				
			}
		});
		
		fetch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				
				System.out.println("Gay2!");
				
			}
		});
		
		panelBottom.add(extract);
		panelBottom.add(fetch);
		
		// Finally Show the window
		frame.setVisible(true);
		
	}
}
