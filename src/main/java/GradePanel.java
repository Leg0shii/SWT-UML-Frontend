import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GradePanel extends GUIHelper {
    private JPanel mainpanel;
    public JButton enterButton;
    private JButton editButton;
    private JLabel dateLabel;
    private JLabel teacherLabel;
    private JLabel nextdatelabel;
    private JLabel thisteacherlabel;
    private JLabel gradeheader;
    private String[] students;

    public GradePanel(Language language, String grade, String thisteacher, String nextdate, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainpanel);
        setupLanguage(language);
        this.gradeheader.setText(grade);
        this.thisteacherlabel.setText(thisteacher);
        this.nextdatelabel.setText(nextdate);
        this.setBorder(BorderFactory.createEtchedBorder());
        this.colorComponents(this.getAllComponents(this, new ArrayList<>()), colors);
        PopupFactory popupFactory = new PopupFactory();
        Popup[] popups = new Popup[1];
        this.editButton.addActionListener(e -> {
            Point point = new Point(this.editButton.getX() - this.getWidth()/2, 0);
            SwingUtilities.convertPointToScreen(point, this);
            EditClassroomPanel editClassroomPanel = new EditClassroomPanel(language, students, colors);
            editClassroomPanel.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
            editClassroomPanel.cancelbt.addActionListener(e1 -> {
                popups[0].hide();
            });
            editClassroomPanel.donebt.addActionListener(e2 ->{
                editClassroomPanel.doneFunction();
                popups[0].hide();
            });
            popups[0] = popupFactory.getPopup(this, editClassroomPanel, point.x, point.y);
            popups[0].show();
        });
        this.enterButton.addActionListener(e1 -> {
            this.enterFunction();
        });
    }

    private void setupLanguage(Language language) {
        String enter;
        String edit;
        String teacher;
        String date;
        switch (language) {
            case german -> {
                enter = "Beitreten";
                edit = "Bearbeiten";
                teacher = "Lehrer:";
                date = "Termin:";
            }
            default -> {
                enter = "Enter";
                edit = "Edit";
                teacher = "Teacher:";
                date = "When?:";
            }
        }
        setupGradePane(enter, edit, teacher, date);
    }

    private void setupGradePane(String enter, String edit, String teacher, String date) {
        this.enterButton.setText(enter);
        this.editButton.setText(edit);
        this.teacherLabel.setText(teacher);
        this.dateLabel.setText(date);
    }

    public void setStudents(String[] students) {
        this.students = students;
    }

    // TODO: Other Group implements this
    private void enterFunction(){

    }
}
