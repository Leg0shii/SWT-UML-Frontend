package de.swt.gui.workspace;

import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.Group;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SelectGroupPanel extends GUI {
    private JPanel mainPanel;
    private JComboBox<Integer> selectGroupComboBox;
    public JButton startTaskButton;
    private JLabel selectGroupLabel;
    private List<Group> groups;

    public SelectGroupPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Wähle deine Gruppe", "Aufgabe beginnen");
            case ENGLISH -> setupGUI("Select your Group", "Start Task");
        }

        setupListeners();
    }

    private void setupGUI(String selectGroup, String beginTask) {
        this.selectGroupLabel.setText(selectGroup);
        this.startTaskButton.setText(beginTask);
    }

    public void updateGUI(List<Group> groups) {
        this.groups = groups;
        this.selectGroupComboBox.removeAllItems();
        for (Group group : groups){
            this.selectGroupComboBox.addItem(group.getNumber());
        }
    }

    private void setupListeners() {

    }

    private void initForAccountType() {
        
    }

    public Group getSelectedGroup() {
        String selectedNumber = String.valueOf(this.selectGroupComboBox.getSelectedItem());
        for (Group group : groups){
            if (group.getNumber() == Integer.parseInt(selectedNumber)){
                return group;
            }
        }
        return null;
    }
}
