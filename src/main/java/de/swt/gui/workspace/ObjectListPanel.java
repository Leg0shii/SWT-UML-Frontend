package de.swt.gui.workspace;

import de.swt.gui.GUI;
import de.swt.logic.Group;
import de.swt.logic.User;
import de.swt.util.AccountType;
import de.swt.util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ObjectListPanel extends GUI {
    private JPanel mainPanel;
    private JButton switchButton;
    private JPanel objectList;
    private JLabel headerLabel;
    private JScrollPane objectScrollPanel;
    private String participants;
    private String groups;
    private String group;
    private boolean showGroups;
    private JButton addStudentButton;
    private Language language;
    private Color[] colors;
    private List<Group> groupsList;
    private List<User> users;
    private int[] popupCounter;
    private Popup[] popups;
    private AccountType accountType;

    public ObjectListPanel(Language language, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.objectScrollPanel.setViewportView(objectList);
        this.addStudentButton = new JButton();
        this.objectList.setLayout(new GridLayout(0,1));

        switch (language) {
            case GERMAN -> setupGUI("Teilnehmer", "Gruppen", "Gruppe", "Schüler hinzufügen");
            case ENGLISH -> setupGUI("Participants", "Groups", "Group", "Add Student");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
        setupListeners();

        this.language = language;
        this.colors = colors;
        this.popupCounter = new int[2];
        this.popups = new Popup[2];
    }

    private void setupGUI(String participants, String groups, String group, String addStudents) {
        this.participants = participants;
        this.groups = groups;
        this.group = group;
        this.showGroups = false;
        this.addStudentButton.setText(addStudents);
    }

    public void updateGUI(List<Group> groups, List<User> users, AccountType accountType) {
        this.accountType = accountType;
        this.groupsList = groups;
        this.users = users;
        setupObjectButtons(groups, users);
        if (showGroups) {
            this.headerLabel.setText(this.groups);
            this.switchButton.setText(this.participants);
            this.objectList.remove(this.addStudentButton);
        } else {
            this.headerLabel.setText(participants);
            this.switchButton.setText(this.groups);
            this.objectList.add(this.addStudentButton);
        }
        initForAccountType(accountType);
        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
        this.revalidate();
    }

    private void setupListeners() {
        this.switchButton.addActionListener(e1 -> {
                    PopupFactory popupFactory = new PopupFactory();
                    if (!groupsList.isEmpty()) {
                        this.showGroups = !this.showGroups;
                        this.updateGUI(this.groupsList, this.users, accountType);
                    } else {
                        if (popupCounter[1] % 2 == 0) {
                            CreateGroupPanel createGroupPanel = new CreateGroupPanel(language, colors);
                            Point point = new Point(switchButton.getX() + switchButton.getWidth(), switchButton.getY());
                            SwingUtilities.convertPointToScreen(point, objectList);
                            popups[1] = popupFactory.getPopup(this, createGroupPanel, point.x, point.y);
                            popups[1].show();
                        } else {
                            popups[1].hide();
                        }
                        popupCounter[1]++;
                    }
                }
        );
        this.addStudentButton.addActionListener(e2 -> {
            PopupFactory popupFactory = new PopupFactory();
            if (popupCounter[0] % 2 == 0) {
                AddUserPanel addUserPanel = new AddUserPanel(language, colors);
                addUserPanel.addButton.addActionListener(e21 -> {
                    addUserFunction();
                });
                Point point = new Point(addStudentButton.getX() + addStudentButton.getWidth(), addStudentButton.getY());
                SwingUtilities.convertPointToScreen(point, objectList);
                popups[0] = popupFactory.getPopup(this, addUserPanel, point.x, point.y);
                popups[0].show();
            } else {
                popups[0].hide();
            }
            popupCounter[0]++;
        });
    }

    public void initForAccountType(AccountType accountType) {
        if (accountType == AccountType.STUDENT) {
            this.objectList.remove(addStudentButton);
            this.mainPanel.remove(switchButton);
        }
    }

    private void setupObjectButtons(List<Group> groups, List<User> users) {
        this.objectList.removeAll();
        if (showGroups) {
            for (Group group : groups) {
                JButton objectButton = new JButton();
                objectButton.setText(this.group + " " + group.getNumber());
                setupListenerForObjectButton(objectButton, group);
                this.objectList.add(objectButton);
            }
        } else {
            for (User user : users) {
                JButton objectButton = new JButton();
                objectButton.setText(user.getFullName());
                if (accountType != AccountType.STUDENT) {
                    setupListenerForObjectButton(objectButton, user);
                }
                this.objectList.add(objectButton);
            }
        }
    }

    // TODO: Add Listeners
    private void setupListenerForObjectButton(JButton objectButton, Object object) {
        PopupFactory popupFactory = new PopupFactory();
        Popup[] popups = new Popup[2];
        int[] popupCounter = new int[2];
        if (showGroups) {
            objectButton.addActionListener(e1 -> {
                if (popupCounter[0] % 2 == 0) {
                    GroupButtonPanel groupButtonPanel = new GroupButtonPanel(language, colors);
                    groupButtonPanel.terminateButton.addActionListener(e11 -> {
                        terminateGroup((Group) object);
                        this.objectList.remove(objectButton);
                        popups[0].hide();
                    });
                    groupButtonPanel.watchButton.addActionListener(e12 -> {
                        watchGroup((Group) object);
                    });
                    Point point = new Point(objectButton.getX() + objectButton.getWidth(), objectButton.getY());
                    SwingUtilities.convertPointToScreen(point, objectList);
                    popups[0] = popupFactory.getPopup(objectButton, groupButtonPanel, point.x, point.y);
                    popups[0].show();
                } else {
                    popups[0].hide();
                }
                popupCounter[0]++;
                this.revalidate();
            });
        } else {
            objectButton.addActionListener(e2 -> {
                if (popupCounter[1] % 2 == 0) {
                    UserButtonPanel userButtonPanel = new UserButtonPanel(language, colors);
                    userButtonPanel.kickButton.addActionListener(e21 -> {
                        removeStudent((User) object);
                        this.objectList.remove(objectButton);
                        popups[1].hide();
                    });
                    Point point = new Point(objectButton.getX() + objectButton.getWidth(), objectButton.getY());
                    SwingUtilities.convertPointToScreen(point, objectList);
                    popups[1] = popupFactory.getPopup(objectButton, userButtonPanel, point.x, point.y);
                    popups[1].show();
                } else {
                    popups[1].hide();
                }
                popupCounter[1]++;
                this.revalidate();
            });
        }

    }

    // TODO: Implemented by other Group
    private void removeStudent(User user) {

    }

    // TODO: Implemented by other Group
    private void terminateGroup(Group group) {

    }

    // TODO: Implemented by other Group
    private void watchGroup(Group group) {

    }

    // TODO: Implemented by other Group
    private void addUserFunction() {

    }

}
