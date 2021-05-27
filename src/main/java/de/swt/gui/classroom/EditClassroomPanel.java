package de.swt.gui.classroom;

import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.course.Course;
import de.swt.logic.user.User;
import de.swt.logic.user.UserManager;
import lombok.Setter;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

@Setter
public class EditClassroomPanel extends GUI {
    public JButton doneButton;
    public JButton cancelButton;
    private JPanel mainPanel;
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

    public void doneFunction() {

        UserManager userManager = guiManager.getClient().userManager;
        User user = null;

        if (!getStudentToAdd().equals("")) {
            user = userManager.getUserHashMap().get(Integer.parseInt(getStudentToAdd()));
            userManager.setSingleCourse(user, course.getId());
        }
        if (!getStudentToRemove().equals("")) {
            user = userManager.getUserHashMap().get(Integer.parseInt(getStudentToRemove()));
            userManager.removeSingleCourse(user, course.getId());
        }

        try { guiManager.getClient().server.sendUser(user, -1, true); }
        catch (RemoteException e) { e.printStackTrace(); }

        ArrayList<User> list = new ArrayList<>(guiManager.getClient().userManager.getUserHashMap().values());
        guiManager.classroomGUI.updateGUI(list);
    }
}
