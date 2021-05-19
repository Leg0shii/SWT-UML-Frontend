package de.swt.drawing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DrawableUseCase extends Draggable{
    private String description;
    private int scale;
    private int width;
    private int height;
    private int textWidth;
    private int textHeight;

    public DrawableUseCase(int x, int y, int scale, String description) {
        this.setBounds(x, y, scale, scale);
        this.description = description;
        this.scale = scale;
    }

    public void updateComponent(String description, int scale) {
        this.description = description;
        this.scale = scale;
    }

    private void calculateWidthAndHeight(Graphics g) {
        this.textWidth = g.getFontMetrics().stringWidth(this.description);
        this.textHeight = g.getFontMetrics().getHeight();
        this.width = textWidth + 20 * scale / 2;
        this.height = textHeight + 20 * scale / 2;
        this.setBounds(super.getX(), super.getY(), width, height);
    }

    private void setupListeners(){
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickedFunction();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void clickedFunction(){

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, g.getFont().getSize() * scale));
        calculateWidthAndHeight(g);
        g.setColor(Color.BLACK);
        g.drawOval(0, 0, width - 1, height - 1);
        g.drawString(description, width / 2 - textWidth / 2, height / 2 + textHeight / 4);
    }
}
