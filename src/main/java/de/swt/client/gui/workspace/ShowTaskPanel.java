package de.swt.client.gui.workspace;

import de.swt.client.gui.GUI;
import de.swt.client.gui.GUIManager;

import javax.swing.*;

public class ShowTaskPanel extends GUI {
    private JPanel mainPanel;
    private JLabel taskLabel;

    public ShowTaskPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        setupListeners();
    }

    private void setupGUI() {
    }

    public void updateGUI(String task) {
        this.taskLabel.setText(task);
    }

    private void setupListeners() {
    }

    private void initForAccountType() {

    }
}
