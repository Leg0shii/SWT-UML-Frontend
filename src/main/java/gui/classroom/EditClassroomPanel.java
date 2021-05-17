package gui.classroom;

import gui.GUIHelper;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EditClassroomPanel extends GUIHelper {
    private JPanel mainpanel;
    public JButton donebt;
    public JButton cancelbt;
    private JTextField studentf;
    private JComboBox<String> studentcb;
    private JLabel studentcblb;
    private JLabel studenttflb;

    public EditClassroomPanel(Language language, String[] students, Color[] colors){
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainpanel);
        this.setBorder(BorderFactory.createEtchedBorder());

        switch (language){
            case german:
                this.setupGUI("Schüler hinzufügen", "Schüler entfernen", "Übernehmen", "Abbrechen");
                break;
            case english:
                this.setupGUI("Add student", "Remove student", "Commit", "Cancel");
                break;
        }
        for (String temp : students){
            studentcb.addItem(temp);
        }
        this.colorComponents(this.getAllComponents(this, new ArrayList<>()), colors);
    }

    public String getStudentToAdd(){
        return studentf.getText();
    }

    public String getStudentToRemove(){
        return (String) studentcb.getSelectedItem();
    }

    private void setupGUI(String student, String students, String done, String cancel){
        this.studenttflb.setText(student);
        this.studentcblb.setText(students);
        this.donebt.setText(done);
        this.cancelbt.setText(cancel);
    }

    // TODO: Other Group adds this Function
    public void doneFunction() {
    }
}
