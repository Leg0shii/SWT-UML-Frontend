package de.swt.gui.login;

import de.swt.gui.GUIManager;
import de.swt.gui.GUI;
import de.swt.util.Language;

import javax.swing.*;

public class LoginGUI extends GUI {
    private JPanel mainPanel;
    public JButton loginButton;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JLabel usernameLabel;
    private JPanel subPanel;

    public LoginGUI(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        subPanel.setBorder(BorderFactory.createEtchedBorder());
        switch (guiManager.language) {
            case GERMAN -> setupGUI("Anmeldung", "Nutzername:", "Passwort:", "Schule:", "Anmelden");
            case ENGLISH -> setupGUI("Login", "Username:", "Password:", "School:", "Login");
        }
        setupActionListeners();
    }

    private void setupGUI(String login, String username, String password, String school, String button) {
        loginLabel.setText(login);
        usernameLabel.setText(username);
        passwordLabel.setText(password);
        loginButton.setText(button);
    }

    public void updateGUI(){
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

    // TODO: Implemented by other Group
    private void login() {
        int loginID;
        try {
            loginID = Integer.parseInt(getUsername());
        } catch (NumberFormatException numberFormatException) {
            // TODO : Errorlabel if no number entered
            System.out.println("Please enter a real number as ID!");
            return;
        }
        String password = getPassword();
        if(guiManager.getClient().userManager.userLogin(loginID, password)) {
            guiManager.getClient().loggedIn = true;
            guiManager.switchToClassRoomGUI();
        } else {
            System.out.println("Login failed");
            // TODO : Errorlabel if login fails
        }
    }


}
