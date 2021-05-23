package de.swt.gui.workspace;

import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.util.Language;

import javax.swing.*;

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

    public CreateGroupPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Gruppen erstellen", "Anzahl", "Gruppengröße", "Öffnungsdauer", "in Minuten", "Erstellen", "Abbrechen");
            case ENGLISH -> setupGUI("Create Groups", "Count", "Size", "Duration", "in minutes", "Create", "Cancel");
        }

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
