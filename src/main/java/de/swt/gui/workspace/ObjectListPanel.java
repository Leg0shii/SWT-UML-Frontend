package de.swt.gui.workspace;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.group.Group;
import de.swt.logic.user.User;
import de.swt.util.AccountType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class ObjectListPanel extends GUI {
    private JPanel mainPanel;
    private JButton switchButton;
    private JPanel objectButtons;
    private JLabel headerLabel;
    private JScrollPane objectScrollPanel;
    private String participants;
    private String groups;
    private String group;
    private boolean showGroups;
    private ArrayList<JButton> userButtons;
    private ArrayList<JButton> groupButtons;

    public ObjectListPanel(GUIManager guiManager) {
        super(guiManager);
        this.add(mainPanel);
        this.objectScrollPanel.setViewportView(objectButtons);
        this.objectButtons.setLayout(new GridLayout(0, 1));
        userButtons = new ArrayList<>();
        groupButtons = new ArrayList<>();
        showGroups = false;

        switch (guiManager.getLanguage()) {
            case GERMAN -> setupGUI("Teilnehmer", "Gruppen", "Gruppe");
            case ENGLISH -> setupGUI("Participants", "Groups", "Group");
        }

    }

    private void setupGUI(String participants, String groups, String group) {
        this.switchButton.setText(groups);
        this.headerLabel.setText(participants);
        this.participants = participants;
        this.groups = groups;
        this.group = group;
        this.showGroups = false;
        initForAccountType();
    }

    @Override
    public void updateGUI() {
        ArrayList<Group> relevantGroups = getGuiManager().getRelevantGroups();
        ArrayList<Integer> relevantUserIds = getGuiManager().getClient().getCurrentSession().getUserIds();
        ArrayList<User> relevantUsers = new ArrayList<>();
        for (Integer userId : relevantUserIds) {
            try {
                relevantUsers.add(getGuiManager().getClient().getUserManager().load(userId));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        if (showGroups) {
            this.headerLabel.setText(this.groups);
            this.switchButton.setText(this.participants);
        } else {
            this.headerLabel.setText(this.participants);
            this.switchButton.setText(this.groups);
        }
        if (relevantUsers.size() == userButtons.size() && relevantGroups.size() == groupButtons.size()) {
            return;
        }
        userButtons.clear();
        for (User user : relevantUsers) {
            userButtons.add(setupUserButton(user));
        }
        groupButtons.clear();
        for (Group group : relevantGroups) {
            groupButtons.add(setupGroupButton(group));
        }
        objectButtons.removeAll();
        if (showGroups) {
            for (JButton button : groupButtons) {
                objectButtons.add(button);
            }
        } else {
            for (JButton button : userButtons) {
                objectButtons.add(button);
            }
        }
        initForAccountType();
        revalidate();
        repaint();
    }

    @Override
    public void setupListeners() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new CreateGroupPanel(getGuiManager()));
        switchButton.setComponentPopupMenu(popupMenu);
        popupMenu.setVisible(true);
        popupMenu.setVisible(false);
        this.switchButton.addActionListener(e1 -> {
                    if (getGuiManager().getClient().getCurrentSession().getSessionId() != -1) {
                        if (!groupButtons.isEmpty()) {
                            showGroups = !showGroups;
                            objectButtons.removeAll();
                            if (showGroups) {
                                for (JButton button : groupButtons) {
                                    objectButtons.add(button);
                                }
                            } else {
                                for (JButton button : userButtons) {
                                    objectButtons.add(button);
                                }
                            }
                            revalidate();
                            repaint();
                        } else {
                            switchButton.getComponentPopupMenu().show(switchButton, 0, switchButton.getHeight() / 2 - popupMenu.getHeight() / 2);
                        }
                    }
                }
        );
    }

    public void initForAccountType() {
        if (getGuiManager().getAccountType().equals(AccountType.STUDENT)) {
            this.mainPanel.remove(switchButton);
            for (JButton button : userButtons) {
                if (button.getActionListeners().length > 0) {
                    button.removeActionListener(button.getActionListeners()[0]);
                }
            }
        }
    }

    private JButton setupUserButton(User user) {
        JButton button = new JButton(user.getFullName());
        UserButtonPanel userButtonPanel = new UserButtonPanel(getGuiManager());
        userButtonPanel.setUser(user);
        if (user.getUserId() != getGuiManager().getClient().getUserId()) {
            setupPopupToTheRight(button, userButtonPanel);
        }
        return button;
    }

    private JButton setupGroupButton(Group group) {
        JButton button = new JButton(this.group + " " + group.getGroupId());
        GroupButtonPanel groupButtonPanel = new GroupButtonPanel(getGuiManager());
        groupButtonPanel.setGroup(group);
        setupPopupToTheRight(button, groupButtonPanel);
        return button;
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
        objectButtons = new JPanel();
        objectButtons.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        objectScrollPanel.setViewportView(objectButtons);
        objectButtons.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
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
