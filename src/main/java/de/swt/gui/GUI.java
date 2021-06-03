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

    public GUI(GUIManager guiManager){
        this.guiManager = guiManager;
        this.setLayout(new GridLayout(1,1));
        this.add(mainPanel);
        setupListeners();
    }

    abstract public void updateGUI();

    abstract public void setupListeners();

    public void setupStandardPopup(JButton button, GUI popup){
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(popup);
        button.setComponentPopupMenu(popupMenu);
        button.addActionListener(e -> button.getComponentPopupMenu().show(button, 0, 0));
    }
}
