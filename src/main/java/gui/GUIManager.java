package gui;

import gui.classroom.ClassroomGUI;
import gui.login.LoginGUI;
import util.AccountType;
import util.Language;
import logic.user.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUIManager extends JFrame {
    public Color[] colorScheme;
    public Language language;

    public LoginGUI loginGUI;
    public ClassroomGUI classroomGUI;

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
    }

    public void updateGUIS(String[] schools, ArrayList<User> students, AccountType accountType) {
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

    public void insertNewPanel() {

    }

    public Color[] getColorScheme() {
        return colorScheme;
    }

    public Language getLanguage() {
        return language;
    }
}
