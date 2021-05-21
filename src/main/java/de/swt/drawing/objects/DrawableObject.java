package de.swt.drawing.objects;

import de.swt.drawing.Draggable;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

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

    public DrawableObject(Color color, double scale, String description) {
        this.color = color;
        this.scale = scale;
        this.description = description;
        this.offset = 5;
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
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (g2d.getFont().getSize() * scale)));
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
    }

    public void init(GUIManager guiManager) {
        this.guiManager = guiManager;
        this.factory = PopupFactory.getSharedInstance();
        this.popupCounter = 0;
        super.initListeners();
        setupListeners();
    }

    public void closeAllPopups() {
        if (popup != null) {
            popup.hide();
            popupPanel.closeAllPopups();
            popupCounter = 0;
        }
    }

    abstract void calculateWidthAndHeight(Graphics2D g2d);

    abstract GUI createPopup();

}
