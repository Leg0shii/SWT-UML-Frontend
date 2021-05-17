package gui.classroom;

import gui.GUIManager;
import gui.GUI;
import util.AccountType;
import util.Course;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
    private Popup[] popups;
    public int grade;
    private final EditClassroomPanel editClassroomPanel;
    private final AdminEditClassroomPanel adminEditClassroomPanel;

    public GradePanel(Course course, Color[] colors, Language language, AccountType accountType) {
        this.course = course;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        switch (language) {
            case german -> setupGUI("Beitreten", "Bearbeiten", "Lehrer", "Termin");
            case english -> setupGUI("Enter", "Edit", "Teacher", "Date");
        }
        this.colorComponents(this.getAllComponents(this, new ArrayList<>()), colors);
        setupActionListeners(accountType);

        this.editClassroomPanel = new EditClassroomPanel(colors, language);
        this.adminEditClassroomPanel = new AdminEditClassroomPanel(colors, language);
    }

    private void setupGUI(String enter, String edit, String teacher, String date) {
        this.enterButton.setText(enter);
        this.editButton.setText(edit);
        this.teacherLabel.setText(teacher);
        this.dateLabel.setText(date);
    }

    // Both language support (too lazy rn)
    public void updateGUI(Course course) {
        this.gradeHeaderLabel.setText("Klasse " + course.getGrade());
        this.grade = course.getGrade();
        this.thisTeacherLabel.setText(course.getTeacher().getFullName());
        this.nextDateLabel.setText("Next Date");
    }

    public void setupActionListeners(AccountType accountType) {
        PopupFactory popupFactory = new PopupFactory();
        popups = new Popup[1];
        switch (accountType) {
            case admin -> {
                this.editButton.addActionListener(e1 -> {
                    Point point = new Point(this.editButton.getX()-5, 0);
                    SwingUtilities.convertPointToScreen(point, this);
                    adminEditClassroomPanel.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
                    adminEditClassroomPanel.cancelButton.addActionListener(e11 -> popups[0].hide());
                    adminEditClassroomPanel.deleteButton.addActionListener(e12 -> adminEditClassroomPanel.deleteFunction());
                    adminEditClassroomPanel.migrateButton.addActionListener(e13 -> adminEditClassroomPanel.migrateFunction());
                    adminEditClassroomPanel.resetButton.addActionListener(e14 -> adminEditClassroomPanel.resetFunction());
                    popups[0] = popupFactory.getPopup(this, adminEditClassroomPanel, point.x, point.y);
                    popups[0].show();
                });
                this.enterButton.addActionListener(e2 -> this.enterFunction());
            }
            case teacher -> {
                this.editButton.addActionListener(e1 -> {
                    Point point = new Point(this.editButton.getX()-5, 0);
                    SwingUtilities.convertPointToScreen(point, this);
                    editClassroomPanel.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
                    editClassroomPanel.cancelButton.addActionListener(e11 -> popups[0].hide());
                    editClassroomPanel.doneButton.addActionListener(e12 -> {
                        editClassroomPanel.doneFunction();
                        popups[0].hide();
                    });
                    popups[0] = popupFactory.getPopup(this, editClassroomPanel, point.x, point.y);
                    popups[0].show();
                });
                this.enterButton.addActionListener(e2 -> this.enterFunction());
            }
        }
    }

    public void closePopups() {
        for (Popup popup : popups) {
            if (popup != null) {
                popup.hide();
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

    public String getThisTeacher() {
        return thisTeacherLabel.getText();
    }

    public String getGradeHeader() {
        return gradeHeaderLabel.getText();
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
