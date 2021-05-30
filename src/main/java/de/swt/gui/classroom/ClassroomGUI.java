package de.swt.gui.classroom;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.swt.gui.GUIManager;
import de.swt.gui.GUI;
import de.swt.logic.course.Course;
import de.swt.logic.user.User;
import de.swt.util.AccountType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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

    public void updateGUI() {
        removeAllGradePanels();
        updateGradePanels();
        for (GradePanel gradePanel : gradePanels) {
            gradePanel.getEditClassroomPanel().updateGUI();
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

    public void updateGradePanels() {
        gradePanels.clear();
        for (Course course : guiManager.getClient().courseManager.getCourseHashMap().values()) {
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

    // TODO: Implemented by other Group
    private void privateWorkspaceFunction() {
        guiManager.switchToWorkspaceGUI();
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
        mainPanel.setLayout(new GridLayoutManager(4, 3, new Insets(5, 5, 5, 5), -1, -1));
        grade10Label = new JLabel();
        grade10Label.setHorizontalAlignment(0);
        grade10Label.setText("Label");
        mainPanel.add(grade10Label, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        grade11Label = new JLabel();
        grade11Label.setHorizontalAlignment(0);
        grade11Label.setText("Label");
        mainPanel.add(grade11Label, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        grade12Label = new JLabel();
        grade12Label.setHorizontalAlignment(0);
        grade12Label.setText("Label");
        mainPanel.add(grade12Label, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPanel10 = new JScrollPane();
        mainPanel.add(scrollPanel10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPanel10.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        gradePanel10 = new JPanel();
        gradePanel10.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        scrollPanel10.setViewportView(gradePanel10);
        gradePanel10.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        scrollPanel11 = new JScrollPane();
        mainPanel.add(scrollPanel11, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPanel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        gradePanel11 = new JPanel();
        gradePanel11.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        scrollPanel11.setViewportView(gradePanel11);
        gradePanel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        scrollPanel12 = new JScrollPane();
        mainPanel.add(scrollPanel12, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPanel12.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        gradePanel12 = new JPanel();
        gradePanel12.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        scrollPanel12.setViewportView(gradePanel12);
        gradePanel12.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        subPanel = new JPanel();
        subPanel.setLayout(new GridLayoutManager(2, 3, new Insets(5, 5, 5, 5), -1, -1));
        mainPanel.add(subPanel, new GridConstraints(2, 0, 2, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        subPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        privateWorkspaceButton = new JButton();
        privateWorkspaceButton.setText("Button");
        subPanel.add(privateWorkspaceButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logoutButton = new JButton();
        logoutButton.setText("Button");
        subPanel.add(logoutButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        createClassroomButton = new JButton();
        createClassroomButton.setText("Button");
        subPanel.add(createClassroomButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        supportButton = new JButton();
        supportButton.setText("Button");
        subPanel.add(supportButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
