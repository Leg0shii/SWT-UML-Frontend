package de.swt.drawing.objects;

import de.swt.drawing.Draggable;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

public abstract class DrawableObject extends Draggable {
    public Color color;
    public double scale;
    public int offset;
    public String description;
    public int textWidth;
    public int textHeight;
    public transient GUIManager guiManager;
    public transient GUI popupPanel;
    private final int[] id;
    private final DrawableObject thisObject;

    public DrawableObject(Color color, double scale, String description) {
        this.color = color;
        this.scale = scale;
        this.description = description;
        this.offset = 5;
        this.id = new int[2];
        thisObject = this;
    }

    public void updateComponent(String description, double scale, Color color) {
        this.description = description;
        this.scale = scale;
        this.color = color;
        this.repaint();
    }

    abstract void drawFunction(Graphics2D g2d);

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Segoe UI", Font.BOLD, (int) (g2d.getFont().getSize() * scale)));
        g2d.setColor(color);
        drawFunction(g2d);
    }

    private void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JPopupMenu popupMenu = new JPopupMenu();
                popupMenu.add(popupPanel);
                add(popupMenu);
                popupMenu.show(thisObject,0,0);
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (guiManager.getClient().getCurrentSession().getSessionId() == -1 ) {
                    if (wantsToChange) {
                        guiManager.syncSingleObject(thisObject);
                    }
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (guiManager.getClient().getCurrentSession().getSessionId() == -1 ) {
                    guiManager.syncSingleObject(thisObject);
                }
            }
        });
    }

    public void init(GUIManager guiManager) {
        this.guiManager = guiManager;
        this.wantsToChange = false;
        this.popupPanel = createPopup();
        if (Arrays.equals(this.id, new int[2])){
            id[0] = guiManager.getClient().getUserId();
            id[1] = guiManager.increaseObjectCounter();
        }
        super.initListeners();
        setupListeners();
    }

    public void removeInteraction() {
        for (MouseMotionListener listener : this.getMouseMotionListeners()) {
            this.removeMouseMotionListener(listener);
        }
        for (MouseListener listener : this.getMouseListeners()) {
            this.removeMouseListener(listener);
        }
    }

    public int[] getID() {
        return id;
    }

    abstract void calculateWidthAndHeight(Graphics2D g2d);

    abstract GUI createPopup();

}
