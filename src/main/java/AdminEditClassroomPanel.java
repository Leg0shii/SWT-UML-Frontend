import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AdminEditClassroomPanel extends GUIHelper{
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
            case german -> setupGUI("<html>neue <br>Klassenstufe</html>", "<html>Schülerliste <br>zurücksetzen</html>", "Klasse migrieren", "Löschen", "Abbrechen");
            case english -> setupGUI("New grade", "Reset student list", "Migrate grade", "Delete", "Cancel");
        }
        this.colorComponents(this.getAllComponents(this, new ArrayList<>()), colors);

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

    // TODO: Other Group implements this
    public void migrateFunction() {
    }

    // TODO: Other Group implements this
    public void resetFunction() {
    }

    // TODO: Other Group implements this
    public void deleteFunction() {
    }
}
