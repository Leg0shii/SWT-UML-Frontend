import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUIHelper extends JPanel {
    public void colorComponents(Component[] components, Color[] colorScheme) {
        for (Component component : components) {
            switch (component.getClass().getSimpleName()) {
                case "JPanel": {
                    component.setBackground(colorScheme[1]);
                    break;
                }
                case "JButton": {
                    component.setBackground(colorScheme[3]);
                    component.setForeground(colorScheme[2]);
                    break;
                }
                case "JLabel": {
                    component.setForeground(colorScheme[2]);
                    break;
                }
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
