import jdk.jfr.Event;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

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
        colors[0] = Color.decode("#EDEAE5");
        colors[1] = Color.decode("#FEF9C7");
        colors[2] = Color.decode("#000000");
        colors[3] = Color.decode("#9FEDD7");
        LoginGUI lg = new LoginGUI("Anmeldung", "Nutzername:", "Passwort:", "Schule:", "Anmelden", schools, colors);
        lg.loginbt.addActionListener(e -> {
            System.out.println(lg.get_school() + " " + lg.get_username() + " " + lg.get_password());
        });

        f.add(lg.$$$getRootComponent$$$());
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
