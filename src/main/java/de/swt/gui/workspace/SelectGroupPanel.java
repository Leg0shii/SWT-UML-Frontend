package de.swt.gui.workspace;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.group.Group;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class SelectGroupPanel extends GUI {
    private JPanel mainPanel;
    private JComboBox<Integer> selectGroupComboBox;
    public JButton startTaskButton;
    private JLabel selectGroupLabel;

    public SelectGroupPanel(GUIManager guiManager) {
        super(guiManager);
        this.add(mainPanel);

        switch (guiManager.getLanguage()) {
            case GERMAN -> setupGUI("Wähle deine Gruppe", "Aufgabe beginnen");
            case ENGLISH -> setupGUI("Select your Group", "Start Task");
        }

        setupListeners();
    }

    private void setupGUI(String selectGroup, String beginTask) {
        this.selectGroupLabel.setText(selectGroup);
        this.startTaskButton.setText(beginTask);
    }

    @Override
    public void updateGUI() {
        this.selectGroupComboBox.removeAllItems();
        for (int groupId : getGuiManager().getClient().getCurrentSession().getGroupIds()) {
            this.selectGroupComboBox.addItem(groupId);
        }
    }

    @Override
    public void setupListeners() {
        startTaskButton.addActionListener(e -> startTaskFunction());
    }

    private void startTaskFunction() {
        int selectedNumber = Integer.parseInt(String.valueOf(this.selectGroupComboBox.getSelectedItem()));
        try {
            Group group = getGuiManager().getClient().getGroupManager().load(selectedNumber);
            group.getUserIds().add(getGuiManager().getClient().getUserId());
            getGuiManager().getClient().getServer().updateGroup(group);
            startTaskButton.setBackground(UIManager.getColor("JButton"));
        } catch (SQLException | RemoteException exception) {
            startTaskButton.setBackground(Color.RED);
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
        mainPanel.setLayout(new GridLayoutManager(2, 2, new Insets(5, 5, 5, 5), -1, -1));
        selectGroupLabel = new JLabel();
        selectGroupLabel.setText("Label");
        mainPanel.add(selectGroupLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectGroupComboBox = new JComboBox();
        mainPanel.add(selectGroupComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startTaskButton = new JButton();
        startTaskButton.setText("Button");
        mainPanel.add(startTaskButton, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectGroupLabel.setLabelFor(selectGroupComboBox);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
