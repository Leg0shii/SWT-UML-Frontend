package de.swt.drawing.objects;

import de.swt.drawing.buttonGUIS.StandardGUI;
import de.swt.gui.GUI;
import org.kordamp.ikonli.materialdesign2.MaterialDesignT;
import org.kordamp.ikonli.swing.FontIcon;

import java.awt.*;

public class ThumbDown extends DrawableObject{
    private int width;
    private transient FontIcon icon;

    public ThumbDown(int x, int y, Color color, double scale, String description) {
        super(color, scale, description);
        setBounds(x, y, 1, 1);
    }

    @Override
    void drawFunction(Graphics2D g2d) {
        setupIcon();
        calculateWidthAndHeight(g2d);
        icon.setIconColor(color);
        icon.setIconSize((int) (width * scale));
        icon.paintIcon(this, g2d, offset, offset);
    }

    private void setupIcon(){
        icon = new FontIcon();
        icon.setIkon(MaterialDesignT.THUMB_DOWN);
    }

    @Override
    void calculateWidthAndHeight(Graphics2D g2d) {
        textWidth = g2d.getFontMetrics().stringWidth(this.description);
        textHeight = g2d.getFontMetrics().getHeight();
        width = 25;
        setBounds(super.getX(), super.getY(), (int) (offset + width * scale), (int) (offset + width * scale));
    }

    @Override
    GUI createPopup() {
        return new StandardGUI(guiManager, this);
    }
}
