import javax.swing.*;
import java.awt.*;

public class DanielMainTest {
    public static void main(String[] args) {
        Main_Test f = new Main_Test("Gruppe 31", 500);

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

        LoginGUI lg = new LoginGUI(Language.german, schools, colors);

        f.add(lg);
        String[] grades = new String[3];
        grades[0] = "Klasse 10";
        grades[1] = "Klasse 11";
        grades[2] = "Klasse 12";
        String[] students = new String[4];
        students[0] = "Yoost";
        students[1] = "Daniel";
        students[2] = "Yoosta";
        students[3] = "Daniela";
        AdminEditClassroomPanel a = new AdminEditClassroomPanel(Language.german, colors);
        PopupFactory pf = new PopupFactory();
        Popup[] p = new Popup[2];
        lg.loginbt.addActionListener(e -> {
            Point point = new Point(lg.loginbt.getX(),lg.loginbt.getY());
            SwingUtilities.convertPointToScreen(point,f);
            p[0] = pf.getPopup(lg, a, point.x, point.y);
            p[0].show();
        });

        f.setVisible(true);
    }
}
