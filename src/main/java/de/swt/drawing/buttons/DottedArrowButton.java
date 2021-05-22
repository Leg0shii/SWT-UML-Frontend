package de.swt.drawing.buttons;

import de.swt.drawing.objects.DottedArrow;
import de.swt.gui.GUIManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DottedArrowButton extends ArrowButton {
    public DottedArrowButton(GUIManager guiManager) {
        super(guiManager);
        this.description = "<<includes>>";
    }

    @Override
    void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                guiManager.addToDrawPanel(new DottedArrow(50, 50, color, scale, description));
            }
        });
    }

    @Override
    void drawButton(Graphics2D g2d) {
        Graphics2D g2dCopy = (Graphics2D) g2d.create();
        Stroke dashed = new BasicStroke((float) scale, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{9}, 0);
        g2dCopy.setStroke(dashed);
        calculateWidthAndHeight(g2d);
        g2dCopy.drawLine(startX, startY, endX, endY);
        g2dCopy.dispose();
        BasicStroke stroke = new BasicStroke((float) scale);
        g2d.setStroke(stroke);
        g2d.drawString(description, offset + width / 2 - textWidth / 2, offset + height + textHeight / 2);
        if (arrowHeadOnRightSide) {
            g2d.drawLine(endX, endY, endX - arrowHeadWidth / 2, endY - arrowHeadWidth / 2);
            g2d.drawLine(endX, endY, endX - arrowHeadWidth / 2, endY + arrowHeadWidth / 2);
        } else {
            g2d.drawLine(startX, startY, endX + arrowHeadWidth / 2, endY - arrowHeadWidth / 2);
            g2d.drawLine(startX, startY, endX + arrowHeadWidth / 2, endY + arrowHeadWidth / 2);
        }
    }
}
