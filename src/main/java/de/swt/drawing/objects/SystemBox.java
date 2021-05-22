package de.swt.drawing.objects;

import de.swt.drawing.buttonGUIS.ResizingGUI;
import de.swt.drawing.buttonGUIS.RotatingGUI;
import de.swt.gui.GUI;

import java.awt.*;

public class SystemBox extends ResizableObject {
    public SystemBox(int x, int y, Color color, double scale, String description) {
        super(color, scale, description);
        this.width = 100;
        this.height = 100;
        this.setBounds(x, y, 1, 1);
    }

    @Override
    void drawFunction(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        BasicStroke stroke = new BasicStroke((float) scale);
        calculateWidthAndHeight(g2d);
        int leftX = offset;
        int rightX = offset + width;
        int topY = offset + textHeight;
        int bottomY = offset + textHeight + height;
        g2d.setStroke(stroke);
        g2d.drawLine(leftX, topY, rightX, topY);
        g2d.drawLine(leftX, topY, leftX, bottomY);
        g2d.drawLine(leftX, bottomY, rightX, bottomY);
        g2d.drawLine(rightX, bottomY, rightX, topY);
        g2d.drawString(description, leftX, topY-textHeight/2);
    }

    @Override
    void calculateWidthAndHeight(Graphics2D g2d) {
        this.textWidth = g2d.getFontMetrics().stringWidth(this.description);
        this.textHeight = g2d.getFontMetrics().getHeight();
        this.setBounds(super.getX(), super.getY(), width + 2 * offset, textHeight + height + 2 * offset);
    }

    @Override
    GUI createPopup() {
        return new ResizingGUI(guiManager, this);
    }
}
