import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JPanel {
    private JPanel loginpanel;
    public JButton loginbt;
    private JTextField usernametf;
    private JPasswordField passwordf;
    private JComboBox<String> schooldropdown;
    private JLabel loginlb;
    private JLabel passwordlb;
    private JLabel schoollb;
    private JLabel usernamelb;
    private JPanel subpanel;

    public LoginGUI(Language language, String[] schools, Color[] colors) {
        this.setLayout(new BoxLayout(this, 2));
        this.add(loginpanel);
        switch (language) {
            case german:
                this.setupGUI("Anmeldung", "Nutzername:", "Passwort:", "Schule:", "Anmelden");
                break;
            case english:
                this.setupGUI("Login", "Username:", "Password:", "School:", "Login");
                break;
        }
        for (String temp : schools) {
            schooldropdown.addItem(temp);
        }
        loginpanel.setBackground(colors[0]);
        subpanel.setBackground(colors[1]);
        usernamelb.setForeground(colors[2]);
        loginlb.setForeground(colors[2]);
        passwordlb.setForeground(colors[2]);
        schoollb.setForeground(colors[2]);
        loginbt.setForeground(colors[2]);
        loginbt.setBackground(colors[3]);
        subpanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
    }

    public String getPassword() {
        return new String(passwordf.getPassword());
    }

    public String getUsername() {
        return usernametf.getText();
    }

    public String getSchool() {
        return (String) schooldropdown.getSelectedItem();
    }

    private void setupGUI(String login, String username, String password, String school, String button) {
        loginlb.setText(login);
        usernamelb.setText(username);
        passwordlb.setText(password);
        schoollb.setText(school);
        loginbt.setText(button);
    }


}
