package de.swt.gui.classroom;

import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.course.Course;
import de.swt.logic.user.User;
import de.swt.util.Client;
import de.swt.util.Language;
import de.swt.util.NextDate;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class CreateClassroomPanel extends GUI {
    private JPanel mainPanel;
    private JComboBox<String> gradeComboBox;
    private JTextField gradeNameTextField;
    private JTextField teacherTextField;
    private JTextField dateTextField;
    public JButton doneButton;
    public JButton cancelButton;
    private JLabel gradeLabel;
    private JLabel gradeNameLabel;
    private JLabel teacherLabel;
    private JLabel dateLabel;

    public CreateClassroomPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.setBorder(BorderFactory.createEtchedBorder());
        switch (guiManager.language) {
            case GERMAN -> this.setupGUI("Klassenstufe", "Klassenname", "Lehrer", "Termin",  "Fertig", "Abbrechen");
            case ENGLISH -> this.setupGUI("Grade", "Grade name", "Teacher", "Date",  "Done", "Cancel");
        }
    }

    private void setupGUI(String grade, String gradeName, String teacher, String date,  String done, String cancel) {
        this.gradeLabel.setText(grade);
        this.gradeNameLabel.setText(gradeName);
        this.teacherLabel.setText(teacher);
        this.dateLabel.setText(date);
        this.doneButton.setText(done);
        this.cancelButton.setText(cancel);
        this.gradeComboBox.addItem("10");
        this.gradeComboBox.addItem("11");
        this.gradeComboBox.addItem("12");
    }

    public String getGradeName() {
        return gradeNameTextField.getText();
    }

    public String getTeacher() {
        return teacherTextField.getText();
    }

    public String getDate() {
        return dateTextField.getText();
    }

    public void doneFunction() {

        Course newCourse = new Course();

        newCourse.setGrade(gradeComboBox.getSelectedIndex() + 10);
        newCourse.setTeacherID(Integer.parseInt(getTeacher()));
        newCourse.setDates(NextDate.getDateFromString(getDate()));
        newCourse.setName(getGradeName());

        try { guiManager.getClient().server.sendCourse(newCourse, 0, true); }
        catch (RemoteException e) { e.printStackTrace(); }

        guiManager.getClient().courseManager.cacheAllCourseData();
        guiManager.classroomGUI.updateGradePanels();
    }
}
