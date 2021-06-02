package de.swt.gui;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Setter
@Getter
public abstract class GUI extends JPanel {
    private GUIManager guiManager;
    JPanel mainPanel;

    public GUI(GUIManager guiManager){
        this.guiManager = guiManager;
        this.setLayout(new GridLayout(1,1));
        this.add(mainPanel);
        setupListeners();
    }

    abstract public void updateGUI();

    abstract public void setupListeners();
}
