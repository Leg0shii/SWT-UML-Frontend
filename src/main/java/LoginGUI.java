import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LoginGUI extends GUIHelper {
    private JPanel mainpanel;
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
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainpanel);
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
        this.colorComponents(this.getAllComponents(this, new ArrayList<>()), colors);
        subpanel.setBorder(BorderFactory.createEtchedBorder());
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
