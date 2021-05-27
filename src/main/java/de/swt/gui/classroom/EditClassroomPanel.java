package de.swt.gui.classroom;

import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.course.Course;
import de.swt.logic.user.User;
import lombok.Setter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Setter
public class EditClassroomPanel extends GUI {
    private JPanel mainPanel;
    public JButton doneButton;
    public JButton cancelButton;
    private JTextField studentTextField;
    private JComboBox<String> studentComboBox;
    private JLabel studentComboBoxLabel;
    private JLabel studentTextFieldLabel;
    private Course course;

    public EditClassroomPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        switch (guiManager.language) {
            case GERMAN -> this.setupGUI("<html>Schüler <br> hinzufügen</html>", "<html>Schüler <br> entfernen</html>", "Übernehmen", "Abbrechen");
            case ENGLISH -> this.setupGUI("Add student", "Remove student", "Commit", "Cancel");
        }
    }

    private void setupGUI(String student, String students, String done, String cancel) {
        this.studentTextFieldLabel.setText(student);
        this.studentComboBoxLabel.setText(students);
        this.doneButton.setText(done);
        this.cancelButton.setText(cancel);
    }

    public void updateGUI(ArrayList<User> students) {
        this.studentComboBox.removeAllItems();
        this.studentComboBox.addItem("");
        for (User student : students) {
            if (student.getCourse().contains(course.getId())) {
                this.studentComboBox.addItem(student.getId() + "");
            }
        }
    }

    public String getStudentToAdd() {
        return studentTextField.getText();
    }

    public String getStudentToRemove() {
        return (String) studentComboBox.getSelectedItem();
    }

    // TODO: Other Group adds this Function
    public void doneFunction() {
        if (!getStudentToAdd().equals("")) {
            int addStudentId = 0;
            try {
                addStudentId = Integer.parseInt(getStudentToAdd());
            } catch (Exception e) {
                System.out.println("Error while parsing Student, Please enter the User Id!");
            }
            if (guiManager.getClient().userManager.getUserHashMap().containsKey(addStudentId)) {
                if (guiManager.getClient().userManager.getUserHashMap().get(addStudentId).getCourse().contains(course.getId())) {
                    System.out.println("Error: Selected User to add already is in course!");
                } else {
                    try {
                        User user = guiManager.getClient().userManager.getUserHashMap().get(addStudentId);
                        StringBuilder courseidsAddStudent = new StringBuilder();
                        courseidsAddStudent.append("\"");
                        courseidsAddStudent.append(course.getId());
                        for (int id : guiManager.getClient().userManager.getUserHashMap().get(addStudentId).getCourse()) {
                            courseidsAddStudent.append("\\;").append(id);
                        }
                        courseidsAddStudent.append("\"");
                        user.getCourse().add(course.getId());
                        guiManager.getClient().server.sendUser(user,-1,true);
                    } catch (Exception e) {
                        System.out.println("Error while updating database");
                    }
                }
            } else {
                System.out.println("User doesn't exist");
            }
        }
        if (!getStudentToRemove().equals("")) {
            int removeStudentId = 0;
            try {
                removeStudentId = Integer.parseInt(getStudentToRemove());
            } catch (Exception e) {
                System.out.println("Error while parsing Student, Please enter the User Id!");
            }
            if (!guiManager.getClient().userManager.getUserHashMap().get(removeStudentId).getCourse().contains(course.getId())) {
                System.out.println("Error: Selected User to remove isn't part of course!");
            } else {
                try {
                    StringBuilder courseidsRemoveStudent = new StringBuilder();
                    ArrayList<Integer> courseids = guiManager.getClient().userManager.getUserHashMap().get(removeStudentId).getCourse();
                    courseids.remove(courseids.indexOf(course.getId()));
                    if (!courseids.isEmpty()) {
                        courseidsRemoveStudent.append("\"");
                        courseidsRemoveStudent.append(courseids.get(0));
                        if (courseids.size() > 1) {
                            for (int course : courseids.subList(1, courseids.size() - 1)) {
                                courseidsRemoveStudent.append("\\;").append(course);
                            }
                        }
                        courseidsRemoveStudent.append("\"");
                    } else {
                        courseidsRemoveStudent.append("NULL");
                    }
                    guiManager.getClient().mySQL.update("UPDATE users SET courseids=" + courseidsRemoveStudent + " WHERE userid=" + removeStudentId + ";");
                } catch (Exception e) {
                    System.out.println("Error while updating database");
                    e.printStackTrace();
                }
            }
        }
        try {
            guiManager.getClient().userManager.cacheAllUserData();
        } catch (Exception e) {
            System.out.println("Error while caching new data");
        }
        ArrayList<User> list = new ArrayList<>(guiManager.getClient().userManager.getUserHashMap().values());
        guiManager.classroomGUI.updateGUI(list);
    }
}
