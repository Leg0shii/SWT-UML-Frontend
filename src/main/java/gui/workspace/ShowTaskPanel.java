package gui.workspace;

import gui.GUI;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ShowTaskPanel extends GUI {
    private JPanel mainPanel;
    private JLabel taskLabel;

    public ShowTaskPanel(Language language, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
        setupListeners();
    }

    private void setupGUI() {
    }

    public void updateGUI(String task) {
        this.taskLabel.setText(task);
    }

    private void setupListeners() {
    }

    private void initForAccountType() {

    }
}
