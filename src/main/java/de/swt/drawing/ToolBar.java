package de.swt.drawing;

import javax.swing.*;
import java.awt.*;


public class ToolBar extends JPanel {//mby panel instead of frame?
	

    private JToolBar toolBar = new JToolBar("TaskBar", JToolBar.VERTICAL);
    JButton button1;
    JButton button2;
    JButton button3;
    JButton button4;
    JButton button5;

	
	public ToolBar() {

		//useless if not frame
		//super("ToolBar");
		setSize(1000, 600);
		//setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
        toolBar.setLayout(new GridLayout(12, 2, 5, 5));
        toolBar.setFloatable(false);
        
        toolBar.add(button1 = new JButton("Button1"));
        button1.setIcon(new ImageIcon(""));				//Image einfuegen
		
        toolBar.add(button2 = new JButton("Button2"));
        button2.setIcon(new ImageIcon(""));				//Image einfuegen
        
        toolBar.add(button3 = new JButton("Button3"));
        button3.setIcon(new ImageIcon(""));				//Image einfuegen
        
        toolBar.add(button4 = new JButton("Button4"));
        button4.setIcon(new ImageIcon(""));				//Image einfuegen
        
        toolBar.add(button5 = new JButton("Button5"));
        button5.setIcon(new ImageIcon(""));				//Image einfuegen
        
        /*Weitere Buttons einfuegen!
         * toolBar.add(buttonN = new JButton("ButtonN"));
           buttonN.setIcon(new ImageIcon(""));
         * 
         */
        
        
        toolBar.setOrientation(JToolBar.HORIZONTAL);
        
		this.add(toolBar);
        /*
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(toolBar, BorderLayout.EAST);

        setVisible(true);*/

    }

    
}