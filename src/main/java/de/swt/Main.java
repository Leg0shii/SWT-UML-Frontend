package de.swt;

import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;
import de.swt.drawing.objects.UseCase;
import de.swt.gui.GUIManager;
import de.swt.gui.classroom.GradePanel;
import de.swt.gui.workspace.CreateGroupPanel;
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

public class Main {
    public static void main(String[] args) {
        Application application = new Application(AccountType.TEACHER, Language.GERMAN);

        JFrame frame = new JFrame("Tests");
        JButton button = new JButton("Add GradePanel!");

        final int[] counter = {0};

        JButton button6 = new JButton("Switch to ClassroomGUI");
        button6.addActionListener(e -> {
            application.guiManager.switchToClassRoomGUI();
        });
        JButton button7 = new JButton("Switch to WorkspaceGUI");
        button7.addActionListener(e -> {
            application.guiManager.switchToWorkspaceGUI();
        });
        JButton button8 = new JButton("Change Workspacestate to Viewing");
        button8.addActionListener(e -> {
            application.guiManager.setWorkspaceState(WorkspaceState.VIEWING);
        });
        JButton button9 = new JButton("Change Workspacestate to Annotating");
        button9.addActionListener(e -> {
            application.guiManager.setWorkspaceState(WorkspaceState.ANNOTATING);
        });
        JButton button10 = new JButton("Change Workspacestate to Editing");
        button10.addActionListener(e -> {
            application.guiManager.setWorkspaceState(WorkspaceState.EDITING);
        });
        frame.setLayout(new FlowLayout());
        frame.add(button6);
        frame.add(button7);
        frame.add(button8);
        frame.add(button9);
        frame.add(button10);
        frame.pack();
        frame.setVisible(true);
    }
}
