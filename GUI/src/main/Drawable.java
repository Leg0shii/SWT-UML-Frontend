package main;

import javax.swing.border.LineBorder;
import java.awt.*;

public class Drawable extends Draggable{

    public Drawable(int x, int y, Color c){
        setBorder(new LineBorder(c, 3));

        setBounds(x, y, 100, 100);
        setBackground(Color.black);
        setOpaque(true);

    }



}
