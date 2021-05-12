import javax.swing.*;
import java.awt.*;

public class AdminEditClassroomPanel extends JPanel{
    private JPanel mainpanel;
    private JTextField gradetf;
    public JButton resetbt;
    public JButton migratebt;
    public JButton deletebt;
    public JButton cancelbt;
    private JLabel gradelb;

    public AdminEditClassroomPanel(Language language, Color[] colors){
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainpanel);
        this.setBorder(BorderFactory.createEtchedBorder());
        switch (language){
            case german -> setupGUI("neue Klassenstufe", "Schülerliste zurücksetzen", "Klasse migrieren", "Löschen", "Abbrechen");
            case english -> setupGUI("New grade", "Reset student list", "Migrate grade", "Delete", "Cancel");
        }
        this.mainpanel.setBackground(colors[0]);
        this.gradelb.setForeground(colors[2]);
        this.resetbt.setForeground(colors[2]);
        this.migratebt.setForeground(colors[2]);
        this.deletebt.setForeground(colors[2]);
        this.cancelbt.setForeground(colors[2]);
        this.resetbt.setBackground(colors[3]);
        this.migratebt.setBackground(colors[3]);
        this.deletebt.setBackground(colors[3]);
        this.cancelbt.setBackground(colors[3]);

    }

    public String getGrade(){
        return gradetf.getText();
    }

    private void setupGUI(String grade, String reset, String migrate, String delete, String cancel){
        this.gradelb.setText(grade);
        this.resetbt.setText(reset);
        this.migratebt.setText(migrate);
        this.deletebt.setText(delete);
        this.cancelbt.setText(cancel);

    }
}
