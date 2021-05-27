package de.swt.gui.classroom;

import de.swt.gui.GUIManager;
import de.swt.gui.GUI;
import de.swt.logic.course.Course;
import de.swt.logic.user.User;
import de.swt.util.AccountType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClassroomGUI extends GUI {
    private JPanel mainPanel;
    private JLabel grade11Label;
    private JLabel grade10Label;
    private JLabel grade12Label;
    public JButton logoutButton;
    public JButton privateWorkspaceButton;
    private JButton supportButton;
    public JButton createClassroomButton;
    private JPanel gradePanel10;
    private JPanel gradePanel11;
    private JPanel gradePanel12;
    private JScrollPane scrollPanel10;
    private JScrollPane scrollPanel11;
    private JScrollPane scrollPanel12;
    private JPanel subPanel;
    private final List<GradePanel> gradePanels;
    private final CreateClassroomPanel createClassroomPanel;

    public ClassroomGUI(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        switch (guiManager.language) {
            case GERMAN -> setupGUI("Klasse 11", "Klasse 10", "Klasse 12", "Abmelden", "Klassenraum erstellen", "Privater Arbeitsplatz");
            case ENGLISH -> setupGUI("Grade 11", "Grade 10", "Grade 12", "Logout", "Create Classroom", "Private Workspace");
        }

        setupActionListeners();

        this.gradePanel10.setLayout(new BoxLayout(gradePanel10, BoxLayout.Y_AXIS));
        this.scrollPanel10.setViewportView(gradePanel10);
        this.gradePanel11.setLayout(new BoxLayout(gradePanel11, BoxLayout.Y_AXIS));
        this.scrollPanel11.setViewportView(gradePanel11);
        this.gradePanel12.setLayout(new BoxLayout(gradePanel12, BoxLayout.Y_AXIS));
        this.scrollPanel12.setViewportView(gradePanel12);

        this.gradePanels = new ArrayList<>();

        this.createClassroomPanel = new CreateClassroomPanel(guiManager);
    }

    private void setupGUI(String grade11, String grade10, String grade12, String logout, String createCR, String privateWS) {
        this.grade11Label.setText(grade11);
        this.grade10Label.setText(grade10);
        this.grade12Label.setText(grade12);
        this.logoutButton.setText(logout);
        this.supportButton.setText("Support");
        this.createClassroomButton.setText(createCR);
        this.privateWorkspaceButton.setText(privateWS);
        initPopups(2);
    }

    public void updateGUI(ArrayList<User> students) {
        removeAllGradePanels();
        updateGradePanels();
        for (GradePanel gradePanel : gradePanels) {
            gradePanel.getEditClassroomPanel().updateGUI(students);
            switch (gradePanel.grade) {
                case 10 -> gradePanel10.add(gradePanel);
                case 11 -> gradePanel11.add(gradePanel);
                case 12 -> gradePanel12.add(gradePanel);
            }
        }
        this.initForAccountType();
        this.revalidate();
    }

    private void setupActionListeners() {
        PopupFactory popupFactory = new PopupFactory();
        this.supportButton.addActionListener(e -> {
            incrementPopupCounter(0);
            if (popupCounter.get(0) % 2 == 0) {
                popups.get(0).hide();
            } else {
                JPanel supportPanel = new JPanel();
                supportPanel.setBorder(BorderFactory.createEtchedBorder());
                JLabel supportNumber = new JLabel("No Support!");
                supportPanel.add(supportNumber);
                Point point = new Point(this.supportButton.getX(), this.supportButton.getY() - 30);
                SwingUtilities.convertPointToScreen(point, subPanel);
                popups.set(0, popupFactory.getPopup(this, supportPanel, point.x, point.y));
                popups.get(0).show();
            }
        });
        this.createClassroomButton.addActionListener(e1 -> {
            incrementPopupCounter(1);
            if (popupCounter.get(1) % 2 == 0) {
                popups.get(1).hide();
            } else {
                createClassroomPanel.cancelButton.addActionListener(e11 -> {
                    incrementPopupCounter(1);
                    popups.get(1).hide();
                });
                createClassroomPanel.doneButton.addActionListener(e12 -> {
                    incrementPopupCounter(1);
                    createClassroomPanel.doneFunction();
                    popups.get(1).hide();
                });
                createClassroomPanel.addButton.addActionListener(e13 -> createClassroomPanel.addFunction());
                Point point = new Point(this.createClassroomButton.getX(), this.createClassroomButton.getY() - 200);
                SwingUtilities.convertPointToScreen(point, subPanel);
                popups.set(1, popupFactory.getPopup(this, createClassroomPanel, point.x, point.y));
                popups.get(1).show();
            }
        });
        this.logoutButton.addActionListener(e2 -> logout());
        this.privateWorkspaceButton.addActionListener(e3 -> privateWorkspaceFunction());
    }

    private void logout() {
        // logs out user
        guiManager.getClient().userManager.userLogout();
        guiManager.switchToLoginGUI();
    }

    public void addGradePanel(GradePanel gradePanel) {
        this.gradePanels.add(gradePanel);
    }

    public GradePanel getGradePanel(int id) {
        for (GradePanel gradePanel : gradePanels) {
            if (gradePanel.getCourse().getId() == id) {
                return gradePanel;
            }
        }
        return null;
    }

    public void updateGradePanels(){
        gradePanels.clear();
        for (Course course: guiManager.getClient().courseManager.getCourseHashMap().values()){
            GradePanel gradePanel = new GradePanel(guiManager);
            gradePanel.updateGUI(course);
            gradePanels.add(gradePanel);
        }
    }

    private void removeAllGradePanels() {
        for (Component component : gradePanel10.getComponents()) {
            if (component instanceof GradePanel) {
                gradePanel10.remove(component);
            }
        }
        for (Component component : gradePanel11.getComponents()) {
            if (component instanceof GradePanel) {
                gradePanel11.remove(component);
            }
        }
        for (Component component : gradePanel12.getComponents()) {
            if (component instanceof GradePanel) {
                gradePanel12.remove(component);
            }
        }
    }

    public void initForAccountType() {
        if (guiManager.accountType == AccountType.STUDENT) {
            this.mainPanel.remove(createClassroomButton);
            removeEditButtonsFromGradePanels();
        }
    }

    private void removeEditButtonsFromGradePanels() {
        for (Component component : gradePanel10.getComponents()) {
            ((GradePanel) component).getMainPanel().remove(((GradePanel) component).editButton);
        }
        for (Component component : gradePanel11.getComponents()) {
            ((GradePanel) component).getMainPanel().remove(((GradePanel) component).editButton);
        }
        for (Component component : gradePanel12.getComponents()) {
            ((GradePanel) component).getMainPanel().remove(((GradePanel) component).editButton);
        }
    }

    public String getCreateClassroomGradeName() {
        return createClassroomPanel.getGradeName();
    }

    public String getCreateClassroomTeacher() {
        return createClassroomPanel.getTeacher();
    }

    public String getCreateClassroomDate() {
        return createClassroomPanel.getDate();
    }

    public String getCreateClassroomStudent() {
        return createClassroomPanel.getStudent();
    }

    // TODO: Implemented by other Group
    private void privateWorkspaceFunction() {
        guiManager.switchToWorkspaceGUI();
    }
}
