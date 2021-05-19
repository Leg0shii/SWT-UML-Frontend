package de.swt.gui.workspace;

import de.swt.gui.GUI;
import de.swt.util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UserButtonPanel extends GUI {
    private JPanel mainPanel;
    public JButton kickButton;

    public UserButtonPanel(Language language, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        switch (language){
            case GERMAN -> setupGUI("Kicken");
            case ENGLISH -> setupGUI("Kick");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
        setupListeners();
    }

    private void setupGUI(String kick) {
        this.kickButton.setText(kick);
    }

    public void updateGUI() {

    }

    private void setupListeners() {

    }

    private void initForAccountType() {

    }
}
