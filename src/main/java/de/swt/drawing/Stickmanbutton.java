package de.swt.drawing;

import de.swt.gui.GUIManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Stickmanbutton extends JComponent {

    int width;
    int height;
    GUIManager guiManager;

    public Stickmanbutton(int xx, int yy, GUIManager guiManager){
        setBorder(new LineBorder(Color.black, 2));
        this.width = 50;
        this.height = 100;
        setPreferredSize(new Dimension(width, height));
        this.guiManager = guiManager;
        // setBackground(Color.black);
        //setOpaque(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                guiManager.addToDrawPanel(new Drawable(20, 20, Color.BLACK));
            }
        });

    }

    @Override
    public void paintComponent(Graphics g) {
        int x = 0;
        int y = 0;
        //int width = this.width -1;
        //int height = this.height -1;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.black);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(x,y,width-1,width-1);
        //body
        g2d.drawLine(x+width/2, y+width, x+width/2, y+2*width-20);
        //arm
        g2d.drawLine(x+width/2, y+width+10, x+width, y+width);
        //arm
        g2d.drawLine(x+width/2, y+width+10, x, y+width);
        //leg
        g2d.drawLine(x+width/2, y+2*width-20, x+width, y+2*width);
        //leg
        g2d.drawLine(x+width/2, y+2*width-20, x, y+2*width);
    }
}
