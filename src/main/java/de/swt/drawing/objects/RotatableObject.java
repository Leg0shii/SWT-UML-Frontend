package de.swt.drawing.objects;

import de.swt.gui.GUIManager;

import java.awt.*;

public abstract class RotatableObject extends DrawableObject{
    public int startYOffset;
    public int endYOffset;
    public int startXOffset;
    public int endXOffset;
    public boolean switchSides;

    public RotatableObject(Color color, double scale, String description, GUIManager guiManager) {
        super(color, scale, description, guiManager);
    }

    public void updateComponent(String description, double scale, Color color, int startYOffset, int endYOffset, boolean switchSides){
        this.description = description;
        this.scale = scale;
        this.color = color;
        this.startYOffset = startYOffset;
        this.endYOffset = endYOffset;
        this.switchSides = switchSides;
        this.repaint();
    }
}
