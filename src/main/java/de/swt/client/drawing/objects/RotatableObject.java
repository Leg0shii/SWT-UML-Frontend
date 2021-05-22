package de.swt.client.drawing.objects;

import java.awt.*;

public abstract class RotatableObject extends DrawableObject{
    public int startYOffset;
    public int endYOffset;
    public boolean switchSides;

    public RotatableObject(Color color, double scale, String description) {
        super(color, scale, description);
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
