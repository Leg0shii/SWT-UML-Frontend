import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ClassroomGUI {

    public JPanel panel1;
    private JLabel grade11Label;
    private JLabel grade10Label;
    private JLabel grade12Label;
    private JButton logoutButton;
    private JButton privateWorkspaceButton;
    private JButton supportButton;
    private JButton createClassroomButton;
    private JPanel gradepanel12;
    private JPanel gradepanel11;
    private JPanel gradepanel10;

    public ClassroomGUI(GradePane[] gpl10, GradePane[] gpl11, GradePane[] gpl12){
        setupLanguage(Language.german);
        this.gradepanel10.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.gradepanel11.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.gradepanel12.setBorder(BorderFactory.createRaisedSoftBevelBorder());



        Box bv10 = Box.createVerticalBox();
        for (GradePane gradePane : gpl10) {
            bv10.add(gradePane.panel1);
            bv10.add(Box.createVerticalStrut(10));
        }
        gradepanel10.add(BorderLayout.NORTH, bv10);

        Box bv11 = Box.createVerticalBox();
        for (GradePane gradePane : gpl11) {
            bv11.add(gradePane.panel1);
            bv11.add(Box.createVerticalStrut(10));
        }
        gradepanel11.add(BorderLayout.NORTH, bv11);

        Box bv12 = Box.createVerticalBox();
        for (GradePane gradePane : gpl12) {
            bv12.add(gradePane.panel1);
            bv12.add(Box.createVerticalStrut(10));
        }
        gradepanel12.add(BorderLayout.NORTH, bv12);




    }
    public ClassroomGUI(Language language, GradePane[] gpl10, GradePane[] gpl11, GradePane[] gpl12){
        setupLanguage(language);
        this.gradepanel10.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.gradepanel11.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.gradepanel12.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        for(GradePane gp : gpl10){
            gradepanel10.add(gp.panel1);
        }
        for(GradePane gp : gpl11){
            gradepanel11.add(gp.panel1);
        }
        for(GradePane gp : gpl12){
            gradepanel12.add(gp.panel1);
        }
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
        setupClassroomGUI(grade11,grade10,grade12,logout,support,createCR,privateWS);
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
