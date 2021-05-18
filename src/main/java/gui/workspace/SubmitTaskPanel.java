package gui.workspace;

import gui.GUI;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SubmitTaskPanel extends GUI {
    private JPanel mainPanel;
    public JButton yesButton;
    public JButton noButton;
    private JLabel checkLabel;

    public SubmitTaskPanel(Language language, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        switch (language) {
            case GERMAN -> setupGUI("Sicher?", "Ja", "Nein");
            case ENGLISH -> setupGUI("Sure?", "Yes", "No");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
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
