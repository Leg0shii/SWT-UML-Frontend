package de.swt.client.gui.classroom;

import de.swt.client.gui.GUI;
import de.swt.client.gui.GUIManager;
import de.swt.client.clientlogic.User;

import javax.swing.*;
import java.util.ArrayList;

public class EditClassroomPanel extends GUI {
    private JPanel mainPanel;
    public JButton doneButton;
    public JButton cancelButton;
    private JTextField studentTextField;
    private JComboBox<String> studentComboBox;
    private JLabel studentComboBoxLabel;
    private JLabel studentTextFieldLabel;

    public EditClassroomPanel(GUIManager guiManager){
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        switch (guiManager.language) {
            case GERMAN -> this.setupGUI("<html>Schüler <br> hinzufügen</html>", "<html>Schüler <br> entfernen</html>", "Übernehmen", "Abbrechen");
            case ENGLISH -> this.setupGUI("Add student", "Remove student", "Commit", "Cancel");
        }
    }

    private void setupGUI(String student, String students, String done, String cancel){
        this.studentTextFieldLabel.setText(student);
        this.studentComboBoxLabel.setText(students);
        this.doneButton.setText(done);
        this.cancelButton.setText(cancel);
    }

    public void updateGUI(ArrayList<User> students){
        this.studentComboBox.removeAllItems();
        this.studentComboBox.addItem("");
        for (User student : students){
            this.studentComboBox.addItem(student.getId() + "");
        }
    }

    public String getStudentToAdd(){
        return studentTextField.getText();
    }

    public String getStudentToRemove(){
        return (String) studentComboBox.getSelectedItem();
    }

    // TODO: Other Group adds this Function
    public void doneFunction() {
    }
}
