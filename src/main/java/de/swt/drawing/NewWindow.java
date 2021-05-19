package de.swt.drawing;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class NewWindow{

	MyFrame myframe = new MyFrame();
	
	NewWindow() {
			
		myframe.setTitle("Workplace");
		myframe.setSize(1920,1080);
		myframe.setLayout(null);
		myframe.setDefaultCloseOperation(MyFrame.DISPOSE_ON_CLOSE);
		myframe.setVisible(true);
		JPanel panel = new JPanel();
		panel.setSize(1920, 1080);
	}
	
	
}
