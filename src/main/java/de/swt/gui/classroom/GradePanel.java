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
import lombok.Getter;
import lombok.Setter;

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

@Setter
@Getter
public class GradePanel extends GUI {
    private Course course;
    private JPanel mainPanel;
    private JButton enterButton;
    private JButton editButton;
    private JLabel dateLabel;
    private JLabel teacherLabel;
    private JLabel nextDateLabel;
    private JLabel thisTeacherLabel;
    private JLabel gradeHeaderLabel;
    private final EditClassroomPanel editClassroomPanel;
    private final AdminEditClassroomPanel adminEditClassroomPanel;

    public GradePanel(GUIManager guiManager) {
        super(guiManager);
        this.add(mainPanel);
        switch (guiManager.getLanguage()) {
            case GERMAN -> setupGUI("Beitreten", "Bearbeiten", "Lehrer", "Termin", "Klasse");
            case ENGLISH -> setupGUI("Enter", "Edit", "Teacher", "Date", "Grade");
        }

        this.editClassroomPanel = new EditClassroomPanel(guiManager);
        this.adminEditClassroomPanel = new AdminEditClassroomPanel(guiManager);

        setupListeners();
    }

    @Override
    public void updateGUI() {
        this.gradeHeaderLabel.setText(gradeHeaderLabel.getText().split(" ")[0] + " " + course.getGrade() + " " + course.getGradeName());
        try {
            User teacher = getGuiManager().getClient().getUserManager().load(course.getTeacherId());
            this.thisTeacherLabel.setText(teacher.getFullName());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        String date = getNextSessionDate();
        this.nextDateLabel.setText(date);
        this.editClassroomPanel.setCourse(course);
        this.adminEditClassroomPanel.setCourse(course);
    }

    @Override
    public void setupListeners() {
        switch (getGuiManager().getAccountType()) {
            case ADMIN -> {
                setupMatchingPopup(editButton, adminEditClassroomPanel, this);
                enterButton.addActionListener(e -> enterFunction());
            }
            case STUDENT -> enterButton.addActionListener(e -> enterFunction());
            case TEACHER -> {
                setupMatchingPopup(editButton, editClassroomPanel, this);
                enterButton.addActionListener(e -> enterFunction());
            }
        }
    }

    private void setupGUI(String enter, String edit, String teacher, String date, String grade) {
        this.enterButton.setText(enter);
        this.editButton.setText(edit);
        this.teacherLabel.setText(teacher);
        this.dateLabel.setText(date);
        this.gradeHeaderLabel.setText(grade + " ");
    }

    private String getNextSessionDate() {
        SimpleDateFormat format = new SimpleDateFormat("E, H:m");
        String minDate = "";
        int minVal = Integer.MAX_VALUE;
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
            if (returnVal > 0 && returnVal < minVal) {
                minDate = sDate;
                minVal = returnVal;
            }
        }
        return minDate;
    }

    private void enterFunction() {
        if (getGuiManager().getAccountType().equals(AccountType.TEACHER)) {
            Session session = getGuiManager().getClient().getSessionManager().getSessionFromTeacherId(getGuiManager().getClient().getUserId());
            if (session != null) {
                session.getUserIds().add(getGuiManager().getClient().getUserId());
                try {
                    getGuiManager().getClient().getServer().updateSession(session);
                    getGuiManager().switchToWorkspaceGUI();
                    enterButton.setBackground(UIManager.getColor("JButton"));
                } catch (RemoteException e) {
                    switch (getGuiManager().getLanguage()) {
                        case GERMAN -> enterButton.setText("Beitreten schlug fehl");
                        case ENGLISH -> enterButton.setText("Entering failed");
                    }
                    enterButton.setBackground(Color.RED);
                }
            } else if (course.getTeacherId() == getGuiManager().getClient().getUserId()) {
                session = new Session();
                session.getMasterIds().add(getGuiManager().getClient().getUserId());
                session.setRemainingTime(System.currentTimeMillis() + (120 * 60000));
                session.getUserIds().add(getGuiManager().getClient().getUserId());
                try {
                    getGuiManager().getClient().getServer().updateSession(session);
                    getGuiManager().switchToWorkspaceGUI();
                    enterButton.setBackground(UIManager.getColor("JButton"));
                } catch (RemoteException e) {
                    switch (getGuiManager().getLanguage()) {
                        case GERMAN -> enterButton.setText("Session eröffnen schlug fehl");
                        case ENGLISH -> enterButton.setText("Creating Session failed");
                    }
                    enterButton.setBackground(Color.RED);
                }
            } else {
                switch (getGuiManager().getLanguage()) {
                    case GERMAN -> enterButton.setText("Keine Berechtigung");
                    case ENGLISH -> enterButton.setText("No Rights");
                }
                enterButton.setBackground(Color.RED);
            }
        } else {
            Session session = getGuiManager().getClient().getSessionManager().getSessionFromTeacherId(getGuiManager().getClient().getUserId());
            if (session != null) {
                if (course.getUserIds().contains(getGuiManager().getClient().getUserId())) {
                    session.getUserIds().add(getGuiManager().getClient().getUserId());
                    try {
                        getGuiManager().getClient().getServer().updateSession(session);
                        getGuiManager().switchToWorkspaceGUI();
                        enterButton.setBackground(UIManager.getColor("JButton"));
                    } catch (RemoteException e) {
                        switch (getGuiManager().getLanguage()) {
                            case GERMAN -> enterButton.setText("Beitreten schlug fehl");
                            case ENGLISH -> enterButton.setText("Entering failed");
                        }
                        enterButton.setBackground(Color.RED);
                    }
                } else {
                    try {
                        getGuiManager().getClient().getServer().sendRequest(getGuiManager().getClient().getUserId(), session.getMasterIds().get(0));
                        enterButton.setBackground(UIManager.getColor("JButton"));
                    } catch (RemoteException e) {
                        switch (getGuiManager().getLanguage()) {
                            case GERMAN -> enterButton.setText("Anfrage schicken fehlgeschlagen");
                            case ENGLISH -> enterButton.setText("Sending request failed");
                        }
                        enterButton.setBackground(Color.RED);
                    }
                }
            } else {
                switch (getGuiManager().getLanguage()) {
                    case GERMAN -> enterButton.setText("Session nicht offen");
                    case ENGLISH -> enterButton.setText("Session not open");
                }
                enterButton.setBackground(Color.RED);
            }
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
