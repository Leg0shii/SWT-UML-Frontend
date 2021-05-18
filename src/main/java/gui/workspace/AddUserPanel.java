package gui.workspace;

import gui.GUI;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AddUserPanel extends GUI {
    private JPanel mainPanel;
    public JButton addButton;
    private JTextField studentTextField;
    private JLabel headerLabel;
    private JLabel studentLabel;

    public AddUserPanel(Language language, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        switch (language) {
            case GERMAN -> setupGUI("Schüler hinzufügen", "Schüler-ID", "Hinzufügen");
            case ENGLISH -> setupGUI("Add student", "Student ID", "Add");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
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
