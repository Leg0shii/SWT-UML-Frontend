package de.swt.drawing.objects;

import de.swt.drawing.buttonGUIS.StandardGUI;
import de.swt.gui.GUI;

import java.awt.*;

public class UseCase extends DrawableObject {
    private int width;
    private int height;

    public UseCase(int x, int y, double scale, String description, Color color) {
        super(color, scale, description);
        this.setBounds(x, y, 1, 1);
    }

    @Override
    void drawFunction(Graphics2D g2d) {
        BasicStroke stroke = new BasicStroke((float) scale);
        calculateWidthAndHeight(g2d);
        g2d.setStroke(stroke);
        g2d.drawOval(offset, offset, width - 1, height - 1);
        g2d.drawString(description, offset + width / 2 - textWidth / 2, offset + height / 2 + textHeight / 4);
    }

    @Override
    void calculateWidthAndHeight(Graphics2D g2d) {
        this.textWidth = g2d.getFontMetrics().stringWidth(this.description);
        this.textHeight = g2d.getFontMetrics().getHeight();
        this.width = (int) (textWidth + 20 * scale / 2);
        this.height = (int) (textHeight + 20 * scale / 2);
        this.setBounds(super.getX(), super.getY(), width + 2 * offset, height + 2 * offset);
    }

    @Override
    GUI createPopup() {
        return new StandardGUI(guiManager, this);
    }
}
