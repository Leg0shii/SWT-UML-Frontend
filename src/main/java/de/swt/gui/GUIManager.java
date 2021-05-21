package de.swt.gui;

import de.swt.gui.classroom.ClassroomGUI;
import de.swt.gui.login.LoginGUI;
import de.swt.gui.workspace.WorkspaceGUI;
import de.swt.logic.Group;
import de.swt.util.AccountType;
import de.swt.util.Client;
import de.swt.util.Language;
import de.swt.logic.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUIManager extends JFrame {
    public Language language;
    public AccountType accountType;

    public LoginGUI loginGUI;
    public ClassroomGUI classroomGUI;
    public WorkspaceGUI workspaceGUI;

    private final List<GUI> childrenGUI;
    private Client client;

    public GUIManager(Client client,  Language language, AccountType accountType) {
        super("E-Learning Software");
        this.client = client;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 450);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setVisible(true);
        this.language = language;
        this.accountType = accountType;
        this.childrenGUI = new ArrayList<>();
    }

    public void setupGUIS() {
        this.loginGUI = new LoginGUI(this);
        this.childrenGUI.add(loginGUI);
        this.classroomGUI = new ClassroomGUI(this);
        this.childrenGUI.add(classroomGUI);
        this.workspaceGUI = new WorkspaceGUI(this);
        this.childrenGUI.add(workspaceGUI);
    }

    public void updateGUIS(String[] schools, ArrayList<User> students,  List<Group> groups, List<User> users, int remainingTime) {
        this.classroomGUI.updateGUI(students);
        this.loginGUI.updateGUI(schools);
        this.workspaceGUI.updateGUI(groups, users, remainingTime);
    }

    public void switchToLoginGUI() {
        closeAllPopups();
        this.setContentPane(loginGUI);
        this.setVisible(true);
    }

    public void switchToClassRoomGUI() {
        closeAllPopups();
        this.setContentPane(classroomGUI);
        this.setVisible(true);
    }

    public void switchToWorkspaceGUI() {
        closeAllPopups();
        this.setContentPane(workspaceGUI);
        this.setVisible(true);
    }

    public void closeAllPopups(){
        for (GUI childGUI : childrenGUI){
            childGUI.closeAllPopups();
        }
    }

    public void addToDrawPanel(JComponent component){
        workspaceGUI.addToDrawPanel(component);
    }

    public Language getLanguage() {
        return language;
    }

    public Client getClient() {
        return this.client;
    }

    public void removeLastDrawnObject() {
        workspaceGUI.removeLastDrawnObject();
    }

    public Component[] getDrawnObjects(){
        return workspaceGUI.getDrawnObjects();
    }

    public void addDrawnObjects(Component[] components){
        for (Component component : components){
            addToDrawPanel((JComponent) component);
        }
    }
}
