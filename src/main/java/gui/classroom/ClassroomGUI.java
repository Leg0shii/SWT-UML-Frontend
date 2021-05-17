package gui.classroom;

import gui.GUIManager;
import gui.GUI;
import util.AccountType;
import logic.user.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
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
    public Popup[] popups;
    private int[] popupCounter;
    public GUIManager guiManager;
    private final List<GradePanel> gradePanels;
    private final CreateClassroomPanel createClassroomPanel;

    public ClassroomGUI(GUIManager guiManager) {
        this.guiManager = guiManager;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        switch (guiManager.language){
            case GERMAN -> setupGUI("Klasse 11", "Klasse 10", "Klasse 12", "Abmelden", "Klassenraum erstellen", "Privater Arbeitsplatz");
            case ENGLISH -> setupGUI("Grade 11", "Grade 10", "Grade 12", "Logout", "Create Classroom", "Private Workspace");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), guiManager.colorScheme, 0);
        setupActionListeners();

        this.gradePanel10.setLayout(new BoxLayout(gradePanel10, BoxLayout.Y_AXIS));
        this.scrollPanel10.setViewportView(gradePanel10);
        this.gradePanel11.setLayout(new BoxLayout(gradePanel11, BoxLayout.Y_AXIS));
        this.scrollPanel11.setViewportView(gradePanel11);
        this.gradePanel12.setLayout(new BoxLayout(gradePanel12, BoxLayout.Y_AXIS));
        this.scrollPanel12.setViewportView(gradePanel12);

        this.gradePanels = new ArrayList<>();

        this.createClassroomPanel = new CreateClassroomPanel(guiManager.colorScheme, guiManager.language);
    }

    private void setupGUI(String grade11, String grade10, String grade12, String logout, String createCR, String privateWS) {
        this.grade11Label.setText(grade11);
        this.grade10Label.setText(grade10);
        this.grade12Label.setText(grade12);
        this.logoutButton.setText(logout);
        this.supportButton.setText("Support");
        this.createClassroomButton.setText(createCR);
        this.privateWorkspaceButton.setText(privateWS);
    }

    public void updateGUI(ArrayList<User> students, AccountType accountType) {
        removeAllGradePanels();
        for (GradePanel gradePanel : gradePanels) {
            gradePanel.getEditClassroomPanel().updateGUI(students);
            switch (gradePanel.grade){
                case 10 -> gradePanel10.add(gradePanel);
                case 11 -> gradePanel11.add(gradePanel);
                case 12 -> gradePanel12.add(gradePanel);
            }
        }
        this.initForAccountType(accountType);
        this.revalidate();
    }

    private void setupActionListeners() {
        PopupFactory popupFactory = new PopupFactory();
        this.popupCounter = new int[2];
        this.popups = new Popup[2];
        this.supportButton.addActionListener(e -> {
            popupCounter[0]++;
            if (popupCounter[0] % 2 == 0) {
                popups[0].hide();
            } else {
                JPanel supportPanel = new JPanel();
                supportPanel.setBorder(BorderFactory.createEtchedBorder());
                JLabel supportNumber = new JLabel("No Support!");
                supportPanel.add(supportNumber);
                Point point = new Point(this.supportButton.getX(), this.supportButton.getY() - 30);
                SwingUtilities.convertPointToScreen(point, this);
                popups[0] = popupFactory.getPopup(this, supportPanel, point.x, point.y);
                popups[0].show();
            }
        });
        this.createClassroomButton.addActionListener(e1 -> {
            popupCounter[1]++;
            if (popupCounter[1] % 2 == 0) {
                popups[1].hide();
            } else {
                createClassroomPanel.cancelButton.addActionListener(e11 -> {
                    popupCounter[1]++;
                    popups[1].hide();
                });
                createClassroomPanel.doneButton.addActionListener(e12 -> {
                    popupCounter[1]++;
                    createClassroomPanel.doneFunction();
                    popups[1].hide();
                });
                createClassroomPanel.addButton.addActionListener(e13 -> createClassroomPanel.addFunction());
                Point point = new Point(this.createClassroomButton.getX(), this.createClassroomButton.getY() - 200);
                SwingUtilities.convertPointToScreen(point, this);
                popups[1] = popupFactory.getPopup(this, createClassroomPanel, point.x, point.y);
                popups[1].show();
            }
        });
        this.logoutButton.addActionListener(e2 -> logout());
        this.privateWorkspaceButton.addActionListener(e3 -> privateWorkspaceFunction());
    }

    private void logout(){
        closeAllPopups();
        guiManager.switchToLoginGUI();
    }

    public void addGradePanel(GradePanel gradePanel){
        this.gradePanels.add(gradePanel);
    }

    public GradePanel getGradePanel(int id){
        for (GradePanel gradePanel : gradePanels){
            if (gradePanel.getCourse().getID() == id){
                return gradePanel;
            }
        }
        return null;
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

    private void closeAllPopups(){
        for (Popup popup : popups){
            if (popup != null) {
                popup.hide();
            }
        }
        for (Component component : gradePanel10.getComponents()){
            if (component instanceof GradePanel) {
                ((GradePanel) component).closePopups();
            }
        }
        for (Component component : gradePanel11.getComponents()){
            if (component instanceof GradePanel) {
                ((GradePanel) component).closePopups();
            }
        }
        for (Component component : gradePanel12.getComponents()){
            if (component instanceof GradePanel) {
                ((GradePanel) component).closePopups();
            }
        }
        Arrays.fill(popupCounter, 0);
    }

    public void initForAccountType(AccountType accountType){
        if (accountType == AccountType.STUDENT) {
            this.mainPanel.remove(createClassroomButton);
            removeEditButtonsFromGradePanels();
        }
    }

    private void removeEditButtonsFromGradePanels(){
        for (Component component : gradePanel10.getComponents()){
            ((GradePanel) component).getMainPanel().remove(((GradePanel) component).editButton);
        }
        for (Component component : gradePanel11.getComponents()){
            ((GradePanel) component).getMainPanel().remove(((GradePanel) component).editButton);
        }
        for (Component component : gradePanel12.getComponents()){
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
    private void privateWorkspaceFunction(){

    }
}
