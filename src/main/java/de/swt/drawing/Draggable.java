package de.swt.drawing;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.Serializable;

//https://stackoverflow.com/questions/874360/swing-creating-a-draggable-component

//a draggable JComponent
public class Draggable extends JComponent implements Serializable {

    private volatile int screenX;
    private volatile int screenY;
    private volatile int myX;
    private volatile int myY;

    public Draggable() {
    }

    public void initListeners() {
        //get info on press
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();

                myX = getX();
                myY = getY();
            }
        });
        //drag on drag
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getXOnScreen() - screenX;
                int deltaY = e.getYOnScreen() - screenY;

                setLocation(myX + deltaX, myY + deltaY);
            }
        });
    }
}
