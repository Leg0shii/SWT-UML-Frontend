package gui.classroom;

import gui.GUIManager;
import gui.GUI;
import util.Language;
import util.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EditClassroomPanel extends GUI {
    private JPanel mainPanel;
    public JButton doneButton;
    public JButton cancelButton;
    private JTextField studentTextField;
    private JComboBox<String> studentComboBox;
    private JLabel studentComboBoxLabel;
    private JLabel studentTextFieldLabel;

    public EditClassroomPanel(Color[] colors, Language language){
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.setBorder(BorderFactory.createEtchedBorder());
        switch (language) {
            case german -> this.setupGUI("<html>Schüler <br> hinzufügen</html>", "<html>Schüler <br> entfernen</html>", "Übernehmen", "Abbrechen");
            case english -> this.setupGUI("Add student", "Remove student", "Commit", "Cancel");
        }
        this.colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
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
            this.studentComboBox.addItem(student.getID());
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
