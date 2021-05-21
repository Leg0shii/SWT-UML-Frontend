package de.swt.drawing.buttons;

import de.swt.drawing.objects.UseCase;
import de.swt.gui.GUIManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UseCaseButton extends DrawableObjectButton {
    private int width;
    private int height;

    public UseCaseButton(GUIManager guiManager) {
        super(guiManager, "Use Case");
    }

    @Override
    void calculateWidthAndHeight(Graphics2D g2d) {
        this.textWidth = g2d.getFontMetrics().stringWidth(this.description);
        this.textHeight = g2d.getFontMetrics().getHeight();
        this.width = (int) (textWidth + 20 * scale / 2);
        this.height = (int) (textHeight + 20 * scale / 2);
        this.setPreferredSize(new Dimension(width + offset * 2, height + offset * 2));
        this.revalidate();
    }

    @Override
    void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                guiManager.addToDrawPanel(new UseCase(50, 50, 1, description, color));
                setPreferredSize(new Dimension(width + offset * 2, height + offset * 2));
            }
        });
    }

    @Override
    void drawButton(Graphics2D g2d) {
        BasicStroke stroke = new BasicStroke((float) scale);
        calculateWidthAndHeight(g2d);
        g2d.setStroke(stroke);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(offset, offset, width - 1, height - 1);
        g2d.drawString(description, offset + width / 2 - textWidth / 2, offset + height / 2 + textHeight / 4);
    }

}
