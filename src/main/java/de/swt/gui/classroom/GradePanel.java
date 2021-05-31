package de.swt.gui.classroom;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.course.Course;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.util.AccountType;
import de.swt.util.NextDate;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class GradePanel extends GUI {
    private Course course;
    private JPanel mainPanel;
    public JButton enterButton;
    public JButton editButton;
    private JLabel dateLabel;
    private JLabel teacherLabel;
    private JLabel nextDateLabel;
    private JLabel thisTeacherLabel;
    private JLabel gradeHeaderLabel;
    public int grade;
    private final EditClassroomPanel editClassroomPanel;
    private final AdminEditClassroomPanel adminEditClassroomPanel;

    public GradePanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        switch (guiManager.language) {
            case GERMAN -> setupGUI("Beitreten", "Bearbeiten", "Lehrer", "Termin", "Klasse");
            case ENGLISH -> setupGUI("Enter", "Edit", "Teacher", "Date", "Grade");
        }
        setupActionListeners(guiManager.accountType);

        this.editClassroomPanel = new EditClassroomPanel(guiManager);
        this.adminEditClassroomPanel = new AdminEditClassroomPanel(guiManager);
    }

    private void setupGUI(String enter, String edit, String teacher, String date, String grade) {
        this.enterButton.setText(enter);
        this.editButton.setText(edit);
        this.teacherLabel.setText(teacher);
        this.dateLabel.setText(date);
        this.gradeHeaderLabel.setText(grade + " ");
        initPopups(1);
    }

    public void updateGUI(Course course) {
        this.course = course;
        this.gradeHeaderLabel.setText(gradeHeaderLabel.getText().split(" ")[0] + " " + course.getGrade() + " " + course.getName());
        this.grade = course.getGrade();
        this.thisTeacherLabel.setText(course.getTeacher().getFullName());
        String date = getNextSessionDate();
        this.nextDateLabel.setText(date);
        this.editClassroomPanel.setCourse(course);
        this.adminEditClassroomPanel.setCourse(course);
    }

    private void initForAccountType() {
        removeAllListeners();
        setupActionListeners(guiManager.accountType);
    }

    private void removeAllListeners() {
        editButton.removeAll();
        enterButton.removeAll();
    }

    public void setupActionListeners(AccountType accountType) {
        switch (accountType) {
            case ADMIN -> {
                this.editButton.addActionListener(e1 -> {
                    Point point = new Point(this.editButton.getX() - 2, 0);
                    SwingUtilities.convertPointToScreen(point, this);
                    adminEditClassroomPanel.setPreferredSize(new Dimension(this.getWidth() + 1, this.getHeight() + 1));
                    adminEditClassroomPanel.cancelButton.addActionListener(e11 -> popups.get(0).hide());
                    adminEditClassroomPanel.deleteButton.addActionListener(e12 -> {
                        adminEditClassroomPanel.deleteFunction();
                        popups.get(0).hide();
                    });
                    adminEditClassroomPanel.migrateButton.addActionListener(e13 -> {
                        adminEditClassroomPanel.migrateFunction();
                        popups.get(0).hide();
                    });
                    adminEditClassroomPanel.resetButton.addActionListener(e14 -> {
                        adminEditClassroomPanel.resetFunction();
                        popups.get(0).hide();
                    });
                    popups.set(0, factory.getPopup(guiManager, adminEditClassroomPanel, point.x, point.y));
                    popups.get(0).show();
                });
                this.enterButton.addActionListener(e2 -> this.enterFunction());
            }
            case TEACHER -> {
                this.editButton.addActionListener(e1 -> {
                    Point point = new Point(this.editButton.getX() - 2, 0);
                    SwingUtilities.convertPointToScreen(point, this);
                    editClassroomPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
                    editClassroomPanel.cancelButton.addActionListener(e11 -> popups.get(0).hide());
                    editClassroomPanel.doneButton.addActionListener(e12 -> {
                        editClassroomPanel.doneFunction();
                        popups.get(0).hide();
                    });
                    popups.set(0, factory.getPopup(guiManager, editClassroomPanel, point.x, point.y));
                    popups.get(0).show();
                });
                this.enterButton.addActionListener(e2 -> this.enterFunction());
            } case STUDENT -> {
                this.enterButton.addActionListener(e3 -> this.enterFunction());
            }
        }
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public String getNextDate() {
        return nextDateLabel.getText();
    }

    public User getThisTeacher() {
        return course.getTeacher();
    }

    public String getGradePlusGradeName() {
        return course.getGrade() + " " + course.getName();
    }

    public EditClassroomPanel getEditClassroomPanel() {
        return editClassroomPanel;
    }

    public AdminEditClassroomPanel getAdminEditClassroomPanel() {
        return adminEditClassroomPanel;
    }

    private String getNextSessionDate() {
        SimpleDateFormat format = new SimpleDateFormat("E, H:m");
        for (Date date : course.getDates()) {
            String sDate = format.format(date);
            Date now = new Date();
            Date sessionDate = new Date();
            try {
                sessionDate = format.parse(sDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int returnVal = now.compareTo(sessionDate);
            if (returnVal > 0) {
                return sDate;
            }
        }
        return "ERROR";
    }

    // TODO: Other Group implements this
    private void enterFunction() {
        try {
            if (guiManager.accountType.equals(AccountType.TEACHER) || guiManager.getClient().userManager.loadUser(guiManager.getClient().userid).getCourse().contains(course.getId())) {
                Session session = guiManager.getClient().sessionManager.getSessionFromTeacherId(course.getTeacherID());
                if (session == null) {
                    if (guiManager.accountType.equals(AccountType.TEACHER)) {
                        Session newSession = new Session();
                        newSession.getMaster().add(guiManager.getClient().userid);
                        newSession.setRemainingTime(System.currentTimeMillis() + (120*60000L));
                        newSession.getParticipants().add(guiManager.getClient().userid);
                        newSession = guiManager.getClient().server.sendSession(newSession, -1, true);
                        guiManager.switchToWorkspaceGUI();
                        guiManager.currentSession = newSession;
                    } else {
                        enterButton.setBackground(Color.RED);
                        switch (guiManager.language) {
                            case ENGLISH:
                                enterButton.setText("Session not yet open");
                            case GERMAN:
                                enterButton.setText("Session nicht offen");
                        }
                    }
                } else {
                    session.getParticipants().add(guiManager.getClient().userid);
                    session = guiManager.getClient().server.sendSession(session, -1, true);
                    guiManager.switchToWorkspaceGUI();
                    guiManager.currentSession = session;
                }
            } else {
                guiManager.getClient().server.sendRequest(guiManager.getClient().userid, course.getTeacherID());
            }

        } catch (SQLException | RemoteException throwables) {
            throwables.printStackTrace();
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        gradeHeaderLabel = new JLabel();
        gradeHeaderLabel.setHorizontalAlignment(0);
        gradeHeaderLabel.setText("Klasse 10 b");
        mainPanel.add(gradeHeaderLabel, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        thisTeacherLabel = new JLabel();
        thisTeacherLabel.setHorizontalAlignment(2);
        thisTeacherLabel.setText("Herr Müller");
        mainPanel.add(thisTeacherLabel, new GridConstraints(1, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        teacherLabel = new JLabel();
        teacherLabel.setHorizontalAlignment(4);
        teacherLabel.setText("Lehrer:");
        mainPanel.add(teacherLabel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dateLabel = new JLabel();
        dateLabel.setHorizontalAlignment(4);
        dateLabel.setText("Termin:");
        mainPanel.add(dateLabel, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nextDateLabel = new JLabel();
        nextDateLabel.setHorizontalAlignment(2);
        nextDateLabel.setText("Mo. 0915 - 1045");
        mainPanel.add(nextDateLabel, new GridConstraints(2, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enterButton = new JButton();
        enterButton.setText("Beitreten");
        mainPanel.add(enterButton, new GridConstraints(3, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editButton = new JButton();
        editButton.setText("Bearbeiten");
        mainPanel.add(editButton, new GridConstraints(4, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
