package gui.workspace;

import gui.GUI;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GroupButtonPanel extends GUI {
    private JPanel mainPanel;
    public JButton watchButton;
    public JButton terminateButton;

    public GroupButtonPanel(Language language, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        switch (language){
            case GERMAN -> setupGUI("Terminieren", "Zuschauen");
            case ENGLISH -> setupGUI("Terminate", "Watch");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
        setupListeners();
    }

    private void setupGUI(String terminate, String watch) {
        this.terminateButton.setText(terminate);
        this.watchButton.setText(watch);
    }

    public void updateGUI() {

    }

    private void setupListeners() {

    }

    private void initForAccountType() {

    }
}
