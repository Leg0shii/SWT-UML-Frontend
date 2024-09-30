package de.swt.drawing;

import de.swt.drawing.objects.Actor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DraggableTest {

    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        Draggable d1 = new Draggable();
    }

    @org.junit.jupiter.api.Test
    void testinitListeners() {

        Draggable d = new Draggable();
        d.initListeners();
        MouseListener[] ml = d.getListeners(MouseListener.class);
        assertTrue(ml.length == 1, "one mouse listener added");
        MouseMotionListener[] mml = d.getListeners(MouseMotionListener.class);
        assertTrue(mml.length == 1, "one mouse motion listener added");

    }

    @org.junit.jupiter.api.Test
    void testCheckifintersects(){

        Draggable d = new Draggable();
        d.initListeners();
        int x = d.getX();
        int y = d.getY();

        Actor a = new Actor(x, y, Color.black, "Hallo", 0.2);

        assertTrue(d.checkIfCrossingComponent(a));



    }


}