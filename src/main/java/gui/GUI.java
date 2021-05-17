package gui;

import gui.classroom.ClassroomGUI;
import gui.classroom.GradePanel;
import gui.login.LoginGUI;
import util.AccountType;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GUI extends JFrame {
    Color[] colorScheme;
    public LoginGUI loginGUI;
    public ClassroomGUI classroomGUI;
    Language language;

    public GUI(Color[] colorScheme, Language language) {
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
        this.loginGUI = new LoginGUI(language, colorScheme);
        this.loginGUI.gui = this;
        this.classroomGUI = new ClassroomGUI(language, colorScheme);
        this.classroomGUI.gui = this;
    }

    public void updateGUIS(String[] schools, String[] students, AccountType accountType) {
        this.classroomGUI.updateGUI(students, accountType);
        this.loginGUI.updateGUI(schools);
    }

    public void switchToLoginGUI() {
        this.setContentPane(loginGUI);
        this.setVisible(true);
    }

    public void switchToClassRoomGUI() {
        this.setContentPane(classroomGUI);
        this.setVisible(true);
    }
}
