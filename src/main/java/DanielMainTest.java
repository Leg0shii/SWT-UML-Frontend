import gui.GUI;
import gui.classroom.GradePanel;
import util.AccountType;
import util.Language;

import javax.swing.*;
import java.awt.*;

public class DanielMainTest {
    public static void main(String[] args) {
        AccountType accountType = AccountType.teacher;
        Language language = Language.german;
        String[] schools = new String[3];
        schools[0] = "Schule A";
        schools[1] = "Schule B";
        schools[2] = "Schule C";
        Color[] colors = new Color[4];
        colors[0] = Color.decode("#EDEAE5"); // Background
        colors[1] = Color.decode("#FEF9C7"); // Second Background
        colors[2] = Color.decode("#000000"); // Text
        colors[3] = Color.decode("#9FEDD7"); // Button Background
        Color[] colors2 = new Color[4];
        colors2[0] = Color.decode("#41B3A3"); // Background
        colors2[1] = Color.decode("#85DCB"); // Second Background
        colors2[2] = Color.decode("#FFFFFF"); // Text
        colors2[3] = Color.decode("#C38D9E"); // Button Background
        String[] grades = new String[3];
        grades[0] = "Klasse 10";
        grades[1] = "Klasse 11";
        grades[2] = "Klasse 12";
        String[] students = new String[4];
        students[0] = "Yoost";
        students[1] = "Daniel";
        students[2] = "Yoosta";
        students[3] = "Daniela";

        GUI gui = new GUI(colors, language);
        gui.setupGUIS();
        gui.updateGUIS(schools, students, accountType);
        gui.switchToLoginGUI();

        JFrame frame = new JFrame("Tests");
        JButton button = new JButton("Add GradePanel!");
        button.addActionListener(e -> {
            GradePanel gradePanel = new GradePanel(language, colors, accountType);
            gradePanel.updateGUI("Klasse 10 b", "Herr Maier", "Mo 7-9");
            gui.classroomGUI.addGradePanel(gradePanel);
            gui.updateGUIS(schools, students, accountType);
        });
        JButton button2 = new JButton("To 11 Grade!");
        button2.addActionListener(e -> {
            gui.classroomGUI.getGradePanel("Klasse 10 b", "Herr Maier", "Mo 7-9").updateGUI("Klasse 11 c", "Frau Heuer", "Mo 9-11");
            gui.updateGUIS(schools, students, accountType);
        });
        frame.setLayout(new FlowLayout());
        frame.add(button);
        frame.add(button2);
        frame.pack();
        frame.setVisible(true);
    }
}
