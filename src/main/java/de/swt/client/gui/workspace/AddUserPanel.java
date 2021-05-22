package de.swt.client.gui.workspace;

import de.swt.client.gui.GUI;
import de.swt.client.gui.GUIManager;

import javax.swing.*;

public class AddUserPanel extends GUI {
    private JPanel mainPanel;
    public JButton addButton;
    private JTextField studentTextField;
    private JLabel headerLabel;
    private JLabel studentLabel;

    public AddUserPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Schüler hinzufügen", "Schüler-ID", "Hinzufügen");
            case ENGLISH -> setupGUI("Add student", "Student ID", "Add");
        }
        setupListeners();
    }

    private void setupGUI(String header, String student, String add) {
        headerLabel.setText(header);
        studentLabel.setText(student);
        addButton.setText(add);
    }

    public void updateGUI() {

    }

    private void setupListeners() {
        addButton.addActionListener(e -> addFunction());
    }

    private void initForAccountType() {

    }

    public String getStudentID() {
        return studentTextField.getText();
    }

    // TODO: Implemented by Other Group
    private void addFunction() {

    }
}
