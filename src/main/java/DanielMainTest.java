import gui.GUIManager;
import gui.classroom.GradePanel;
import util.AccountType;
import util.Course;
import util.Language;
import util.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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

        // load from Database
        ArrayList<User> students = new ArrayList<>() {{
            add(new User("2", "Yoost1", "surname"));
            add(new User("3", "Yoost2", "surname"));
            add(new User("4", "Yoost3", "surname"));
            add(new User("5", "Yoost4", "surname"));
        }};

        Course testCourse = new Course(2, 12, 'a', new ArrayList<>(), new User("0", "prename", "surname"), students);

        ArrayList<Course> courses = new ArrayList<>() {{
            add(new Course(0, 10, 'a', new ArrayList<>(), new User("0", "prename", "surname"), students));
            add(new Course(1, 11, 'b', new ArrayList<>(), new User("1", "prename", "surname"), students));
        }};

        GUIManager guiManager = new GUIManager(colors, language);
        guiManager.setupGUIS();
        guiManager.updateGUIS(schools, students, accountType);
        guiManager.switchToLoginGUI();

        JFrame frame = new JFrame("Tests");
        JButton button = new JButton("Add GradePanel!");

        button.addActionListener(e -> {
            GradePanel gradePanel = new GradePanel(testCourse, colors, language, accountType);
            gradePanel.updateGUI(testCourse);
            guiManager.classroomGUI.addGradePanel(gradePanel);
            guiManager.updateGUIS(schools, students, accountType);
        });
        JButton button2 = new JButton("To 11 Grade!");
        button2.addActionListener(e -> {
            GradePanel gradePanel = guiManager.classroomGUI.getGradePanel(testCourse.getID());
            Course migrateCourse = gradePanel.getCourse();
            migrateCourse.setGrade(11);
            gradePanel.updateGUI(migrateCourse);
            guiManager.updateGUIS(schools, students, accountType);
        });

        frame.setLayout(new FlowLayout());
        frame.add(button);
        frame.add(button2);
        frame.pack();
        frame.setVisible(true);
    }
}
