package de.swt.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JPanel {
    public GUIManager guiManager;
    public List<Popup> popups;
    public List<Integer> popupCounter;
    public List<GUI> childrenGUI;
    public PopupFactory factory;

    public GUI(GUIManager guiManager) {
        this.guiManager = guiManager;
        this.popups = new ArrayList<>();
        this.popupCounter = new ArrayList<>();
        this.childrenGUI = new ArrayList<>();
        this.factory = new PopupFactory();
    }

    public void incrementPopupCounter(int index) {
        int temp = popupCounter.get(index);
        temp++;
        popupCounter.set(index, temp);
    }

    public void initPopups(int n) {
        for (int i = 0; i < n; i++) {
            this.popupCounter.add(0);
            this.popups.add(factory.getPopup(this, new JLabel(), this.getX(), this.getY()));
        }
    }

    public void colorComponents(Component[] components, Color[] colorScheme, int panelCounterStart) {
        int panelCounter = panelCounterStart;
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
                case "JRadioButton" -> {
                    component.setBackground(colorScheme[1]);
                    component.setForeground(colorScheme[2]);
                }
            }
        }
    }

    public Component[] getAllComponents(Container container, List<Component> list) {
        for (Component component : container.getComponents()) {
            if (component instanceof GUI) {
                continue;
            }
            list.add(component);
            if (component instanceof Container) {
                getAllComponents((Container) component, list);
            }
        }
        return list.toArray(new Component[0]);
    }

    public GUI[] getAllChildrenGUI(Container container, List<GUI> list) {
        for (Component component : container.getComponents()) {
            if (component instanceof GUI) {
                list.add((GUI) component);
            }
            if (component instanceof Container) {
                getAllChildrenGUI((Container) component, list);
            }
        }
        return list.toArray(new GUI[0]);
    }

    public void closeAllPopups() {
        for (Popup popup : popups) {
            popupCounter.set(popups.indexOf(popup), 0);
            popup.hide();
        }
        for (GUI childGUI : getAllChildrenGUI(this, new ArrayList<>())) {
            childGUI.closeAllPopups();
        }
    }
}
