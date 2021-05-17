package gui.login;

import gui.GUIManager;
import gui.GUI;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LoginGUI extends GUI {
    private JPanel mainPanel;
    public JButton loginButton;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JComboBox<String> schoolDropDown;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JLabel schoolLabel;
    private JLabel usernameLabel;
    private JPanel subPanel;
    public GUIManager guiManager;

    public LoginGUI(GUIManager guiManager) {
        this.guiManager = guiManager;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        subPanel.setBorder(BorderFactory.createEtchedBorder());
        switch (guiManager.language) {
            case german -> setupGUI("Anmeldung", "Nutzername:", "Passwort:", "Schule:", "Anmelden");
            case english -> setupGUI("Login", "Username:", "Password:", "School:", "Login");
        }
        setupActionListeners();
        colorComponents(this.getAllComponents(this, new ArrayList<>()), guiManager.colorScheme,1);
    }

    private void setupGUI(String login, String username, String password, String school, String button) {
        loginLabel.setText(login);
        usernameLabel.setText(username);
        passwordLabel.setText(password);
        schoolLabel.setText(school);
        loginButton.setText(button);
    }

    public void updateGUI(String[] schools){
        for (String temp : schools) {
            schoolDropDown.addItem(temp);
        }
    }

    public void setupActionListeners(){
        this.loginButton.addActionListener(e -> login());
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getUsername() {
        return usernameTextField.getText();
    }

    public String getSchool() {
        return (String) schoolDropDown.getSelectedItem();
    }

    // TODO: Implemented by other Group
    private void login() {
        guiManager.switchToClassRoomGUI();
    }

}
