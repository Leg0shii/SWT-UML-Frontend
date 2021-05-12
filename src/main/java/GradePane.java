import javax.swing.*;

public class GradePane {
    public JPanel panel1;
    public JPanel subpanel;
    private JButton enterButton;
    private JButton editButton;
    private JLabel dateLabel;
    private JLabel teacherLabel;
    private JLabel nextdatelabel;
    private JLabel thisteacherlabel;
    private JLabel gradeheader;

    public GradePane(String grade, String thisteacher, String nextdate){

        setupLanguage(Language.german);
        this.gradeheader.setText(grade);
        this.thisteacherlabel.setText(thisteacher);
        this.nextdatelabel.setText(nextdate);
        this.subpanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
    }
    public GradePane(Language language, String grade, String thisteacher, String nextdate){

        setupLanguage(language);
        this.gradeheader.setText(grade);
        this.thisteacherlabel.setText(thisteacher);
        this.nextdatelabel.setText(nextdate);
        this.subpanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
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
}
