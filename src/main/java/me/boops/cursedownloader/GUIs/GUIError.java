package me.boops.cursedownloader.GUIs;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUIError {
	
	public void printError(String msg) {
		
		// Set Window options
		JFrame frame = new JFrame("MainWindow");
		frame.setSize(350, 200);
		frame.setResizable(false);
		frame.setTitle("CurseDownloader - Error");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(0,2));
		
		// Create the panel and set options
		JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		// Add the panels to the frame
		frame.add(panelTop);
		frame.add(panelBottom);
		
		// Add the label and set options
		JLabel chooseLabel = new JLabel();
		chooseLabel.setText("Error: " + msg);
		panelTop.add(chooseLabel);
		
		JButton ok = new JButton();
		ok.setText("OK");
		
		frame.add(ok);
		
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				
				frame.setVisible(false);
				
			}
		});
		
		frame.setVisible(true);
		
	}
}
