import javax.swing.*;

public class GradePane {
    public JPanel panel1;
    private JPanel subpanel;
    private JButton beitretenButton;
    private JButton bearbeitenButton;
    private JLabel terminLabel;
    private JLabel lehrerLabel;
    private JLabel nextdatelabel;
    private JLabel thisteacherlabel;
    private JLabel gradeheader;

    public GradePane(String grade, String thisteacher, String nextdate){

        this.gradeheader.setText(grade);
        this.thisteacherlabel.setText(thisteacher);
        this.nextdatelabel.setText(nextdate);
        this.subpanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
    }


    public GradePane(String beitreten, String bearbeiten, String lehrer, String termin, String grade, String thisteacher, String nextdate){

        this.beitretenButton.setText(beitreten);
        this.bearbeitenButton.setText(bearbeiten);
        this.lehrerLabel.setText(lehrer);
        this.terminLabel.setText(termin);
        this.gradeheader.setText(grade);
        this.thisteacherlabel.setText(thisteacher);
        this.nextdatelabel.setText(nextdate);
        this.subpanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());

    }


}
