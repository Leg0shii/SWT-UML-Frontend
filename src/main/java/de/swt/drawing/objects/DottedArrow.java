package de.swt.drawing.objects;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static java.lang.Math.abs;

public class DottedArrow extends Arrow{
    public DottedArrow(int x, int y, Color color, double scale, String description) {
        super(x, y, color, scale, description);
    }

    @Override
    void drawFunction(Graphics2D g2d){
        Graphics2D g2dCopy = (Graphics2D) g2d.create();
        Stroke dashed = new BasicStroke((float) scale, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{9}, 0);
        g2dCopy.setStroke(dashed);
        calculateWidthAndHeight(g2d);
        g2dCopy.drawLine(startX, startY, endX, endY);
        g2dCopy.dispose();
        g2d.setStroke(new BasicStroke((float) scale));
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
}
