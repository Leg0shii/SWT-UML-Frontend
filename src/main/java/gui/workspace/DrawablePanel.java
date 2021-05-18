package gui.workspace;

import gui.GUI;
import util.AccountType;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DrawablePanel extends GUI {
    private JPanel mainPanel;
    private JButton taskButton;
    private JPanel drawPanel;
    private JToolBar toolBar;
    private JLabel remainingLabel;
    private Popup[] popups;
    private int popupCounter;
    private Language language;
    private Color[] colors;

    public DrawablePanel(Language language, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.drawPanel.setLayout(null);

        switch (language) {
            case GERMAN -> setupGUI("Aufgabe erstellen", "Verbleibende Zeit: ");
            case ENGLISH -> setupGUI("Create Task", "Remaining Time: ");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
        setupListeners();

        this.drawPanel.setBackground(Color.WHITE);
        Drawable drawable = new Drawable(10, 10, Color.BLUE);
        this.drawPanel.add(drawable);

        this.popups = new Popup[1];
        this.popupCounter = 0;

        this.language = language;
        this.colors = colors;
    }

    private void setupGUI(String task, String remaining) {
        this.taskButton.setText(task);
        this.remainingLabel.setText(remaining);
    }

    public void updateGUI(int remainingTime, AccountType accountType) {
        String[] split = this.remainingLabel.getText().split(" ");
        this.remainingLabel.setText(split[0] + " " + split[1] + " " + remainingTime);
        initForAccountType(accountType);
    }

    private void setupListeners() {
        this.taskButton.addActionListener(e -> {
            PopupFactory popupFactory = new PopupFactory();
            if (popupCounter % 2 == 0) {
                CreateTaskPanel createTaskPanel = new CreateTaskPanel(language, colors);
                createTaskPanel.taskScrollPanel.setPreferredSize(new Dimension(this.getWidth() / 4, this.getHeight() / 4));
                createTaskPanel.setPreferredSize(new Dimension(this.getWidth() / 3, this.getHeight() / 3));
                createTaskPanel.cancelButton.addActionListener(e1 -> {
                    popups[0].hide();
                    popupCounter++;
                });
                createTaskPanel.createButton.addActionListener(e2 -> {
                    createTaskPanel.createFunction();
                    popups[0].hide();
                    popupCounter++;
                });
                Point point = new Point(taskButton.getX() - this.getWidth() / 4, taskButton.getY() + taskButton.getHeight());
                SwingUtilities.convertPointToScreen(point, this);
                popups[0] = popupFactory.getPopup(this, createTaskPanel, point.x, point.y);
                popups[0].show();
            } else {
                popups[0].hide();
            }
            popupCounter++;
        });
    }

    private void initForAccountType(AccountType accountType) {
        if (accountType == AccountType.STUDENT) {
            this.mainPanel.remove(taskButton);
        }
    }

}
