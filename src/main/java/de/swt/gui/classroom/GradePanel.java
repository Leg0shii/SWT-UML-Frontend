package de.swt.gui.classroom;

import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.util.AccountType;
import de.swt.logic.Course;
import de.swt.logic.User;

import javax.swing.*;
import java.awt.*;

public class GradePanel extends GUI {
    private Course course;
    private JPanel mainPanel;
    public JButton enterButton;
    public JButton editButton;
    private JLabel dateLabel;
    private JLabel teacherLabel;
    private JLabel nextDateLabel;
    private JLabel thisTeacherLabel;
    private JLabel gradeHeaderLabel;
    public int grade;
    private final EditClassroomPanel editClassroomPanel;
    private final AdminEditClassroomPanel adminEditClassroomPanel;

    public GradePanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        switch (guiManager.language) {
            case GERMAN -> setupGUI("Beitreten", "Bearbeiten", "Lehrer", "Termin", "Klasse");
            case ENGLISH -> setupGUI("Enter", "Edit", "Teacher", "Date", "Grade");
        }
        setupActionListeners(guiManager.accountType);

        this.editClassroomPanel = new EditClassroomPanel(guiManager);
        this.adminEditClassroomPanel = new AdminEditClassroomPanel(guiManager);
    }

    private void setupGUI(String enter, String edit, String teacher, String date, String grade) {
        this.enterButton.setText(enter);
        this.editButton.setText(edit);
        this.teacherLabel.setText(teacher);
        this.dateLabel.setText(date);
        this.gradeHeaderLabel.setText(grade + " ");
        initPopups(1);
    }

    public void updateGUI(Course course) {
        this.course = course;
        this.gradeHeaderLabel.setText(gradeHeaderLabel.getText().split(" ")[0] + " " + course.getGrade() + " " + course.getName());
        this.grade = course.getGrade();
        this.thisTeacherLabel.setText(course.getTeacher().getFullName());
        this.nextDateLabel.setText(String.valueOf(course.getDates().get(0)));
    }

    public void setupActionListeners(AccountType accountType) {
        switch (accountType) {
            case ADMIN -> {
                this.editButton.addActionListener(e1 -> {
                    Point point = new Point(this.editButton.getX() - 2, 0);
                    SwingUtilities.convertPointToScreen(point, this);
                    adminEditClassroomPanel.setPreferredSize(new Dimension(this.getWidth()+1, this.getHeight()+1));
                    adminEditClassroomPanel.cancelButton.addActionListener(e11 -> popups.get(0).hide());
                    adminEditClassroomPanel.deleteButton.addActionListener(e12 -> adminEditClassroomPanel.deleteFunction());
                    adminEditClassroomPanel.migrateButton.addActionListener(e13 -> adminEditClassroomPanel.migrateFunction());
                    adminEditClassroomPanel.resetButton.addActionListener(e14 -> adminEditClassroomPanel.resetFunction());
                    popups.set(0, factory.getPopup(guiManager, adminEditClassroomPanel, point.x, point.y));
                    popups.get(0).show();
                });
                this.enterButton.addActionListener(e2 -> this.enterFunction());
            }
            case TEACHER -> {
                this.editButton.addActionListener(e1 -> {
                    Point point = new Point(this.editButton.getX() - 2, 0);
                    SwingUtilities.convertPointToScreen(point, this);
                    editClassroomPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
                    editClassroomPanel.cancelButton.addActionListener(e11 -> popups.get(0).hide());
                    editClassroomPanel.doneButton.addActionListener(e12 -> {
                        editClassroomPanel.doneFunction();
                        popups.get(0).hide();
                    });
                    popups.set(0, factory.getPopup(guiManager, editClassroomPanel, point.x, point.y));
                    popups.get(0).show();
                });
                this.enterButton.addActionListener(e2 -> this.enterFunction());
            }
        }
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public String getNextDate() {
        return nextDateLabel.getText();
    }

    public User getThisTeacher() {
        return course.getTeacher();
    }

    public String getGradePlusGradeName() {
        return course.getGrade() + " " + course.getName();
    }

    public EditClassroomPanel getEditClassroomPanel() {
        return editClassroomPanel;
    }

    public AdminEditClassroomPanel getAdminEditClassroomPanel() {
        return adminEditClassroomPanel;
    }

    // TODO: Other Group implements this
    private void enterFunction() {

    }
}
