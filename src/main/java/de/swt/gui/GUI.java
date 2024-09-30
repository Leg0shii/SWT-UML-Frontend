package de.swt.gui;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Setter
@Getter
public abstract class GUI extends JPanel {
    private GUIManager guiManager;
    private JPanel mainPanel;

    public GUI(GUIManager guiManager) {
        super();
        this.guiManager = guiManager;
        this.setLayout(new GridLayout(1, 1));
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setupListeners();
        }).start();

    }

    abstract public void updateGUI();

    abstract public void setupListeners();

    public void setupPopupOnTop(JButton button, GUI popup) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(popup);
        button.setComponentPopupMenu(popupMenu);
        popupMenu.pack();
        popupMenu.setVisible(true);
        popupMenu.setVisible(false);
        button.addActionListener(e -> button.getComponentPopupMenu().show(button, button.getWidth() / 2 - popupMenu.getWidth() / 2, button.getHeight() - popupMenu.getHeight()));
    }

    public void setupPopupBelow(JButton button, GUI popup) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(popup);
        button.setComponentPopupMenu(popupMenu);
        popupMenu.pack();
        popupMenu.setVisible(true);
        popupMenu.setVisible(false);
        button.addActionListener(e -> button.getComponentPopupMenu().show(button, button.getWidth() / 2 - popupMenu.getWidth() / 2, 0));
    }

    public void setupPopupToTheRight(JButton button, GUI popup) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(popup);
        button.setComponentPopupMenu(popupMenu);
        popupMenu.pack();
        popupMenu.setVisible(true);
        popupMenu.setVisible(false);
        button.addActionListener(e -> button.getComponentPopupMenu().show(button, button.getWidth(), button.getHeight() / 2 - popupMenu.getHeight() / 2));
    }

    public void setupMatchingPopup(JButton button, GUI popup, GUI match) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(popup);
        button.setComponentPopupMenu(popupMenu);
        popupMenu.pack();
        popupMenu.setPreferredSize(new Dimension(match.getWidth(), match.getHeight()));
        button.addActionListener(e -> button.getComponentPopupMenu().show(match, 0, 0));
    }
}
