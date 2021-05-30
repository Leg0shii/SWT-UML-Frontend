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
    public transient Popup popup;
    public transient PopupFactory factory;
    public transient int popupCounter;
    public transient GUIManager guiManager;
    public transient GUI popupPanel;
    private final int[] id;
    private DrawableObject thisObject;

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
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (popupCounter % 2 == 0) {
                    popupPanel = createPopup();
                    Point point = new Point(getX() + getWidth(), getY());
                    SwingUtilities.convertPointToScreen(point, getParent());
                    popup = factory.getPopup(guiManager, popupPanel, point.x, point.y);
                    popup.show();
                } else {
                    popup.hide();
                    popupPanel.closeAllPopups();
                }
                popupCounter++;
            }
        });

        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (wantsToChange) {
                    guiManager.syncSingleObject(thisObject);
                }
            }
        });
    }

    public void init(GUIManager guiManager) {
        this.guiManager = guiManager;
        this.wantsToChange = false;
        this.factory = PopupFactory.getSharedInstance();
        this.popupCounter = 0;
        id[0] = guiManager.getClient().userid;
        id[1] = guiManager.increaseObjectCounter();
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

    public void closeAllPopups() {
        if (popup != null) {
            popup.hide();
            popupPanel.closeAllPopups();
            popupCounter = 0;
        }
    }

    public int[] getID() {
        return id;
    }

    abstract void calculateWidthAndHeight(Graphics2D g2d);

    abstract GUI createPopup();

}
