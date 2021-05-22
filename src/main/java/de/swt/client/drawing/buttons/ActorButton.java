package de.swt.client.drawing.buttons;

import de.swt.client.drawing.objects.Actor;
import de.swt.client.gui.GUIManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ActorButton extends DrawableObjectButton {
    private int width;
    private int height;

    public ActorButton(GUIManager guiManager) {
        super(guiManager, "Actor");
    }

    @Override
    void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                guiManager.addToDrawPanel(new Actor(50, 50, color, description, scale));
            }
        });
    }

    @Override
    void calculateWidthAndHeight(Graphics2D g2d) {
        textWidth = g2d.getFontMetrics().stringWidth(this.description);
        textHeight = g2d.getFontMetrics().getHeight();
        width = (int) (textWidth * scale);
        height = width * 2;
        setPreferredSize(new Dimension(width + 2 * offset, textHeight + height + 2 * offset));
        this.revalidate();
    }

    @Override
    void drawButton(Graphics2D g2d) {
        calculateWidthAndHeight(g2d);
        g2d.setStroke(new BasicStroke((float) (scale * 2)));
        // Head
        g2d.drawOval(offset + width / 4, offset + width / 4, width / 2, width / 2);
        // Body
        g2d.drawLine(offset + width / 2, offset + 3 * width / 4, offset + width / 2, offset + 3 * height / 4);
        // Legs
        g2d.drawLine(offset + width / 2, offset + 3 * height / 4, offset, offset + height);
        g2d.drawLine(offset + width / 2, offset + 3 * height / 4, offset + width, offset + height);
        // Arms
        g2d.drawLine(offset + width / 2, offset + 7 * width / 8, offset, offset + 3 * height / 8);
        g2d.drawLine(offset + width / 2, offset + 7 * width / 8, offset + width, offset + 3 * height / 8);
        // Description
        g2d.drawString(description, offset + width / 2 - textWidth / 2, offset + height + textHeight);
    }
}
