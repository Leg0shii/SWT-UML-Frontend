import javax.swing.*;
import java.awt.*;

public class CreateClassroomPanel extends JPanel {
    private JPanel createclassroompanel;
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

    public CreateClassroomPanel(String language, String[] grades, Color[] colors) {
        this.setLayout(new BoxLayout(this, 2));
        this.add(createclassroompanel);
        this.setBorder(BorderFactory.createEtchedBorder());
        switch (language) {
            case "german":
                this.setupGUI("Klassenstufe", "Klassenname", "Lehrer", "Termin", "Schüler", "Hinzufügen", "Fertig", "Abbrechen");
                break;
            case "english":
                this.setupGUI("Grade", "Grade name", "Teacher", "Date", "Student", "Add", "Done", "Cancel");
                break;
        }
        for (String temp : grades) {
            this.gradecb.addItem(temp);
        }
        this.createclassroompanel.setBackground(colors[0]);
        this.gradelb.setForeground(colors[2]);
        this.gradenamelb.setForeground(colors[2]);
        this.teacherlb.setForeground(colors[2]);
        this.datelb.setForeground(colors[2]);
        this.studentlb.setForeground(colors[2]);
        this.addbt.setForeground(colors[2]);
        this.donebt.setForeground(colors[2]);
        this.cancelbt.setForeground(colors[2]);
        this.addbt.setBackground(colors[3]);
        this.donebt.setBackground(colors[3]);
        this.cancelbt.setBackground(colors[3]);
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

}
