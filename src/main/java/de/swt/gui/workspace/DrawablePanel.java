package de.swt.gui.workspace;

import de.swt.drawing.Drawable;
import de.swt.drawing.DrawableUseCase;
import de.swt.gui.GUI;
import de.swt.util.AccountType;
import de.swt.util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DrawablePanel extends GUI {
    private JPanel mainPanel;
    private JButton taskButton;
    private JPanel drawPanel;
    private JLabel remainingLabel;
    private JButton showTaskButton;
    private Popup[] popups;
    private int[] popupCounter;
    private Language language;
    private Color[] colors;
    private String task;

    public DrawablePanel(Language language, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.drawPanel.setLayout(null);

        switch (language) {
            case GERMAN -> setupGUI("Aufgabe erstellen", "Verbleibende Zeit:  Minuten", "Aufgabe anzeigen");
            case ENGLISH -> setupGUI("Create Task", "Remaining Time:  Minutes", "Show Task");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
        setupListeners();

        this.drawPanel.setBackground(Color.WHITE);
        for (int i = 1; i < 4; i++) {
            DrawableUseCase useCase = new DrawableUseCase(10,10,i,"Pussy");
            this.drawPanel.add(useCase);
        }

        this.popups = new Popup[2];
        this.popupCounter = new int[2];

        this.language = language;
        this.colors = colors;
    }

    private void setupGUI(String task, String remaining, String showTask) {
        this.taskButton.setText(task);
        this.remainingLabel.setText(remaining);
        this.showTaskButton.setText(showTask);
    }

    public void updateGUI(int remainingTime, AccountType accountType) {
        String[] split = this.remainingLabel.getText().split(" ");
        this.remainingLabel.setText(split[0] + " " + split[1] + " " + remainingTime + " " + split[3]);
        initForAccountType(accountType);
    }

    private void setupListeners() {
        this.taskButton.addActionListener(e1 -> {
            PopupFactory popupFactory = new PopupFactory();
            if (popupCounter[0] % 2 == 0) {
                CreateTaskPanel createTaskPanel = new CreateTaskPanel(language, colors);
                createTaskPanel.taskScrollPanel.setPreferredSize(new Dimension(this.getWidth() / 4, this.getHeight() / 4));
                createTaskPanel.cancelButton.addActionListener(e11 -> {
                    popups[0].hide();
                    popupCounter[0]++;
                });
                createTaskPanel.createButton.addActionListener(e12 -> {
                    createTaskPanel.createFunction();
                    popups[0].hide();
                    popupCounter[0]++;
                });
                Point point = new Point(taskButton.getX() - this.getWidth() / 4, taskButton.getY() + taskButton.getHeight());
                SwingUtilities.convertPointToScreen(point, this);
                popups[0] = popupFactory.getPopup(this, createTaskPanel, point.x, point.y);
                popups[0].show();
            } else {
                popups[0].hide();
            }
            popupCounter[0]++;
        });
        this.showTaskButton.addActionListener(e2 -> {
            PopupFactory popupFactory = new PopupFactory();
            if (popupCounter[1] % 2 == 0) {
                ShowTaskPanel showTaskPanel = new ShowTaskPanel(language, colors);
                showTaskPanel.updateGUI(task);
                Point point = new Point(showTaskButton.getX() - this.getWidth() / 4, showTaskButton.getY() + showTaskButton.getHeight());
                SwingUtilities.convertPointToScreen(point, this);
                popups[1] = popupFactory.getPopup(this, showTaskPanel, point.x, point.y);
                popups[1].show();
            } else {
                popups[1].hide();
            }
            popupCounter[1]++;
        });
    }

    private void initForAccountType(AccountType accountType) {
        if (accountType == AccountType.STUDENT) {
            this.mainPanel.remove(taskButton);
        } else {
            this.mainPanel.remove(showTaskButton);
        }
    }

    public void setTask(String task) {
        this.task = task;
    }
}
