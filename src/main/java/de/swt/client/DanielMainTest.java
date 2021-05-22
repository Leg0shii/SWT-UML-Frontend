package de.swt.client;

import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import de.swt.client.gui.GUIManager;
import de.swt.client.gui.classroom.GradePanel;
import de.swt.client.clientlogic.Group;
import de.swt.client.util.AccountType;
import de.swt.client.clientlogic.Course;
import de.swt.client.util.Client;
import de.swt.client.util.Language;
import de.swt.client.clientlogic.User;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class DanielMainTest {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatArcOrangeIJTheme());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Client client = new Client();
        client.onStart();
        ArrayList<User> userList = new ArrayList<>();
        ArrayList<Course> courseList = new ArrayList<>();
        for (int key : client.userManager.getUserHashMap().keySet()) {
            userList.add(client.userManager.getUserHashMap().get(key));
        }
        for (int key : client.courseManager.getCourseHashMap().keySet()) {
            courseList.add(client.courseManager.getCourseHashMap().get(key));
        }
        AccountType accountType = AccountType.TEACHER;
        Language language = Language.GERMAN;

        String[] schools = new String[3];
        schools[0] = "Schule A";
        schools[1] = "Schule B";
        schools[2] = "Schule C";

        ArrayList<Group> groups = new ArrayList<>() {{
            add(new Group(1, 10, 5, userList, "TestWorkspace1"));
            add(new Group(2, 10, 5, userList, "TestWorkspace2"));
            add(new Group(3, 10, 5, userList, "TestWorkspace3"));
            add(new Group(4, 10, 5, userList, "TestWorkspace4"));
        }};

        GUIManager guiManager = new GUIManager(client, language, accountType);
        guiManager.setupGUIS();
        guiManager.updateGUIS(schools, userList, groups, userList, 0);
        guiManager.switchToLoginGUI();

        JFrame frame = new JFrame("Tests");
        JButton button = new JButton("Add GradePanel!");

        final int[] counter = {0};

        for (Course course : courseList) {
            GradePanel gradePanel = new GradePanel(guiManager);
            gradePanel.updateGUI(course);
            guiManager.classroomGUI.addGradePanel(gradePanel);
            guiManager.updateGUIS(schools, userList, groups, userList, 1);
        }

        button.addActionListener(e -> {
            GradePanel gradePanel = new GradePanel(guiManager);
            gradePanel.updateGUI(courseList.get(counter[0] % courseList.size()));
            guiManager.classroomGUI.addGradePanel(gradePanel);
            guiManager.updateGUIS(schools, userList, groups, userList, 1);
            counter[0]++;
        });
        JButton button2 = new JButton("To 11 Grade!");
        button2.addActionListener(e -> {
            GradePanel gradePanel = guiManager.classroomGUI.getGradePanel(courseList.get(2).getId());
            Course course = gradePanel.getCourse();
            course.setGrade(11);
            gradePanel.updateGUI(course);
            guiManager.updateGUIS(schools, userList, groups, userList, 0);
        });
        JButton button3 = new JButton("Send Request to Join!");
        button3.addActionListener(e -> {
            guiManager.workspaceGUI.sendRequest(userList.get(1));
        });
        JButton button4 = new JButton("Send Task Proposition!");
        button4.addActionListener(e -> {
            guiManager.workspaceGUI.sendTaskProposition(groups);
        });
        JButton button5 = new JButton("Set Task");
        button5.addActionListener(e -> {
            guiManager.workspaceGUI.setTask("Test Task");
        });
        JButton button6 = new JButton("Switch to ClassroomGUI");
        button6.addActionListener(e -> {
            guiManager.switchToClassRoomGUI();
        });
        JButton button7 = new JButton("Switch to WorkspaceGUI");
        button7.addActionListener(e -> {
            guiManager.switchToWorkspaceGUI();
        });
        File file= new File("src\\main\\resources\\test.ser");
        JButton button8 = new JButton("Save Drawn Objects");
        button8.addActionListener(e -> {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(guiManager.getDrawnObjects());
                objectOutputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        JButton button9 = new JButton("Load Drawn Objects");
        button9.addActionListener(e -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Component[] list = (Component[]) objectInputStream.readObject();
                objectInputStream.close();
                guiManager.addDrawnObjects(list);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        frame.setLayout(new FlowLayout());
        frame.add(button);
        frame.add(button2);
        frame.add(button3);
        frame.add(button4);
        frame.add(button5);
        frame.add(button6);
        frame.add(button7);
        frame.add(button8);
        frame.add(button9);
        frame.pack();
        frame.setVisible(true);
    }
}
