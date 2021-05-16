import javax.swing.*;
import java.awt.*;

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

    public void setupGUIS(String[] schools, GradePanel[][] gradePanels) {
        this.loginGUI = new LoginGUI(language, schools, colorScheme);
        this.classroomGUI = new ClassroomGUI(language, gradePanels[0], gradePanels[1], gradePanels[2], colorScheme);
    }

    public void switchToLoginGUI() {
        //this.removeComponents();
        this.setContentPane(loginGUI);
        this.setVisible(true);
    }

    public void switchToClassRoomGUI() {
        //this.removeComponents();
        this.setContentPane(classroomGUI);
        this.setVisible(true);
    }

    private void removeComponents() {
        for (Component component : this.getRootPane().getComponents()) {
            System.out.println(component.getClass().getSimpleName());
            if (!component.getClass().getSimpleName().equals("JLayeredPane")) {
                this.getRootPane().remove(component);
            }
        }
    }
}
