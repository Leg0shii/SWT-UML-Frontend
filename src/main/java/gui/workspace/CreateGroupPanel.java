package gui.workspace;

import gui.GUI;
import gui.GUIManager;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CreateGroupPanel extends GUI {
    private JPanel mainPanel;
    private JTextField numberTextField;
    private JTextField sizeTextField;
    private JTextField durationTextField;
    private JButton createButton;
    public JButton cancelButton;
    private JLabel headerLabel;
    private JLabel numberLabel;
    private JLabel sizeLabel;
    private JLabel durationLabel;
    private JLabel unitLabel;

    public CreateGroupPanel(Language language, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        switch (language) {
            case GERMAN -> setupGUI("Gruppen erstellen", "Anzahl", "Gruppengröße", "Öffnungsdauer", "in Minuten", "Erstellen", "Abbrechen");
            case ENGLISH -> setupGUI("Create Groups", "Count", "Size", "Duration", "in minutes", "Create", "Cancel");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
        setupListeners();
    }

    private void setupGUI(String header, String number, String size, String duration, String unit, String create, String cancel) {
        this.headerLabel.setText(header);
        this.numberLabel.setText(number);
        this.sizeLabel.setText(size);
        this.durationLabel.setText(duration);
        this.unitLabel.setText(unit);
        this.createButton.setText(create);
        this.cancelButton.setText(cancel);
    }

    public void updateGUI() {

    }

    // TODO: add Cancel Button Action Listener in PopUp superclass
    private void setupListeners() {
        this.createButton.addActionListener(e -> createFunction());
    }

    private void initForAccountType() {

    }

    public String getNumberText() {
        return numberTextField.getText();
    }

    public String getSizeText() {
        return sizeTextField.getText();
    }

    public String getDurationText() {
        return durationTextField.getText();
    }

    // TODO: Implemented by other Group
    private void createFunction() {

    }
}
