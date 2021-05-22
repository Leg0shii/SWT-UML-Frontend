package de.swt.gui.workspace;

import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.util.Language;

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
