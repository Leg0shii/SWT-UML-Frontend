package de.swt.gui.workspace;

import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.Group;
import de.swt.logic.User;
import de.swt.util.AccountType;
import de.swt.util.Language;
import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    private List<Group> groupsList;
    private List<User> users;

    public ObjectListPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.objectScrollPanel.setViewportView(objectList);
        this.addStudentButton = new JButton();
        this.objectList.setLayout(new GridLayout(0, 1));

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Teilnehmer", "Gruppen", "Gruppe", "Schüler hinzufügen");
            case ENGLISH -> setupGUI("Participants", "Groups", "Group", "Add Student");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), guiManager.colorScheme, 1);
        setupListeners();
    }

    private void setupGUI(String participants, String groups, String group, String addStudents) {
        this.participants = participants;
        this.groups = groups;
        this.group = group;
        this.showGroups = false;
        this.addStudentButton.setText(addStudents);
        initPopups(2);
    }

    public void updateGUI(List<Group> groups, List<User> users) {
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
        initForAccountType();
        colorComponents(this.getAllComponents(this, new ArrayList<>()), guiManager.colorScheme, 1);
        this.revalidate();
    }

    private void setupListeners() {
        this.switchButton.addActionListener(e1 -> {
                    if (!groupsList.isEmpty()) {
                        this.showGroups = !this.showGroups;
                        closeAllPopups();
                        this.updateGUI(this.groupsList, this.users);
                    } else {
                        if (popupCounter.get(1) % 2 == 0) {
                            CreateGroupPanel createGroupPanel = new CreateGroupPanel(guiManager);
                            Point point = new Point(switchButton.getX() + switchButton.getWidth(), switchButton.getY());
                            SwingUtilities.convertPointToScreen(point, objectList);
                            popups.get(1).hide();
                            popups.set(1, factory.getPopup(this, createGroupPanel, point.x, point.y));
                            popups.get(1).show();
                        } else {
                            popups.get(1).hide();
                        }
                        incrementPopupCounter(1);
                    }
                }
        );
        this.addStudentButton.addActionListener(e2 -> {
            if (popupCounter.get(0) % 2 == 0) {
                AddUserPanel addUserPanel = new AddUserPanel(guiManager);
                addUserPanel.addButton.addActionListener(e21 -> {
                    addUserFunction();
                });
                Point point = new Point(addStudentButton.getX() + addStudentButton.getWidth(), addStudentButton.getY());
                SwingUtilities.convertPointToScreen(point, objectList);
                popups.get(0).hide();
                popups.set(0, factory.getPopup(this, addUserPanel, point.x, point.y));
                popups.get(0).show();
            } else {
                popups.get(0).hide();
            }
            incrementPopupCounter(0);
        });
    }

    public void initForAccountType() {
        if (guiManager.accountType == AccountType.STUDENT) {
            this.objectList.remove(addStudentButton);
            this.mainPanel.remove(switchButton);
        }
    }

    private void setupObjectButtons(List<Group> groups, List<User> users) {
        this.objectList.removeAll();
        System.out.println(popupCounter);
        popups.subList(2, popups.size()).clear();
        popupCounter.subList(2, popupCounter.size()).clear();
        System.out.println(popupCounter);
        int counter = 2;
        if (showGroups) {
            for (Group group : groups) {
                JButton objectButton = new JButton();
                objectButton.setText(this.group + " " + group.getNumber());
                setupListenerForObjectButton(objectButton, group, counter);
                counter += 1;
                this.objectList.add(objectButton);
            }
        } else {
            for (User user : users) {
                JButton objectButton = new JButton();
                objectButton.setText(user.getFullName());
                if (guiManager.accountType != AccountType.STUDENT) {
                    setupListenerForObjectButton(objectButton, user, counter);
                    counter += 1;
                }
                this.objectList.add(objectButton);
            }
        }
    }

    private void setupListenerForObjectButton(JButton objectButton, Object object, int n) {
        initPopups(1);
        if (showGroups) {
            objectButton.addActionListener(e -> {
                if (popupCounter.get(n) % 2 == 0) {
                    GroupButtonPanel groupButtonPanel = new GroupButtonPanel(guiManager);
                    groupButtonPanel.terminateButton.addActionListener(e1 -> {
                        terminateGroup((Group) object);
                        objectList.remove(objectButton);
                        popups.get(n).hide();
                    });
                    groupButtonPanel.watchButton.addActionListener(e2 -> watchGroup((Group) object));
                    Point point = new Point(objectButton.getX() + objectButton.getWidth(), objectButton.getY());
                    SwingUtilities.convertPointToScreen(point, objectList);
                    popups.get(n).hide();
                    popups.set(n, factory.getPopup(guiManager, groupButtonPanel, point.x, point.y));
                    popups.get(n).show();
                } else {
                    popups.get(n).hide();
                }
                incrementPopupCounter(n);
                revalidate();
            });
        } else {
            objectButton.addActionListener(e -> {
                if (popupCounter.get(n) % 2 == 0) {
                    UserButtonPanel userButtonPanel = new UserButtonPanel(guiManager);
                    userButtonPanel.kickButton.addActionListener(e1 -> {
                        ObjectListPanel.this.removeStudent((User) object);
                        ObjectListPanel.this.objectList.remove(objectButton);
                        popups.get(n).hide();
                    });
                    Point point = new Point(objectButton.getX() + objectButton.getWidth(), objectButton.getY());
                    SwingUtilities.convertPointToScreen(point, objectList);
                    popups.get(n).hide();
                    popups.set(n, factory.getPopup(guiManager, userButtonPanel, point.x, point.y));
                    popups.get(n).show();
                } else {
                    popups.get(n).hide();
                }
                incrementPopupCounter(n);
                revalidate();
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
