package de.swt.drawing.buttons;

import de.swt.drawing.objects.Arrow;
import de.swt.gui.GUIManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ArrowButton extends DrawableObjectButton{
    public int width;
    public int height;
    public int startX;
    public int startY;
    public int endX;
    public int endY;
    public final int startYOffset;
    public final int endYOffset;
    public final int arrowHeadWidth;
    public final boolean arrowHeadOnRightSide;

    public ArrowButton(GUIManager guiManager) {
        super(guiManager, "Arrow");
        this.arrowHeadWidth = 10;
        this.startYOffset = (int) (arrowHeadWidth/2 * scale);
        this.endYOffset = (int) (arrowHeadWidth/2 * scale);
        this.arrowHeadOnRightSide = true;
    }

    @Override
    void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                guiManager.addToDrawPanel(new Arrow(50, 50, color, scale, description));
            }
        });
    }

    @Override
    void calculateWidthAndHeight(Graphics2D g2d) {
        this.textWidth = g2d.getFontMetrics().stringWidth(this.description);
        this.textHeight = g2d.getFontMetrics().getHeight();
        this.width = (int) (100 * scale);
        this.height = (int) ((endYOffset - startYOffset + arrowHeadWidth) * scale);
        this.startX = offset;
        this.startY = offset + startYOffset;
        this.endX = offset + width;
        this.endY = offset + endYOffset;
        this.setPreferredSize(new Dimension(width + 2 * offset, textHeight/2 + height + 2 * offset));
    }

    @Override
    void drawButton(Graphics2D g2d) {
        BasicStroke stroke = new BasicStroke((float) scale);
        g2d.setStroke(stroke);
        calculateWidthAndHeight(g2d);
        g2d.drawLine(startX, startY, endX, endY);
        g2d.drawString(description,offset + width/2 - textWidth/2,offset + height + textHeight/2);
        if (arrowHeadOnRightSide) {
            g2d.drawLine(endX, endY, endX - arrowHeadWidth / 2, endY - arrowHeadWidth / 2);
            g2d.drawLine(endX, endY, endX - arrowHeadWidth / 2, endY + arrowHeadWidth / 2);
        } else {
            g2d.drawLine(startX, startY, endX + arrowHeadWidth / 2, endY - arrowHeadWidth / 2);
            g2d.drawLine(startX, startY, endX + arrowHeadWidth / 2, endY + arrowHeadWidth / 2);
        }
    }
}
