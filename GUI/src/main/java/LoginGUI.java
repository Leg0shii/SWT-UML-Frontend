import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;

public class LoginGUI {
    public JPanel loginpanel;
    public JButton loginbt;
    private JTextField usernametf;
    private JPasswordField passwordf;
    private JComboBox<String> schooldropdown;
    private JLabel loginlb;
    private JLabel passwordlb;
    private JLabel schoollb;
    private JLabel usernamelb;
    private JPanel subpanel;

    public LoginGUI(String login, String username, String password, String school, String button, String[] schools, Color[] colors) {
        loginlb.setText(login);
        usernamelb.setText(username);
        passwordlb.setText(password);
        schoollb.setText(school);
        loginbt.setText(button);
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

    public String get_password() {
        return new String(passwordf.getPassword());
    }

    public String get_username() {
        return usernametf.getText();
    }

    public String get_school() {
        return (String) schooldropdown.getSelectedItem();
    }


}
