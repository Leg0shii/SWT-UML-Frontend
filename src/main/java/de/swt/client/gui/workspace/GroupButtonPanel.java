package de.swt.client.gui.workspace;

import de.swt.client.gui.GUI;
import de.swt.client.gui.GUIManager;

import javax.swing.*;

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
