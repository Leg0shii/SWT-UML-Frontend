package de.swt.gui;

import de.swt.drawing.objects.DrawableObject;
import de.swt.gui.classroom.ClassroomGUI;
import de.swt.gui.login.LoginGUI;
import de.swt.gui.workspace.WorkspaceGUI;
import de.swt.logic.group.Group;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.logic.user.UserManager;
import de.swt.util.AccountType;
import de.swt.util.Client;
import de.swt.util.Language;
import de.swt.util.WorkspaceState;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

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
    public Group currentGroup;

    public GUIManager(Language language) {
        super("E-Learning Software");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 450);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setVisible(true);
        this.language = language;
        this.childrenGUI = new ArrayList<>();
        this.drawableObjectCounter = 0;

        this.state = WorkspaceState.EDITING;
    }

    public void updateGUIManager(Client client) {
        this.client = client;
    }

    public void setupGUIS() {
        this.loginGUI = new LoginGUI(this);
        this.childrenGUI.add(loginGUI);
        loginGUI.loginButton.setEnabled(false);
        loginGUI.loginButton.setBackground(loginGUI.loginButton.getBackground().darker());
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    client.server.deleteSession(currentSession.getId());
                    currentSession = null;
                    client.userManager.userLogout();
                    client.onDisable();
                } catch (Exception ignored) {

                }
            }
        });
    }

    public void secondSetup() {
        this.accountType = client.userManager.getUserHashMap().get(client.userid).getAccountType();
        this.classroomGUI = new ClassroomGUI(this);
        this.childrenGUI.add(classroomGUI);
        this.workspaceGUI = new WorkspaceGUI(this);
        this.childrenGUI.add(workspaceGUI);
        timeUpdater();
        updateGUIS(new ArrayList<>(getClient().userManager.getUserHashMap().values()));
    }

    public void updateGUIS(ArrayList<User> students) {
        this.classroomGUI.updateGUI(students);
        this.loginGUI.updateGUI();
        this.workspaceGUI.updateGUI();
    }

    public void updateGUIS() {
        try {
            this.classroomGUI.updateGUI();
            this.loginGUI.updateGUI();
            this.workspaceGUI.updateGUI();
        } catch (Exception ignored) {
        }
    }

    public void switchToLoginGUI() {
        try {
            this.getClient().userManager.userLogout();
            if (!accountType.equals(AccountType.TEACHER)) {
                this.currentSession.getParticipants().remove(client.userid);
                this.client.server.sendSession(currentSession, currentSession.getId(), true);
                this.currentSession = null;
            } else {
                this.client.server.deleteSession(currentSession.getId());
                this.currentSession = null;
            }
        } catch (Exception ignored) {

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

    public void closeAllPopups() {
        for (GUI childGUI : childrenGUI) {
            childGUI.closeAllPopups();
        }
    }

    public void addToDrawPanel(JComponent component) {
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

    public boolean removeLastAnnotations() {
        return workspaceGUI.removeLastAnnotations();
    }

    public Component[] getDrawnObjects() {
        Component[] components = workspaceGUI.getDrawnObjects();
        return SerializationUtils.clone(components);
    }

    public Component[] getAnnotations() {
        Component[] components = workspaceGUI.getAnnotations();
        return SerializationUtils.clone(components);
    }

    public void addDrawnObjects(Component[] components) {
        while (removeLastDrawnObject()) {
        }
        for (Component component : components) {
            addToDrawPanel((JComponent) component);
        }
    }

    public void addObjectsWithoutRemoval(Component[] components) {
        for (Component component : components) {
            addToDrawPanel((JComponent) component);
        }
    }

    public void addAnnotations(Component[] components) {
        while (removeLastAnnotations()) {
        }
        for (Component component : components) {
            addToDrawPanel((JComponent) component);
        }
    }

    public void saveWorkspace(File file) {
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

    public void loadWorkspace(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Component[][] list = (Component[][]) objectInputStream.readObject();
            Component[] objects = list[0];
            Component[] annotations = list[1];
            objectInputStream.close();
            while (removeLastDrawnObject()) {
            }
            this.addDrawnObjects(objects);
            while (removeLastAnnotations()) {
            }
            this.addAnnotations(annotations);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void syncWorkspace(byte[] input) {
        try {
            File file = new File("input.ser");
            FileUtils.writeByteArrayToFile(file, input);
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Component[][] list = (Component[][]) objectInputStream.readObject();
            Component[] objects = list[0];
            Component[] annotations = list[1];
            objectInputStream.close();
            removeAllIndexedObjects(objects);
            removeAllIndexedObjects(annotations);
            addObjectsWithoutRemoval(objects);
            addObjectsWithoutRemoval(annotations);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void syncSingleObject(Component component) {
        try {
            File file = new File("output.ser");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            var allComponents = new Component[2][2];
            if (!component.getClass().getSimpleName().matches("Thumb.*")) {
                allComponents[0] = new Component[]{component};
            } else {
                allComponents[1] = new Component[]{component};
            }
            objectOutputStream.writeObject(allComponents);
            objectOutputStream.close();
            byte[] outByteArray = FileUtils.readFileToByteArray(file);
            client.server.updateWorkspaceFile(outByteArray, client.userid);
        } catch (Exception ignored) {

        }
    }

    private void removeAllIndexedObjects(Component[] objects) {
        workspaceGUI.removeAllIndexedObjects(objects);
    }

    public int increaseObjectCounter() {
        return this.drawableObjectCounter++;
    }

    public void setWorkspaceState(WorkspaceState state) {
        this.state = state;
        this.workspaceGUI.updateGUI();
        this.revalidate();
    }

    public ArrayList<Group> getRelevantGroups() {
        ArrayList<Group> groups = new ArrayList<>();
        for (Group group : client.groupManager.getGroupHashMap().values()) {
            if (group == null) {
                continue;
            }
            if (group.getCourseID() == currentSession.getId()) {
                groups.add(group);
            }
        }
        return groups;
    }

    public void timeUpdater() {

        new Thread(() -> {
            TimerTask task = new TimerTask() {

                @Override
                public void run() {

                    long time;
                    if (currentSession != null) {
                        time = currentSession.getRemainingTime();
                        if (currentGroup != null) {
                            time = currentGroup.getTimeTillTermination();
                        }
                        int minutes = (int) ((time - System.currentTimeMillis()) / 60000) + 1;
                        workspaceGUI.setTimeTilLTermination(minutes);
                    }

                }

            };

            Timer timer = new Timer();
            timer.schedule(task, 0, 1000);
        }).start();
    }
}
