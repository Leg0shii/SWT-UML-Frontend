package de.swt.drawing.buttons;

import de.swt.gui.GUIManager;

import javax.swing.*;
import java.awt.*;

public abstract class DrawableObjectButton extends JComponent {
    public final GUIManager guiManager;
    public String description;
    public final double scale;
    public final Color color;
    public int offset;
    public int textWidth;
    public int textHeight;

    public DrawableObjectButton(GUIManager guiManager, String description) {
        setBorder(BorderFactory.createEtchedBorder());
        this.guiManager = guiManager;
        this.description = description;
        this.scale = 1.0;
        this.color = Color.BLACK;
        this.offset = 5;
        setupListeners();
    }

    abstract void setupListeners();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (g2d.getFont().getSize() * scale)));
        drawButton(g2d);
    }

    abstract void calculateWidthAndHeight(Graphics2D g2d);

    abstract void drawButton(Graphics2D g2d);
}
