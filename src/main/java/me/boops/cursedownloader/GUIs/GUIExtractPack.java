package me.boops.cursedownloader.GUIs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GUIExtractPack {
	
	public GUIExtractPack() {
		
		// Set Window options
		JFrame frame = new JFrame("MainWindow");
		frame.setSize(700, 75);
		frame.setResizable(false);
		frame.setTitle("CurseDownloader - Extract a pack");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		//Add all the elements to the window
		JLabel label = new JLabel();
		label.setText("ZIP File path: ");
		
		JTextField feild_url = new JTextField();
		feild_url.setPreferredSize(new Dimension(400, 25));
		
		JButton findFile = new JButton();
		JButton extract = new JButton();
		findFile.setText("...");
		extract.setText("Extract");
		
		findFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				
				// Off to the extraction window
				final JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Please Select the zip file");
				chooser.showOpenDialog(findFile);
				
				// Check if the file ends in a .zip
				if(chooser.getSelectedFile().toString().substring(chooser.getSelectedFile().toString().lastIndexOf('.') + 1).equalsIgnoreCase("zip")) {
					// We have a zip file!
					feild_url.setText(chooser.getSelectedFile().toString());
				} else {
					
					new GUIError().printError("Error the item you selected is not a zip file!");
					
				}
				
				
				
			}
		});
		
		extract.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				
				System.out.println("You wish!");
				System.out.println(feild_url.getText());
				
			}
		});
		
		frame.add(label);
		frame.add(feild_url);
		frame.add(findFile);
		frame.add(extract);
		
		// Show the window
		frame.setVisible(true);
		
	}
	
}
