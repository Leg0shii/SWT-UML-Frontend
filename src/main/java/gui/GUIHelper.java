package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUIHelper extends JPanel {
    public void colorComponents(Component[] components, Color[] colorScheme) {
        int panelCounter = 0;
        for (Component component : components) {
            switch (component.getClass().getSimpleName()) {
                case "JPanel" -> {
                    if (panelCounter < 1) {
                        component.setBackground(colorScheme[0]);
                    } else {
                        component.setBackground(colorScheme[1]);
                    }
                    panelCounter++;
                }
                case "JButton" -> {
                    component.setBackground(colorScheme[3]);
                    component.setForeground(colorScheme[2]);
                }
                case "JLabel" -> component.setForeground(colorScheme[2]);
            }
        }
    }

    public Component[] getAllComponents(Container container, List<Component> list) {
        for (Component component : container.getComponents()) {
            list.add(component);
            if (component instanceof Container) {
                getAllComponents((Container) component, list);
            }
        }
        return list.toArray(new Component[0]);
    }
}
