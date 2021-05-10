package main;

import javax.swing.*;

public class Main_Test extends JFrame {

    Main_Test(String name, int size){
        setTitle(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0,0,size, size);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }


    public static void main(String[] args) {
        JFrame f = new JFrame("Swing Hello World");

        // by doing this, we prevent Swing from resizing
        // our nice component
        f.setLayout(null);

        Draggable mc = new Draggable();
        f.add(mc);

        f.setSize(500, 500);

        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
        LoginGUI lg = new LoginGUI();
        f.add(lg.$$$getRootComponent$$$());
    }
}
