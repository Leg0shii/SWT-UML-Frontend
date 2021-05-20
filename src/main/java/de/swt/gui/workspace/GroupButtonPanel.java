package de.swt.gui.workspace;

import de.swt.gui.GUI;
import de.swt.gui.GUIManager;

import javax.swing.*;
import java.util.ArrayList;

public class GroupButtonPanel extends GUI {
    private JPanel mainPanel;
    public JButton watchButton;
    public JButton terminateButton;

    public GroupButtonPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        switch (guiManager.language){
            case GERMAN -> setupGUI("Terminieren", "Zuschauen");
            case ENGLISH -> setupGUI("Terminate", "Watch");
        }

        setupListeners();
    }

    private void setupGUI(String terminate, String watch) {
        this.terminateButton.setText(terminate);
        this.watchButton.setText(watch);
    }

    public void updateGUI() {

    }

    private void setupListeners() {

    }

    private void initForAccountType() {

    }
}
