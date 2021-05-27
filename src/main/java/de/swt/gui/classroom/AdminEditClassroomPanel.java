package de.swt.gui.classroom;

import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.course.Course;
import de.swt.logic.user.User;
import lombok.Setter;

import javax.swing.*;
import java.util.ArrayList;

@Setter
public class AdminEditClassroomPanel extends GUI {
    private JPanel mainPanel;
    private JTextField gradeTextField;
    public JButton resetButton;
    public JButton migrateButton;
    public JButton deleteButton;
    public JButton cancelButton;
    private JLabel gradeLabel;
    private Course course;

    public AdminEditClassroomPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.setBorder(BorderFactory.createEtchedBorder());
        switch (guiManager.language) {
            case GERMAN -> setupGUI("neue Klassenstufe", "Zurücksetzen", "Migrieren", "Löschen", "Abbrechen");
            case ENGLISH -> setupGUI("New grade", "Reset", "Migrate", "Delete", "Cancel");
        }
    }

    private void setupGUI(String grade, String reset, String migrate, String delete, String cancel) {
        this.gradeLabel.setText(grade);
        this.resetButton.setText(reset);
        this.migrateButton.setText(migrate);
        this.deleteButton.setText(delete);
        this.cancelButton.setText(cancel);
    }

    public String getGrade() {
        return gradeTextField.getText();
    }

    public void migrateFunction() {
        int grade = 0;
        try {
            grade = Integer.parseInt(getGrade());
        } catch (Exception e) {
            System.out.println("Error, Please enter a number!");
        }
        try {
            guiManager.getClient().mySQL.update("UPDATE courses SET grade = " + grade + " WHERE courseid = " + course.getId() + ";");
        } catch (Exception e) {
            System.out.println("Error while updating database");
        }
        try {
            guiManager.getClient().courseManager.cacheAllCourseData();
        } catch (Exception e) {
            System.out.println("Error while updating local Hashmap");
        }
        ArrayList<User> list = new ArrayList<>(guiManager.getClient().userManager.getUserHashMap().values());
        guiManager.classroomGUI.updateGUI(list);
    }

    public void resetFunction() {
        for (User students : guiManager.getClient().userManager.getUserHashMap().values()) {
            if (students.getCourse().contains(course.getId())) {
                try {
                    StringBuilder courses = new StringBuilder();
                    ArrayList<Integer> courseids = students.getCourse();
                    courseids.remove(students.getCourse().indexOf(course.getId()));
                    if (!courseids.isEmpty()) {
                        courses.append("\"");
                        courses.append(courseids.get(0));
                        if (courseids.size() > 1) {
                            for (int course : courseids.subList(1, courseids.size() - 1)) {
                                courses.append("\\;").append(course);
                            }
                        }
                        courses.append("\"");
                    } else {
                        courses.append("NULL");
                    }
                    guiManager.getClient().mySQL.update("UPDATE users SET courseids = " + courses + " WHERE userid=" + students.getId() + ";");
                } catch (Exception e) {
                    System.out.println("Error while updating database");
                }
            }
        }
        try {
            guiManager.getClient().userManager.cacheAllUserData();
            guiManager.getClient().courseManager.cacheAllCourseData();
        } catch (Exception e) {
            System.out.println("Error while caching new data");
        }
        ArrayList<User> list = new ArrayList<>(guiManager.getClient().userManager.getUserHashMap().values());
        guiManager.classroomGUI.updateGUI(list);
    }

    public void deleteFunction() {
        try {
            resetFunction();
            guiManager.getClient().mySQL.update("DELETE FROM courses WHERE courseid = " + course.getId() + ";");
        } catch (Exception e) {
            System.out.println("Error while updating database");
        }
        try {
            guiManager.getClient().userManager.cacheAllUserData();
            guiManager.getClient().courseManager.cacheAllCourseData();
        } catch (Exception e) {
            System.out.println("Error while caching new data");
        }
        ArrayList<User> list = new ArrayList<>(guiManager.getClient().userManager.getUserHashMap().values());
        guiManager.classroomGUI.updateGUI(list);
    }
}
