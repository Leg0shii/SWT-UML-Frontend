package de.swt.gui.classroom;

import de.swt.gui.GUI;
import de.swt.gui.GUIManager;

import javax.swing.*;
import java.util.ArrayList;

public class AdminEditClassroomPanel extends GUI {
    private JPanel mainPanel;
    private JTextField gradeTextField;
    public JButton resetButton;
    public JButton migrateButton;
    public JButton deleteButton;
    public JButton cancelButton;
    private JLabel gradeLabel;

    public AdminEditClassroomPanel(GUIManager guiManager){
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.setBorder(BorderFactory.createEtchedBorder());
        switch (guiManager.language){
            case GERMAN -> setupGUI("neue Klassenstufe", "Zurücksetzen", "Migrieren", "Löschen", "Abbrechen");
            case ENGLISH -> setupGUI("New grade", "Reset", "Migrate", "Delete", "Cancel");
        }
    }

    private void setupGUI(String grade, String reset, String migrate, String delete, String cancel){
        this.gradeLabel.setText(grade);
        this.resetButton.setText(reset);
        this.migrateButton.setText(migrate);
        this.deleteButton.setText(delete);
        this.cancelButton.setText(cancel);
    }

    public String getGrade(){
        return gradeTextField.getText();
    }

    // TODO: Other Group implements this
    public void migrateFunction() {
    }

    // TODO: Other Group implements this
    public void resetFunction() {
    }

    // TODO: Other Group implements this
    public void deleteFunction() {
    }
}
