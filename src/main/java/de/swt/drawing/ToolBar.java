package de.swt.drawing;

import javax.swing.*;
import java.awt.*;


public class ToolBar extends JFrame {
	

    private JToolBar toolBar = new JToolBar("TaskBar", JToolBar.VERTICAL);
    JButton button1 = new JButton();
    JButton button2 = new JButton();
    JButton button3 = new JButton();
    JButton button4 = new JButton();
    JButton button5 = new JButton();
    private Container cp = null;

    JPanel panel = new JPanel();

	
	ToolBar() {
		
		super("ToolBar");
		setVisible(true);
		setSize(1920, 1080);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
        toolBar.setLayout(new GridLayout(12, 2, 5, 5));
        toolBar.setFloatable(false);
        
        toolBar.add(button1 = new JButton("Button1"));
        button1.setIcon(new ImageIcon(""));				//Image einf�gen
		
        toolBar.add(button2 = new JButton("Button2"));
        button2.setIcon(new ImageIcon(""));				//Image einf�gen
        
        toolBar.add(button3 = new JButton("Button3"));
        button3.setIcon(new ImageIcon(""));				//Image einf�gen
        
        toolBar.add(button4 = new JButton("Button4"));
        button4.setIcon(new ImageIcon(""));				//Image einf�gen
        
        toolBar.add(button5 = new JButton("Button5"));
        button5.setIcon(new ImageIcon(""));				//Image einf�gen
        
        /*Weitere Buttons einf�gen!
         * toolBar.add(buttonN = new JButton("ButtonN"));
           buttonN.setIcon(new ImageIcon(""));	
         * 
         * 
         * 
         * 
         * 
         * 
         * 
         */
        
        
        toolBar.setOrientation(JToolBar.HORIZONTAL);
        
		add(panel);

        cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(toolBar, BorderLayout.EAST);
        
        setVisible(true);
    }

    
}