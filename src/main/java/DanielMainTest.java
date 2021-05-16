import javax.swing.*;
import java.awt.*;

public class DanielMainTest {
    public static void main(String[] args) {
        AccountType accountType = AccountType.admin;
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
        GradePanel gp = new GradePanel(Language.german, "Klasse 10 a", "Herr irgendwer", "heute", colors, accountType);
        GradePanel gp11 = new GradePanel(Language.german, "Klasse 10 a", "Herr irgendwer", "heute", colors,accountType);
        GradePanel gp111 = new GradePanel(Language.german, "Klasse 10 a", "Herr irgendwer", "heute", colors,accountType);
        GradePanel gp1 = new GradePanel(Language.german, "Klasse 10 a", "Herr irgendwer", "heute", colors,accountType);
        GradePanel gp2 = new GradePanel(Language.german, "Klasse 10 a", "Herr irgendwer", "heute", colors,accountType);
        GradePanel[] gpl10 = {gp, gp11, gp111};
        GradePanel[] gpl11 = {gp1};
        GradePanel[] gpl12 = {gp2};
        GradePanel[][] gradePanels = {gpl10, gpl11, gpl12};

        GUI gui = new GUI(colors, Language.german);
        gui.setupGUIS(schools, gradePanels, grades, students, accountType);
        gui.switchToLoginGUI();
    }
}
