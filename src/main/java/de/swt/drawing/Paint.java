package de.swt.drawing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Paint extends JFrame {

    private int x = -5;
    private int y = -5;
    JButton colorChooser;
    Color c;

    public Paint() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);

        colorChooser = new JButton("Color");
        colorChooser.addActionListener(e -> c = JColorChooser.showDialog(null, "Color", Color.BLACK));

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                repaint();
            }
        });


        add(colorChooser, BorderLayout.NORTH);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(c);
        g.fillOval(x, y, 4, 4);
    }
}