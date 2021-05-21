package de.swt.drawing.objects;

import de.swt.drawing.buttonGUIS.StandardGUI;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;

import javax.swing.*;
import java.awt.*;

public class Actor extends DrawableObject {
    private int width;
    private int height;
    private int verticalOffset;

    public Actor(int x, int y, Color color, String description, double scale, GUIManager guiManager) {
        super(color, scale, description, guiManager);
        setBounds(x, y, 1, 1);
        verticalOffset = offset;
    }

    @Override
    void drawFunction(Graphics2D g2d) {
        calculateWidthAndHeight(g2d);
        g2d.setStroke(new BasicStroke((float) (scale * 2)));
        // Head
        g2d.drawOval(offset + width / 4, verticalOffset + width / 4, width / 2, width / 2);
        // Body
        g2d.drawLine(offset + width / 2, verticalOffset + 3 * width / 4, offset + width / 2, verticalOffset + 3 * height / 4);
        // Legs
        g2d.drawLine(offset + width / 2, verticalOffset + 3 * height / 4, offset, verticalOffset + height);
        g2d.drawLine(offset + width / 2, verticalOffset + 3 * height / 4, offset + width, verticalOffset + height);
        // Arms
        g2d.drawLine(offset + width / 2, verticalOffset + 7 * width / 8, offset, verticalOffset + 3 * height / 8);
        g2d.drawLine(offset + width / 2, verticalOffset + 7 * width / 8, offset + width, verticalOffset + 3 * height / 8);
        // Description
        g2d.drawString(description, offset + width / 2 - textWidth / 2, verticalOffset + height + textHeight);
    }

    @Override
    void calculateWidthAndHeight(Graphics2D g2d) {
        textWidth = g2d.getFontMetrics().stringWidth(this.description);
        textHeight = g2d.getFontMetrics().getHeight();
        width = (int) (50 * scale);
        height = width * 2;
        if (textWidth < width) {
            this.setBounds(super.getX(), super.getY(), width + 2 * verticalOffset, textHeight + height + 2 * verticalOffset);
            offset = verticalOffset;
        } else {
            this.setBounds(super.getX(), super.getY(), textWidth + 2 * verticalOffset, textHeight + height + 2 * verticalOffset);
            offset = (textWidth - width) / 2;
        }
    }

    @Override
    GUI createPopup() {
        StandardGUI gui = new StandardGUI(guiManager, this);
        return gui;
    }
}
