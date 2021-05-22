package de.swt.client.gui.workspace;

import de.swt.client.gui.GUI;
import de.swt.client.gui.GUIManager;

import javax.swing.*;

public class SubmitTaskPanel extends GUI {
    private JPanel mainPanel;
    public JButton yesButton;
    public JButton noButton;
    private JLabel checkLabel;

    public SubmitTaskPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Sicher?", "Ja", "Nein");
            case ENGLISH -> setupGUI("Sure?", "Yes", "No");
        }

        setupListeners();
    }

    private void setupGUI(String check, String yes, String no) {
        this.checkLabel.setText(check);
        this.yesButton.setText(yes);
        this.noButton.setText(no);
    }

    public void updateGUI() {

    }

    private void setupListeners() {

    }

    private void initForAccountType() {

    }

    // TODO: Implemented by other Group
    public void yesFunction(){

    }
}
