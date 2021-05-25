package de.swt.gui;

import de.swt.gui.classroom.ClassroomGUI;
import de.swt.gui.login.LoginGUI;
import de.swt.gui.workspace.WorkspaceGUI;
import de.swt.logic.group.Group;
import de.swt.logic.user.User;
import de.swt.util.AccountType;
import de.swt.util.Client;
import de.swt.util.Language;
import org.apache.commons.lang3.SerializationUtils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GUIManager extends JFrame {
    public Language language;
    public AccountType accountType;

    public LoginGUI loginGUI;
    public ClassroomGUI classroomGUI;
    public WorkspaceGUI workspaceGUI;

    private final List<GUI> childrenGUI;
    private final Client client;

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

    public void updateGUIS(String[] schools, ArrayList<User> students, List<Group> groups, List<User> users, int remainingTime) {
        this.classroomGUI.updateGUI(students);
        this.loginGUI.updateGUI(schools);
        this.workspaceGUI.updateGUI(groups, users, remainingTime);
    }

    public void switchToLoginGUI() {
        try {
            this.getClient().userManager.userLogout();
        } catch (Exception ignored){

        }
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

    public boolean removeLastDrawnObject() {
        return workspaceGUI.removeLastDrawnObject();
    }

    public Component[] getDrawnObjects(){
        Component[] components = workspaceGUI.getDrawnObjects();
        return SerializationUtils.clone(components);
    }

    public void addDrawnObjects(Component[] components){
        while (removeLastDrawnObject()){

        }
        for (Component component : components){
            addToDrawPanel((JComponent) component);
        }
    }

    public void saveWorkspace(File file){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this.getDrawnObjects());
            objectOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadWorkspace(File file){
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Component[] list = (Component[]) objectInputStream.readObject();
            objectInputStream.close();
            this.addDrawnObjects(list);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
