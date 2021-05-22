package de.swt.client.drawing.objects;

import java.awt.*;

public abstract class ResizableObject extends DrawableObject{
    public int width;
    public int height;
    public ResizableObject(Color color, double scale, String description) {
        super(color, scale, description);
    }

    public void updateComponent(String description, double scale, Color color, int width, int height){
        this.description = description;
        this.scale = scale;
        this.color = color;
        this.width = width;
        this.height = height;
        this.repaint();
    }
}
