package de.swt.gui.workspace;

import de.swt.drawing.Drawable;
import de.swt.drawing.DrawableUseCase;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.util.AccountType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DrawablePanel extends GUI {
    private JPanel mainPanel;
    private JButton taskButton;
    public JPanel drawPanel;
    private JLabel remainingLabel;
    private JButton showTaskButton;
    private String task;

    public DrawablePanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.drawPanel.setLayout(null);

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Aufgabe erstellen", "Verbleibende Zeit:  Minuten", "Aufgabe anzeigen");
            case ENGLISH -> setupGUI("Create Task", "Remaining Time:  Minutes", "Show Task");
        }

        setupListeners();

        this.drawPanel.setBackground(Color.WHITE);
        for (int i = 1; i < 4; i++) {
            DrawableUseCase useCase = new DrawableUseCase(10, 10, i, "Test");
            this.drawPanel.add(useCase);
        }
        Drawable dbstickman = new Drawable(20, 20, Color.black);
        this.drawPanel.add(dbstickman);
    }

    private void setupGUI(String task, String remaining, String showTask) {
        this.taskButton.setText(task);
        this.remainingLabel.setText(remaining);
        this.showTaskButton.setText(showTask);
        initPopups(2);
    }

    public void updateGUI(int remainingTime) {
        String[] split = this.remainingLabel.getText().split(" ");
        this.remainingLabel.setText(split[0] + " " + split[1] + " " + remainingTime + " " + split[3]);
        initForAccountType();
    }

    private void setupListeners() {
        this.taskButton.addActionListener(e1 -> {
            if (popupCounter.get(0) % 2 == 0) {
                CreateTaskPanel createTaskPanel = new CreateTaskPanel(guiManager);
                createTaskPanel.taskScrollPanel.setPreferredSize(new Dimension(this.getWidth() / 4, this.getHeight() / 4));
                createTaskPanel.cancelButton.addActionListener(e11 -> {
                    popups.get(0).hide();
                    incrementPopupCounter(0);
                });
                createTaskPanel.createButton.addActionListener(e12 -> {
                    createTaskPanel.createFunction();
                    popups.get(0).hide();
                    incrementPopupCounter(0);
                });
                Point point = new Point(taskButton.getX() - this.getWidth() / 4, taskButton.getY() + taskButton.getHeight());
                SwingUtilities.convertPointToScreen(point, this);
                popups.set(0, factory.getPopup(this, createTaskPanel, point.x, point.y));
                popups.get(0).show();
            } else {
                popups.get(0).hide();
            }
            incrementPopupCounter(0);
        });
        this.showTaskButton.addActionListener(e2 -> {
            PopupFactory popupFactory = new PopupFactory();
            if (popupCounter.get(1) % 2 == 0) {
                ShowTaskPanel showTaskPanel = new ShowTaskPanel(guiManager);
                showTaskPanel.updateGUI(task);
                Point point = new Point(showTaskButton.getX() - this.getWidth() / 4, showTaskButton.getY() + showTaskButton.getHeight());
                SwingUtilities.convertPointToScreen(point, this);
                popups.set(1, popupFactory.getPopup(this, showTaskPanel, point.x, point.y));
                popups.get(1).show();
            } else {
                popups.get(1).hide();
            }
            incrementPopupCounter(1);
        });
    }

    private void initForAccountType() {
        if (guiManager.accountType == AccountType.STUDENT) {
            this.mainPanel.remove(taskButton);
        } else {
            this.mainPanel.remove(showTaskButton);
        }
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void addToDrawPanel(JComponent component) {
        drawPanel.add(component);
        drawPanel.repaint();
    }
}
