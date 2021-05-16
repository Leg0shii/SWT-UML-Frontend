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

    public void setupGUIS(String[] schools, GradePanel[][] gradePanels, String[] grades, String[] students, AccountType accountType) {
        this.loginGUI = new LoginGUI(language, schools, colorScheme);
        this.loginGUI.gui = this;
        this.classroomGUI = new ClassroomGUI(language, gradePanels, grades, colorScheme);
        this.classroomGUI.gui = this;
        this.classroomGUI.initForAccountType(accountType);
        setAllStudents(students, gradePanels);
    }

    public void updateGUIS(GradePanel[][] gradePanels, String[] students) {
        this.classroomGUI.updateGUI(gradePanels);
        setAllStudents(students, gradePanels);
    }

    public void setAllStudents(String[] students, GradePanel[][] gradePanels) {
        for (GradePanel[] gradePanelGroup : gradePanels) {
            for (GradePanel gradePanel : gradePanelGroup) {
                gradePanel.setStudents(students);
            }
        }
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
