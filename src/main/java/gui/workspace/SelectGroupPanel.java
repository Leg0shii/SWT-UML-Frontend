package gui.workspace;

import gui.GUI;
import logic.group.Group;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SelectGroupPanel extends GUI {
    private JPanel mainPanel;
    private JComboBox<Integer> selectGroupComboBox;
    public JButton startTaskButton;
    private JLabel selectGroupLabel;
    private List<Group> groups;

    public SelectGroupPanel(Language language, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        switch (language) {
            case GERMAN -> setupGUI("Wähle deine Gruppe", "Aufgabe beginnen");
            case ENGLISH -> setupGUI("Select your Group", "Start Task");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
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
        Object selectedNumber = this.selectGroupComboBox.getSelectedItem();
        for (Group group : groups){
            if (group.getNumber() == (int) selectedNumber){
                return group;
            }
        }
        return null;
    }
}
