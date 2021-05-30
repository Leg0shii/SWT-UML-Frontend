package de.swt.gui.classroom;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.course.Course;
import de.swt.logic.user.User;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(3, 8, new Insets(5, 5, 5, 5), -1, -1));
        gradeLabel = new JLabel();
        gradeLabel.setText("Label");
        mainPanel.add(gradeLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gradeTextField = new JTextField();
        mainPanel.add(gradeTextField, new GridConstraints(0, 1, 1, 7, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        migrateButton = new JButton();
        migrateButton.setText("Button");
        mainPanel.add(migrateButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resetButton = new JButton();
        resetButton.setHorizontalAlignment(0);
        resetButton.setHorizontalTextPosition(11);
        resetButton.setText("Button");
        mainPanel.add(resetButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteButton = new JButton();
        deleteButton.setText("Button");
        mainPanel.add(deleteButton, new GridConstraints(1, 1, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Button");
        mainPanel.add(cancelButton, new GridConstraints(2, 1, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gradeLabel.setLabelFor(gradeTextField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
