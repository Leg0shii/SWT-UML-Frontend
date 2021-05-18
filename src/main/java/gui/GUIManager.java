package gui;

import gui.classroom.ClassroomGUI;
import gui.login.LoginGUI;
import gui.workspace.WorkspaceGUI;
import logic.group.Group;
import util.AccountType;
import util.Language;
import logic.user.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUIManager extends JFrame {
    public Color[] colorScheme;
    public Language language;

    public LoginGUI loginGUI;
    public ClassroomGUI classroomGUI;
    public WorkspaceGUI workspaceGUI;

    public GUIManager(Color[] colorScheme, Language language) {
        super("E-Learning Software");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 450);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.colorScheme = colorScheme;
        this.setVisible(true);
        this.language = language;
    }

    public void setupGUIS() {
        this.loginGUI = new LoginGUI(this);
        this.classroomGUI = new ClassroomGUI(this);
        this.workspaceGUI = new WorkspaceGUI(this);
    }

    public void updateGUIS(String[] schools, ArrayList<User> students, AccountType accountType, List<Group> groups, List<User> users, int remainingTime) {
        this.classroomGUI.updateGUI(students, accountType);
        this.loginGUI.updateGUI(schools);
        this.workspaceGUI.updateGUI(groups, users, accountType, remainingTime);
    }

    public void switchToLoginGUI() {
        this.setContentPane(loginGUI);
        this.setVisible(true);
    }

    public void switchToClassRoomGUI() {
        this.setContentPane(classroomGUI);
        this.setVisible(true);
    }

    public void switchToWorkspaceGUI() {
        this.setContentPane(workspaceGUI);
        this.setVisible(true);
    }

    public void insertNewPanel() {

    }

    public Color[] getColorScheme() {
        return colorScheme;
    }

    public Language getLanguage() {
        return language;
    }
}
