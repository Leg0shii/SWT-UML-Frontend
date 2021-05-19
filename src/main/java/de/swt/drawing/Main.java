package de.swt.drawing;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        //new MyFrame();                        //Menubar
       // new Paint();                        //Zeichenflaeche

        JFrame tb = new JFrame("Toolbaar");

        ToolBar tbpane = new ToolBar();                        //Toolbar
        tb.add(tbpane);
        tb.setSize(1920,1080);
        tb.setVisible(true);

    }
}	
