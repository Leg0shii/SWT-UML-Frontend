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
    private JTextField studentTextField;
    public JButton addButton;
    public JButton doneButton;
    public JButton cancelButton;
    private JLabel gradeLabel;
    private JLabel gradeNameLabel;
    private JLabel teacherLabel;
    private JLabel dateLabel;
    private JLabel studentLabel;
    private Course course;

    public CreateClassroomPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.setBorder(BorderFactory.createEtchedBorder());
        switch (guiManager.language) {
            case GERMAN -> this.setupGUI("Klassenstufe", "Klassenname", "Lehrer", "Termin", "Schüler", "Hinzufügen", "Fertig", "Abbrechen");
            case ENGLISH -> this.setupGUI("Grade", "Grade name", "Teacher", "Date", "Student", "Add", "Done", "Cancel");
        }
    }

    private void setupGUI(String grade, String gradeName, String teacher, String date, String student, String add, String done, String cancel) {
        this.gradeLabel.setText(grade);
        this.gradeNameLabel.setText(gradeName);
        this.teacherLabel.setText(teacher);
        this.dateLabel.setText(date);
        this.studentLabel.setText(student);
        this.addButton.setText(add);
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

    public String getStudent() {
        return studentTextField.getText();
    }

    public void doneFunction() {
        Client client = guiManager.getClient();
        if (course == null) {
            course = new Course();
        }
        course.setGrade(gradeComboBox.getSelectedIndex() + 10);
        course.setTeacherID(Integer.parseInt(getTeacher()));
        course.setDates(NextDate.getDateFromString(getDate()));
        course.setName(getGradeName());

        try {
            guiManager.getClient().server.sendCourse(course, 0, true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addFunction() {
        if (course == null) {
            course = new Course();
        }
        User user = guiManager.getClient().userManager.getUserHashMap().get(Integer.parseInt(getStudent()));
        guiManager.getClient().userManager.setSingleCourse(user,course.getId());
        try {
            guiManager.getClient().server.sendUser(user,-1,true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
