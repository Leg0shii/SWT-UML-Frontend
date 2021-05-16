import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClassroomGUI extends GUIHelper {
    private JPanel mainpanel;
    private JLabel grade11Label;
    private JLabel grade10Label;
    private JLabel grade12Label;
    public JButton logoutButton;
    public JButton privateWorkspaceButton;
    private JButton supportButton;
    public JButton createClassroomButton;
    private JPanel gradepanel12;
    private JPanel gradepanel11;
    private JPanel gradepanel10;
    public Popup[] popups;
    private int[] popupCounter;

    public ClassroomGUI(Language language, GradePanel[][] gradePanels, String[] grades, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainpanel);
        setupLanguage(language);
        this.gradepanel10.setBorder(BorderFactory.createEtchedBorder());
        this.gradepanel10.setLayout(new BoxLayout(this.gradepanel10, BoxLayout.Y_AXIS));
        this.gradepanel11.setBorder(BorderFactory.createEtchedBorder());
        this.gradepanel11.setLayout(new BoxLayout(this.gradepanel11, BoxLayout.Y_AXIS));
        this.gradepanel12.setBorder(BorderFactory.createEtchedBorder());
        this.gradepanel12.setLayout(new BoxLayout(this.gradepanel12, BoxLayout.Y_AXIS));
        for (GradePanel gp : gradePanels[0]) {
            gradepanel10.add(gp);
        }
        for (GradePanel gp : gradePanels[1]) {
            gradepanel11.add(gp);
        }
        for (GradePanel gp : gradePanels[2]) {
            gradepanel12.add(gp);
        }
        this.colorComponents(this.getAllComponents(this, new ArrayList<>()), colors);
        PopupFactory popupFactory = new PopupFactory();
        this.popupCounter = new int[2];
        this.popups = new Popup[2];
        this.supportButton.addActionListener(e -> {
            popupCounter[0]++;
            if (popupCounter[0] % 2 == 0) {
                popups[0].hide();
            } else {
                JPanel supportPanel = new JPanel();
                supportPanel.setBorder(BorderFactory.createEtchedBorder());
                JLabel supportNumber = new JLabel("No Support!");
                supportPanel.add(supportNumber);
                Point point = new Point(this.supportButton.getX(), this.supportButton.getY() - 30);
                SwingUtilities.convertPointToScreen(point, this);
                popups[0] = popupFactory.getPopup(this, supportPanel, point.x, point.y);
                popups[0].show();
            }
        });
        this.createClassroomButton.addActionListener(e -> {
            popupCounter[1]++;
            if (popupCounter[1] % 2 == 0) {
                popups[1].hide();
            } else {
                CreateClassroomPanel createClassroomPanel = new CreateClassroomPanel(language, grades, colors);
                createClassroomPanel.cancelbt.addActionListener(e1 -> {
                    popupCounter[1]++;
                    popups[1].hide();
                });
                createClassroomPanel.donebt.addActionListener(e2 -> {
                    popupCounter[1]++;
                    createClassroomPanel.doneFunction();
                    popups[1].hide();
                });
                createClassroomPanel.addbt.addActionListener(e3 -> {
                    createClassroomPanel.addFunction();
                });
                Point point = new Point(this.createClassroomButton.getX(), this.createClassroomButton.getY() - 200);
                SwingUtilities.convertPointToScreen(point, this);
                popups[1] = popupFactory.getPopup(this, createClassroomPanel, point.x, point.y);
                popups[1].show();
            }
        });
        this.logoutButton.addActionListener(e -> {
            logout();
        });
        this.privateWorkspaceButton.addActionListener(e -> {
            privateWorkspaceFunction();
        });
    }

    private void setupLanguage(Language language) {
        String grade11;
        String grade10;
        String grade12;
        String logout;
        String support;
        String createCR;
        String privateWS;

        switch (language) {
            case german -> {
                grade11 = "Klasse 11";
                grade10 = "Klasse 10";
                grade12 = "Klasse 12";
                logout = "Abmelden";
                support = "Support";
                createCR = "Klassenraum erstellen";
                privateWS = "Privater Arbeitsplatz";
            }
            default -> {
                grade11 = "Grade 11";
                grade10 = "Grade 10";
                grade12 = "Grade 12";
                logout = "Logout";
                support = "Support";
                createCR = "create Classroom";
                privateWS = "private Workspace";
            }
        }
        setupClassroomGUI(grade11, grade10, grade12, logout, support, createCR, privateWS);
    }

    private void setupClassroomGUI(String grade11, String grade10, String grade12, String logout, String support, String createCR, String privateWS) {

        this.grade11Label.setText(grade11);
        this.grade10Label.setText(grade10);
        this.grade12Label.setText(grade12);
        this.logoutButton.setText(logout);
        this.supportButton.setText(support);
        this.createClassroomButton.setText(createCR);
        this.privateWorkspaceButton.setText(privateWS);

    }

    public void updateGUI(GradePanel[][] gradePanels) {
        removeAllGradePanels();
        for (GradePanel gp : gradePanels[0]) {
            gradepanel10.add(gp);
        }
        for (GradePanel gp : gradePanels[1]) {
            gradepanel11.add(gp);
        }
        for (GradePanel gp : gradePanels[2]) {
            gradepanel12.add(gp);
        }
    }

    private void removeAllGradePanels() {
        for (Component component : gradepanel10.getComponents()) {
            gradepanel10.remove(component);
        }
        for (Component component : gradepanel11.getComponents()) {
            gradepanel11.remove(component);
        }
        for (Component component : gradepanel12.getComponents()) {
            gradepanel12.remove(component);
        }
    }

    // TODO: Implemented by other Group
    private void logout(){

    }

    // TODO: Implemented by other Group
    private void privateWorkspaceFunction(){

    }
}
