package de.swt.gui;

import de.swt.gui.classroom.ClassroomGUI;
import de.swt.gui.login.LoginGUI;
import de.swt.gui.workspace.WorkspaceGUI;
import de.swt.logic.group.Group;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.util.AccountType;
import de.swt.util.Client;
import de.swt.util.Language;
import de.swt.util.WorkspaceState;
import org.apache.commons.lang3.SerializationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GUIManager extends JFrame {
    public Language language;
    public AccountType accountType;

    public LoginGUI loginGUI;
    public ClassroomGUI classroomGUI;
    public WorkspaceGUI workspaceGUI;

    private final List<GUI> childrenGUI;
    private Client client;

    private int drawableObjectCounter;

    public WorkspaceState state;

    public Session currentSession;

    public GUIManager(Language language, AccountType accountType) {
        super("E-Learning Software");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 450);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setVisible(true);
        this.language = language;
        this.accountType = accountType;
        this.childrenGUI = new ArrayList<>();
        this.drawableObjectCounter = 0;

        this.state = WorkspaceState.EDITING;
    }

    public void updateGUIManager(Client client){
        this.client = client;
        this.currentSession = new Session(client.userid);
    }

    public void setupGUIS() {
        this.loginGUI = new LoginGUI(this);
        this.childrenGUI.add(loginGUI);
        this.classroomGUI = new ClassroomGUI(this);
        this.childrenGUI.add(classroomGUI);
        this.workspaceGUI = new WorkspaceGUI(this);
        this.childrenGUI.add(workspaceGUI);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    client.userManager.userLogout();
                    client.onDisable();
                } catch (Exception ignored){

                }
            }
        });
    }

    public void updateGUIS(ArrayList<User> students) {
        this.classroomGUI.updateGUI(students);
        this.loginGUI.updateGUI();
        this.workspaceGUI.updateGUI();
    }

    public void updateGUIS(){
        this.classroomGUI.updateGUI();
        this.loginGUI.updateGUI();
        this.workspaceGUI.updateGUI();
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

    public boolean removeLastAnnotations(){
        return workspaceGUI.removeLastAnnotations();
    }

    public Component[] getDrawnObjects(){
        Component[] components = workspaceGUI.getDrawnObjects();
        return SerializationUtils.clone(components);
    }

    public Component[] getAnnotations(){
        Component[] components = workspaceGUI.getAnnotations();
        return SerializationUtils.clone(components);
    }

    public void addDrawnObjects(Component[] components){
        while(removeLastDrawnObject()){}
        for (Component component : components){
            addToDrawPanel((JComponent) component);
        }
    }

    public void addAnnotations(Component[] components){
        while(removeLastAnnotations()){}
        for (Component component : components){
            addToDrawPanel((JComponent) component);
        }
    }

    public void saveWorkspace(File file){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            var allComponents = new Component[2][2];
            allComponents[0] = this.getDrawnObjects();
            allComponents[1] = this.getAnnotations();
            objectOutputStream.writeObject(allComponents);
            objectOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadWorkspace(File file){
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Component[][] list = (Component[][]) objectInputStream.readObject();
            Component[] objects = list[0];
            Component[] annotations = list[1];
            objectInputStream.close();
            while (removeLastDrawnObject()){}
            this.addDrawnObjects(objects);
            while (removeLastAnnotations()){}
            this.addAnnotations(annotations);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void syncWorkspace(File file){
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Component[][] list = (Component[][]) objectInputStream.readObject();
            Component[] objects = list[0];
            Component[] annotations = list[1];
            objectInputStream.close();
            removeAllIndexedObjects(objects);
            removeAllIndexedObjects(annotations);
            addDrawnObjects(objects);
            addAnnotations(annotations);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void removeAllIndexedObjects(Component[] objects) {
        workspaceGUI.removeAllIndexedObjects(objects);
    }

    public int increaseObjectCounter(){
        return this.drawableObjectCounter++;
    }

    public void setWorkspaceState(WorkspaceState state){
        this.state = state;
        this.workspaceGUI.updateGUI();
        this.revalidate();
    }
}
