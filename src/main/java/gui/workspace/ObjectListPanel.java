package gui.workspace;

import gui.GUI;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ObjectListPanel extends GUI {
    private JPanel mainPanel;
    private JButton switchButton;
    private JList objectList;
    private JLabel headerLabel;
    private JScrollPane objectScrollPanel;

    public ObjectListPanel(Language language, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.objectScrollPanel.setViewportView(objectList);

        switch (language) {
            case GERMAN -> setupGUI();
            case ENGLISH -> setupGUI();
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
        setupListeners();
    }

    private void setupGUI() {

    }

    public void updateGUI() {

    }

    private void setupListeners() {

    }

    private void initForAccountType() {

    }
}
