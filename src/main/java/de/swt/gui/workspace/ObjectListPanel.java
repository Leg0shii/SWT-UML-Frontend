package de.swt.gui.workspace;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.group.Group;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.util.AccountType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.sql.SQLException;
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
    private List<Group> groupsList;
    private List<User> users;

    public ObjectListPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.objectScrollPanel.setViewportView(objectList);
        this.objectList.setLayout(new GridLayout(0, 1));
        this.groupsList = new ArrayList<>();
        this.users = new ArrayList<>();

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Teilnehmer", "Gruppen", "Gruppe");
            case ENGLISH -> setupGUI("Participants", "Groups", "Group");
        }

        setupListeners();
    }

    private void setupGUI(String participants, String groups, String group) {
        this.participants = participants;
        this.groups = groups;
        this.group = group;
        this.showGroups = false;
        initPopups(2);
    }

    public void updateGUI(List<Group> groups, List<User> users) {
        this.groupsList = groups;
        this.users = users;
        if (groupsList.isEmpty()) {
            showGroups = false;
        }
        setupObjectButtons(groups, users);
        if (showGroups) {
            this.headerLabel.setText(this.groups);
            this.switchButton.setText(this.participants);
        } else {
            this.headerLabel.setText(participants);
            this.switchButton.setText(this.groups);
        }
        initForAccountType();
        this.revalidate();
    }

    public void updateGUI() {
        if (groupsList.isEmpty()) {
            showGroups = false;
        }
        setupObjectButtons(groupsList, users);
        if (showGroups) {
            this.headerLabel.setText(this.groups);
            this.switchButton.setText(this.participants);
        } else {
            this.headerLabel.setText(participants);
            this.switchButton.setText(this.groups);
        }
        initForAccountType();
        this.revalidate();
    }

    private void setupListeners() {
        this.switchButton.addActionListener(e1 -> {
                    if (!groupsList.isEmpty()) {
                        closeAllPopups();
                        this.showGroups = !this.showGroups;
                        this.updateGUI(this.groupsList, this.users);
                    } else {

                        if (popupCounter.get(1) % 2 == 0) {
                            PopupFactory factory1 = new PopupFactory();
                            CreateGroupPanel createGroupPanel = new CreateGroupPanel(guiManager);
                            createGroupPanel.cancelButton.addActionListener(e -> {
                                popups.get(1).hide();
                                incrementPopupCounter(1);
                            });
                            createGroupPanel.createButton.addActionListener(e -> {
                                popups.get(1).hide();
                                incrementPopupCounter(1);
                            });
                            Point point = new Point(switchButton.getX() + switchButton.getWidth(), switchButton.getY());
                            SwingUtilities.convertPointToScreen(point, guiManager);
                            popups.get(1).hide();
                            popups.set(1, factory1.getPopup(guiManager, createGroupPanel.mainPanel, point.x, point.y));
                            popups.get(1).show();
                        } else {
                            popups.get(1).hide();
                        }
                        incrementPopupCounter(1);
                    }
                }
        );
    }

    public void initForAccountType() {
        if (guiManager.accountType == AccountType.STUDENT) {
            this.mainPanel.remove(switchButton);
        }
    }

    private void setupObjectButtons(List<Group> groups, List<User> users) {
        this.objectList.removeAll();
        popups.subList(2, popups.size()).clear();
        popupCounter.subList(2, popupCounter.size()).clear();
        int counter = 2;
        if (showGroups) {
            for (Group group : groups) {
                JButton objectButton = new JButton();
                objectButton.setText(this.group + " " + group.getId());
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

    private void removeStudent(User user) {
        Session session = guiManager.currentSession;
        session.getParticipants().remove((Integer) user.getId());
        try {
            guiManager.getClient().server.sendSession(session, session.getId(), true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void terminateGroup(Group group) {
        Session session = guiManager.currentSession;
        session.getGroups().remove((Integer) group.getId());
        try {
            guiManager.getClient().server.sendSession(session, session.getId(), true);
            guiManager.getClient().server.deleteGroup(group.getId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void watchGroup(Group group) {
        try {
            guiManager.currentGroup = guiManager.getClient().groupManager.loadGroup(group.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        guiManager.currentGroup.getParticipants().add(guiManager.getClient().userid);
        try {
            guiManager.getClient().server.sendGroup(guiManager.currentGroup, guiManager.currentGroup.getId(), true);
        } catch (RemoteException e) {
            e.printStackTrace();
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
        mainPanel.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        headerLabel = new JLabel();
        headerLabel.setHorizontalAlignment(0);
        headerLabel.setText("Label");
        mainPanel.add(headerLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        objectScrollPanel = new JScrollPane();
        objectScrollPanel.setHorizontalScrollBarPolicy(31);
        objectScrollPanel.setVerticalScrollBarPolicy(20);
        mainPanel.add(objectScrollPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        objectScrollPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        objectList = new JPanel();
        objectList.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        objectScrollPanel.setViewportView(objectList);
        objectList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        switchButton = new JButton();
        switchButton.setText("Button");
        mainPanel.add(switchButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
