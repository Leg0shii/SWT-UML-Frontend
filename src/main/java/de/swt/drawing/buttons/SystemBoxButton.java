package de.swt.drawing.buttons;

import de.swt.drawing.objects.SystemBox;
import de.swt.gui.GUIManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SystemBoxButton extends DrawableObjectButton{
    private int width;
    private int height;
    public SystemBoxButton(GUIManager guiManager) {
        super(guiManager, "System Box");
        this.width = 100;
        this.height = 100;
    }

    @Override
    void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                guiManager.addToDrawPanel(new SystemBox(50, 50, color, 1, description));
            }
        });
    }

    @Override
    void calculateWidthAndHeight(Graphics2D g2d) {
        this.textWidth = g2d.getFontMetrics().stringWidth(this.description);
        this.textHeight = g2d.getFontMetrics().getHeight();
        this.setPreferredSize(new Dimension(width + 2 * offset, textHeight + height + 2 * offset));
    }

    @Override
    void drawButton(Graphics2D g2d) {
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
        g2d.drawString(description,leftX,topY);
    }
}
