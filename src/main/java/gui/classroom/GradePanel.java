package gui.classroom;

import gui.GUIHelper;
import util.AccountType;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GradePanel extends GUIHelper {
    private JPanel mainpanel;
    public JButton enterButton;
    public JButton editButton;
    private JLabel dateLabel;
    private JLabel teacherLabel;
    private JLabel nextdatelabel;
    private JLabel thisteacherlabel;
    private JLabel gradeheader;
    private String[] students;
    private Popup[] popups;
    private Language language;
    private Color[] colors;

    public GradePanel(Language language, String grade, String thisteacher, String nextdate, Color[] colors, AccountType accountType) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainpanel);
        setupLanguage(language);
        this.gradeheader.setText(grade);
        this.thisteacherlabel.setText(thisteacher);
        this.nextdatelabel.setText(nextdate);
        this.language = language;
        this.colors = colors;
        this.setBorder(BorderFactory.createEtchedBorder());
        this.colorComponents(this.getAllComponents(this, new ArrayList<>()), colors);
        this.setupActionListeners(accountType);
    }

    private void setupLanguage(Language language) {
        if (language == Language.german) setupGradePane("Beitreten","Bearbeiten","Lehrer:","Termin:");
        else setupGradePane("Enter","Edit","Teacher:","When?:");
    }

    private void setupGradePane(String enter, String edit, String teacher, String date) {
        this.enterButton.setText(enter);
        this.editButton.setText(edit);
        this.teacherLabel.setText(teacher);
        this.dateLabel.setText(date);
    }

    public void closePopups() {
        for (Popup popup : popups) {
            if (popup != null) {
                popup.hide();
            }
        }
    }

    public JPanel getMainpanel() {
        return mainpanel;
    }

    public void setupActionListeners(AccountType accountType) {
        PopupFactory popupFactory = new PopupFactory();
        popups = new Popup[1];
        switch (accountType) {
            case admin -> {
                this.editButton.addActionListener(e1 -> {
                    Point point = new Point(this.editButton.getX() - this.getWidth() / 2 + 1, 0);
                    SwingUtilities.convertPointToScreen(point, this);
                    AdminEditClassroomPanel adminEditClassroomPanel = new AdminEditClassroomPanel(language, colors);
                    adminEditClassroomPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
                    adminEditClassroomPanel.cancelbt.addActionListener(e11 -> {
                        popups[0].hide();
                    });
                    adminEditClassroomPanel.deletebt.addActionListener(e12 -> {
                        adminEditClassroomPanel.deleteFunction();
                    });
                    adminEditClassroomPanel.migratebt.addActionListener(e13 ->{
                        adminEditClassroomPanel.migrateFunction();
                    });
                    adminEditClassroomPanel.resetbt.addActionListener(e14 -> {
                        adminEditClassroomPanel.resetFunction();
                    });
                    popups[0] = popupFactory.getPopup(this, adminEditClassroomPanel, point.x, point.y);
                    popups[0].show();
                });
                this.enterButton.addActionListener(e2 -> {
                    this.enterFunction();
                });
            }
            case teacher -> {
                this.editButton.addActionListener(e1 -> {
                    Point point = new Point(this.editButton.getX() - this.getWidth() / 2 + 1, 0);
                    SwingUtilities.convertPointToScreen(point, this);
                    EditClassroomPanel editClassroomPanel = new EditClassroomPanel(language, students, colors);
                    editClassroomPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
                    editClassroomPanel.cancelbt.addActionListener(e11 -> {
                        popups[0].hide();
                    });
                    editClassroomPanel.donebt.addActionListener(e12 -> {
                        editClassroomPanel.doneFunction();
                        popups[0].hide();
                    });
                    popups[0] = popupFactory.getPopup(this, editClassroomPanel, point.x, point.y);
                    popups[0].show();
                });
                this.enterButton.addActionListener(e2 -> {
                    this.enterFunction();
                });
            }
        }
    }

    public void setStudents(String[] students) {
        this.students = students;
    }

    // TODO: Other Group implements this
    private void enterFunction() {

    }
}
