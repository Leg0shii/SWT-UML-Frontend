package main;

import javax.swing.*;
import java.awt.*;

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
        Main_Test f = new Main_Test("Draggable and mby drawable", 500);

        // by doing this, we prevent Swing from resizing
        // our nice component
        f.setLayout(null);

        Drawable mc = new Drawable(100,100, Color.blue);
        Drawable vc = new Drawable(100,200, Color.GREEN);

        f.add(mc);
        f.add(vc);

        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        System.out.println("Hello Yoost!");
    }
}
