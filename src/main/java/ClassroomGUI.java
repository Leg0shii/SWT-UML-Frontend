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
    private int popupCounter;

    public ClassroomGUI(Language language, GradePanel[] gpl10, GradePanel[] gpl11, GradePanel[] gpl12, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainpanel);
        setupLanguage(language);
        this.gradepanel10.setBorder(BorderFactory.createEtchedBorder());
        this.gradepanel10.setLayout(new BoxLayout(this.gradepanel10, BoxLayout.Y_AXIS));
        this.gradepanel11.setBorder(BorderFactory.createEtchedBorder());
        this.gradepanel11.setLayout(new BoxLayout(this.gradepanel11, BoxLayout.Y_AXIS));
        this.gradepanel12.setBorder(BorderFactory.createEtchedBorder());
        this.gradepanel12.setLayout(new BoxLayout(this.gradepanel12, BoxLayout.Y_AXIS));
        for (GradePanel gp : gpl10) {
            gradepanel10.add(gp);
        }
        for (GradePanel gp : gpl11) {
            gradepanel11.add(gp);
        }
        for (GradePanel gp : gpl12) {
            gradepanel12.add(gp);
        }
        this.colorComponents(this.getAllComponents(this, new ArrayList<>()), colors);
        PopupFactory popupFactory = new PopupFactory();
        this.popupCounter = 0;
        this.popups = new Popup[2];
        this.supportButton.addActionListener(e -> {
            popupCounter++;
            if (popupCounter % 2 == 0) {
                popups[0].hide();
            } else {
                JPanel supportPanel = new JPanel();
                supportPanel.setBorder(BorderFactory.createEtchedBorder());
                JLabel supportNumber = new JLabel("No Support!");
                supportPanel.add(supportNumber);
                Point point = new Point(this.supportButton.getX(), this.supportButton.getY()-30);
                SwingUtilities.convertPointToScreen(point, this);
                popups[0] = popupFactory.getPopup(this, supportPanel, point.x, point.y);
                popups[0].show();
            }
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
}
