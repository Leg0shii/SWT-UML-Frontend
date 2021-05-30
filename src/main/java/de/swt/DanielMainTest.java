package de.swt;

import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;
import de.swt.gui.GUIManager;
import de.swt.gui.classroom.GradePanel;
import de.swt.logic.group.Group;
import de.swt.logic.user.User;
import de.swt.util.AccountType;
import de.swt.logic.course.Course;
import de.swt.util.Client;
import de.swt.util.Language;
import de.swt.util.WorkspaceState;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class DanielMainTest {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatSolarizedLightIJTheme());
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

        ArrayList<Group> groups = new ArrayList<>();

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
        JButton button8 = new JButton("Change Workspacestate to Viewing");
        button8.addActionListener(e -> {
            guiManager.setWorkspaceState(WorkspaceState.VIEWING);
        });
        JButton button9 = new JButton("Change Workspacestate to Annotating");
        button9.addActionListener(e -> {
            guiManager.setWorkspaceState(WorkspaceState.ANNOTATING);
        });
        JButton button10 = new JButton("Change Workspacestate to Editing");
        button10.addActionListener(e -> {
            guiManager.setWorkspaceState(WorkspaceState.EDITING);
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
        frame.add(button10);
        frame.pack();
        frame.setVisible(true);
    }
}
