package de.swt.gui;

import de.swt.drawing.objects.DrawableObject;
import de.swt.gui.classroom.ClassroomGUI;
import de.swt.gui.login.LoginGUI;
import de.swt.gui.workspace.WorkspaceGUI;
import de.swt.logic.group.Group;
import de.swt.logic.user.User;
import de.swt.util.AccountType;
import de.swt.util.Client;
import de.swt.util.Language;
import de.swt.util.WorkspaceState;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Getter
@Setter
public class GUIManager extends JFrame {
    private Language language;
    private AccountType accountType;

    private LoginGUI loginGUI;
    private ClassroomGUI classroomGUI;
    private WorkspaceGUI workspaceGUI;

    private final List<GUI> childrenGUI;
    private Client client;

    private int drawableObjectCounter;

    private WorkspaceState state;

    public GUIManager(Language language) {
        super("E-Learning Software");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 450);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.language = language;
        this.childrenGUI = new ArrayList<>();
        this.drawableObjectCounter = 0;

        this.state = WorkspaceState.EDITING;
    }

    public void setupGUIS() {
        this.loginGUI = new LoginGUI(this);
        this.childrenGUI.add(loginGUI);
        loginGUI.loginButton.setEnabled(false);
        loginGUI.loginButton.setBackground(loginGUI.loginButton.getBackground().darker());
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    client.onDisable();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    public void secondSetup() {
        User user = client.getUserManager().getHashMap().get(client.getUserId());
        this.accountType = user.getAccountType();
        this.classroomGUI = new ClassroomGUI(this);
        this.childrenGUI.add(classroomGUI);
        this.workspaceGUI = new WorkspaceGUI(this);
        this.childrenGUI.add(workspaceGUI);
        timeUpdater();
        updateGUIS();
    }

    public void updateGUIS() {
        try {
            this.classroomGUI.updateGUI();
            this.loginGUI.updateGUI();
            this.workspaceGUI.updateGUI();
            this.revalidate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void switchToLoginGUI() {
        try {
            this.getClient().onDisable();
        } catch (Exception ignored) {

        }
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

    public void addToDrawPanel(DrawableObject object) {
        workspaceGUI.addToDrawPanel(object);
    }

    public boolean removeLastDrawnObject() {
        return workspaceGUI.removeLastDrawnObject();
    }

    public boolean removeLastAnnotations() {
        return workspaceGUI.removeLastAnnotations();
    }

    public void clearDrawingPanel() {
        while (workspaceGUI.removeLastDrawnObject());
        while (workspaceGUI.removeLastAnnotations());
    }

    public DrawableObject[] getDrawnObjects() {
        DrawableObject[] objects = workspaceGUI.getDrawnObjects();
        return SerializationUtils.clone(objects);
    }

    public DrawableObject[] getAnnotations() {
        DrawableObject[] objects = workspaceGUI.getAnnotations();
        return SerializationUtils.clone(objects);
    }

    public void addDrawableObjects(DrawableObject[] objects) {
        for (DrawableObject object : objects) {
            addToDrawPanel(object);
        }
    }

    public void saveWorkspace(File file) {
        try {
            var fileOutputStream = new FileOutputStream(file);
            var objectOutputStream = new ObjectOutputStream(fileOutputStream);
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
            var fileInputStream = new FileInputStream(file);
            var objectInputStream = new ObjectInputStream(fileInputStream);
            var list = (Component[][]) objectInputStream.readObject();
            var objects = (DrawableObject[]) list[0];
            var annotations = (DrawableObject[]) list[1];
            objectInputStream.close();
            while (removeLastDrawnObject());
            while (removeLastAnnotations());
            addDrawableObjects(objects);
            addDrawableObjects(annotations);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void syncWorkspace(byte[] input) {
        try {
            var file = new File("input.ser");
            FileUtils.writeByteArrayToFile(file, input);
            var fileInputStream = new FileInputStream(file);
            var objectInputStream = new ObjectInputStream(fileInputStream);
            var list = (Component[][]) objectInputStream.readObject();
            var objects = (DrawableObject[]) list[0];
            var annotations = (DrawableObject[]) list[1];
            objectInputStream.close();
            removeAllIndexedObjects(objects);
            removeAllIndexedObjects(annotations);
            addDrawableObjects(objects);
            addDrawableObjects(annotations);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void syncSingleObject(DrawableObject object) {
        try {
            var file = new File("output.ser");
            var fileOutputStream = new FileOutputStream(file);
            var objectOutputStream = new ObjectOutputStream(fileOutputStream);
            var allComponents = new Component[2][2];
            if (!object.getClass().getSimpleName().matches("Thumb.*")) {
                allComponents[0] = new Component[]{object};
            } else {
                allComponents[1] = new Component[]{object};
            }
            objectOutputStream.writeObject(allComponents);
            objectOutputStream.close();
            var outByteArray = FileUtils.readFileToByteArray(file);
            client.getServer().updateWorkspaceFile(outByteArray, client.getUserId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void removeAllIndexedObjects(DrawableObject[] objects) {
        workspaceGUI.removeAllIndexedObjects(objects);
    }

    public int increaseObjectCounter() {
        return workspaceGUI.increaseObjectCounter();
    }

    public ArrayList<Group> getRelevantGroups() {
        ArrayList<Group> groups = new ArrayList<>();
        ArrayList<Integer> list = client.getCurrentSession().getGroupIds();
        for (Integer id : list){
            try {
                groups.add((Group) client.getGroupManager().load(id));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return groups;
    }

    private void timeUpdater() {

        new Thread(() -> {
            TimerTask task = new TimerTask() {

                @Override
                public void run() {

                    long time;
                    if (client.getCurrentSession() != null) {
                        time = client.getCurrentSession().getRemainingTime();
                        if (client.getCurrentGroup() != null) {
                            time = client.getCurrentGroup().getTimeTillTermination();
                        }
                        int minutes = (int) ((time - System.currentTimeMillis()) / 60000) + 1;
                        setTimeTilLTermination(minutes);
                    }

                }

            };

            Timer timer = new Timer();
            timer.schedule(task, 0, 1000);
        }).start();
    }

    private void setTimeTilLTermination(int minutes) {
        workspaceGUI.setTimeTilLTermination(minutes);
    }

    public void sendRequest(User user) {
        workspaceGUI.sendRequest(user);
    }

    public void sendTaskProposition() {
        workspaceGUI.sendTaskProposition();
    }

    public void setTask(String toString) {
        workspaceGUI.setTask(toString);
    }
}
