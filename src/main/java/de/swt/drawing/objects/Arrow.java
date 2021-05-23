package de.swt.drawing.objects;

import de.swt.drawing.buttonGUIS.RotatingGUI;
import de.swt.gui.GUI;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static java.lang.Math.abs;

public class Arrow extends RotatableObject {
    public int width;
    public int height;
    public int startX;
    public int startY;
    public int endX;
    public int endY;
    public int arrowHeadWidth;
    public final int arrowHeadWidthConstant;

    public Arrow(int x, int y, Color color, double scale, String description) {
        super(color, scale, description);
        this.setBounds(x, y, 1, 1);
        this.arrowHeadWidthConstant = 10;
        this.arrowHeadWidth = (int) (arrowHeadWidthConstant * scale);
        this.startYOffset = (int) (arrowHeadWidth / 2 * scale);
        this.endYOffset = (int) (arrowHeadWidth / 2 * scale);
        this.switchSides = true;
    }

    @Override
    void drawFunction(Graphics2D g2d) {
        BasicStroke stroke = new BasicStroke((float) scale);
        g2d.setStroke(stroke);
        calculateWidthAndHeight(g2d);
        g2d.drawLine(startX, startY, endX, endY);
        AffineTransform transform = g2d.getTransform();
        g2d.translate(offset + width / 2, offset + ((abs(endYOffset - startYOffset) + arrowHeadWidthConstant) / 2));
        if (Math.atan((float) width / (endYOffset - startYOffset)) > 0) {
            g2d.rotate(Math.toRadians(90) - Math.atan((float) width / (endYOffset - startYOffset)));
        } else {
            g2d.rotate(Math.toRadians(270) - Math.atan((float) width / (endYOffset - startYOffset)));
        }
        g2d.translate(-textWidth / 2, textHeight);
        g2d.drawString(description, 0, 0);
        g2d.setTransform(transform);
        AffineTransform transform2 = g2d.getTransform();
        if (switchSides) {
            g2d.translate(endX, endY);
            if (Math.atan((float) width / (endYOffset - startYOffset)) > 0) {
                g2d.rotate(Math.toRadians(90) - Math.atan((float) width / (endYOffset - startYOffset)));
            } else {
                g2d.rotate(Math.toRadians(270) - Math.atan((float) width / (endYOffset - startYOffset)));
            }
            g2d.drawLine(0, 0, -arrowHeadWidth / 2, -arrowHeadWidth / 2);
            g2d.drawLine(0, 0, -arrowHeadWidth / 2, +arrowHeadWidth / 2);
        } else {
            g2d.translate(startX, startY);
            if (Math.atan((float) width / (endYOffset - startYOffset)) > 0) {
                g2d.rotate(Math.toRadians(90) - Math.atan((float) width / (endYOffset - startYOffset)));
            } else {
                g2d.rotate(Math.toRadians(270) - Math.atan((float) width / (endYOffset - startYOffset)));
            }
            g2d.drawLine(0, 0, arrowHeadWidth / 2, -arrowHeadWidth / 2);
            g2d.drawLine(0, 0, arrowHeadWidth / 2, +arrowHeadWidth / 2);
        }
        g2d.setTransform(transform2);
    }

    @Override
    void calculateWidthAndHeight(Graphics2D g2d) {
        this.textWidth = g2d.getFontMetrics().stringWidth(this.description);
        this.textHeight = g2d.getFontMetrics().getHeight();
        this.width = (int) (100 * scale);
        this.arrowHeadWidth = (int) (arrowHeadWidthConstant * scale);
        this.height = (int) ((abs(endYOffset - startYOffset) * scale + arrowHeadWidth));
        this.startX = offset;
        this.startY = offset + startYOffset + arrowHeadWidth / 2;
        this.endX = offset + width;
        this.endY = offset + endYOffset + arrowHeadWidth / 2;
        this.setBounds(super.getX(), super.getY(), width + 2 * offset, textHeight / 2 + height + 2 * offset);
    }

    @Override
    GUI createPopup() {
        return new RotatingGUI(guiManager, this);
    }
}
