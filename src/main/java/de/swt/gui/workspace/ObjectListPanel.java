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
        this.add(mainPanel);
        this.objectScrollPanel.setViewportView(objectList);
        this.objectList.setLayout(new GridLayout(0, 1));
        this.groupsList = new ArrayList<>();
        this.users = new ArrayList<>();

        switch (guiManager.getLanguage()) {
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
        initForAccountType();
    }

    @Override
    public void updateGUI() {
        if (groupsList.size() != getGuiManager().getRelevantGroups().size() || users.size() != getGuiManager().getClient().getCurrentSession().getUserIds().size()) {
            groupsList.clear();
            users.clear();
            groupsList = getGuiManager().getRelevantGroups();
            ArrayList<Integer> userIds = getGuiManager().getClient().getCurrentSession().getUserIds();
            for (int id : userIds) {
                try {
                    users.add(getGuiManager().getClient().getUserManager().load(id));
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
            if (groupsList.isEmpty()) {
                showGroups = false;
            }
            setupObjectButtons();
            if (showGroups) {
                this.headerLabel.setText(this.groups);
                this.switchButton.setText(this.participants);
            } else {
                this.headerLabel.setText(this.participants);
                this.switchButton.setText(this.groups);
            }
            revalidate();
        }
    }

    @Override
    public void setupListeners() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new CreateGroupPanel(getGuiManager()));
        switchButton.setComponentPopupMenu(popupMenu);
        this.switchButton.addActionListener(e1 -> {
                    if (getGuiManager().getClient().getCurrentSession().getSessionId() != -1) {
                        if (!groupsList.isEmpty()) {
                            showGroups = !showGroups;
                            updateGUI();
                        } else {
                            switchButton.getComponentPopupMenu().show(switchButton, 0, 0);
                        }
                    }
                }
        );
    }

    public void initForAccountType() {
        if (getGuiManager().getAccountType().equals(AccountType.STUDENT)) {
            this.mainPanel.remove(switchButton);
        }
    }

    private void setupObjectButtons() {
        this.objectList.removeAll();
        if (showGroups) {
            for (Group group : groupsList) {
                JButton objectButton = new JButton();
                objectButton.setText(this.group + " " + group.getGroupId());
                setupListenerForObjectButton(objectButton, group);
                this.objectList.add(objectButton);
            }
        } else {
            for (User user : users) {
                JButton objectButton = new JButton();
                objectButton.setText(user.getFullName());
                if (getGuiManager().getAccountType() != AccountType.STUDENT || user.getUserId() == getGuiManager().getClient().getUserId()) {
                    setupListenerForObjectButton(objectButton, user);
                }
                this.objectList.add(objectButton);
            }
        }
    }

    private void setupListenerForObjectButton(JButton objectButton, Object object) {
        if (showGroups) {
            GroupButtonPanel groupButtonPanel = new GroupButtonPanel(getGuiManager());
            groupButtonPanel.setGroup((Group) object);
            setupPopupToTheRight(objectButton, groupButtonPanel);
        } else {
            UserButtonPanel userButtonPanel = new UserButtonPanel(getGuiManager());
            userButtonPanel.setUser((User) object);
            setupPopupToTheRight(objectButton, userButtonPanel);
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
