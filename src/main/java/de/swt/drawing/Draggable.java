package de.swt.drawing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;

//https://stackoverflow.com/questions/874360/swing-creating-a-draggable-component

//a draggable JComponent
public class Draggable extends JComponent implements Serializable {

    private volatile int screenX;
    private volatile int screenY;
    private volatile int myX;
    private volatile int myY;

    public boolean wantsToChange;


    public Draggable() {
        wantsToChange = false;
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
                wantsToChange = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ArrayList<Component> underlyingComponents = getAllUnderlyingComponents();
                ArrayList<Component> orderedComponents = new ArrayList<>();
                orderedComponents.add(Draggable.this);
                orderedComponents.addAll(underlyingComponents);
                for (Component component : orderedComponents){
                    getParent().add(component);
                }
                wantsToChange = false;
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

    private ArrayList<Component> getAllUnderlyingComponents() {
        ArrayList<Component> components = new ArrayList<>();
        for (Component component : getParent().getComponents()) {
            if (component == this) {
                continue;
            }
            if (components.contains(component)){
                continue;
            }
            if (checkIfCrossingComponent(component)) {
                components.add(component);
            }
        }
        return components;
    }

    public boolean checkIfCrossingComponent(Component component) {
        Rectangle rect1 = component.getBounds();
        Rectangle rect2 = getBounds();
        return rect1.contains(rect2);
    }
}
