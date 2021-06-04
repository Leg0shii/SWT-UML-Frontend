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
import java.io.Serializable;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public abstract class DrawableObject extends Draggable implements Serializable {
    public Color color;
    public double scale;
    public int offset;
    public String description;
    public int textWidth;
    public int textHeight;
    public transient GUIManager guiManager;
    public transient GUI popupPanel;
    private final int[] id;
    private transient DrawableObject thisObject;
    private transient JPopupMenu popupMenu;

    public DrawableObject(Color color, double scale, String description) {
        this.color = color;
        this.scale = scale;
        this.description = description;
        this.offset = 5;
        this.id = new int[2];
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
        popupMenu = new JPopupMenu();
        popupMenu.add(createPopup());
        thisObject.setComponentPopupMenu(popupMenu);
        popupMenu.pack();
        popupMenu.setVisible(true);
        popupMenu.setVisible(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                thisObject.getComponentPopupMenu().show(thisObject,thisObject.getWidth(),thisObject.getHeight()/2-popupMenu.getHeight()/2);
                }
        });
    }

    public void init(GUIManager guiManager) {
        this.guiManager = guiManager;
        this.thisObject = this;
        this.wantsToChange = false;
        this.popupPanel = createPopup();
        if (Arrays.equals(this.id, new int[2])) {
            id[0] = guiManager.getClient().getUserId();
            id[1] = guiManager.increaseObjectCounter();
        }
        super.initListeners();
        setupListeners();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (guiManager.getClient().getCurrentSession().getSessionId() != -1) {
                    if (wantsToChange) {
                        guiManager.syncSingleObject(thisObject);
                    }
                }
            }
        }, 0, 33);
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
