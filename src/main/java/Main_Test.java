import javax.swing.*;
import java.awt.*;

public class Main_Test extends JFrame {

    Main_Test(String name, int size) {
        setTitle(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, size, size);
        setLocationRelativeTo(null);
        setResizable(true);
    }


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
        LoginGUI lg = new LoginGUI("german", schools, colors);

        f.add(lg);
        String[] grades = new String[3];
        grades[0] = "Klasse 10";
        grades[1] = "Klasse 11";
        grades[2] = "Klasse 12";
        CreateClassroomPanel ccp = new CreateClassroomPanel("german", grades, colors);
        PopupFactory pf = new PopupFactory();
        Popup[] p = new Popup[1];

        lg.loginbt.addActionListener(e -> {
            p[0] = pf.getPopup(lg, ccp, f.getX() + lg.loginbt.getX(), f.getY() + lg.loginbt.getY());
            p[0].show();
        });
        ccp.donebt.addActionListener(e -> p[0].hide());

        f.setVisible(true);
        // by doing this, we prevent Swing from resizing
        // our nice component
        //f.setLayout(null);

        //Drawable mc = new Drawable(100,100, Color.blue);
        //Drawable vc = new Drawable(100,200, Color.GREEN);

        //f.add(mc);
        //f.add(vc);
        System.out.println("Hello Yoost!");
    }
}
