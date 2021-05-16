import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CreateClassroomPanel extends GUIHelper {
    private JPanel mainpanel;
    private JComboBox<String> gradecb;
    private JTextField gradenametf;
    private JTextField teachertf;
    private JTextField datetf;
    private JTextField studenttf;
    public JButton addbt;
    public JButton donebt;
    public JButton cancelbt;
    private JLabel gradelb;
    private JLabel gradenamelb;
    private JLabel teacherlb;
    private JLabel datelb;
    private JLabel studentlb;

    public CreateClassroomPanel(Language language, String[] grades, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainpanel);
        this.setBorder(BorderFactory.createEtchedBorder());
        switch (language) {
            case german:
                this.setupGUI("Klassenstufe", "Klassenname", "Lehrer", "Termin", "Schüler", "Hinzufügen", "Fertig", "Abbrechen");
                break;
            case english:
                this.setupGUI("Grade", "Grade name", "Teacher", "Date", "Student", "Add", "Done", "Cancel");
                break;
        }
        for (String temp : grades) {
            this.gradecb.addItem(temp);
        }
        this.colorComponents(this.getAllComponents(this, new ArrayList<>()), colors);
    }

    private void setupGUI(String grade, String gradename, String teacher, String date, String student, String add, String done, String cancel) {
        this.gradelb.setText(grade);
        this.gradenamelb.setText(gradename);
        this.teacherlb.setText(teacher);
        this.datelb.setText(date);
        this.studentlb.setText(student);
        this.addbt.setText(add);
        this.donebt.setText(done);
        this.cancelbt.setText(cancel);
    }

    // TODO : Other Group adds this Function!
    public void doneFunction(){

    }

    // TODO: Other Group adds this Function
    public void addFunction() {

    }
}
