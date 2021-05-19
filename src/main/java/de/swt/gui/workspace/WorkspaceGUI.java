package de.swt.gui.workspace;

import de.swt.drawing.Stickmanbutton;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.Group;
import de.swt.logic.User;
import de.swt.util.AccountType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceGUI extends GUI {
    private JPanel mainPanel;
    private JRadioButton writeRadioButton;
    private JRadioButton annotationRadioButton;
    private JButton logoutButton;
    private JPanel leftPanel;
    private JPanel menuPanel;
    private JPanel objectListPanelPanel;
    private JPanel midPanel;
    private JPanel rightPanel;
    private JButton sendTaskButton;
    private GUIManager guiManager;
    private ObjectListPanel objectListPanel;
    private DrawablePanel drawablePanel;
    private Group selectedGroup;
    private AccountType accountType;
    private int[] popupCounter;
    private Popup[] popups;
    private SymbolListPanel symbolListPanel;

    public WorkspaceGUI(GUIManager guiManager) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.objectListPanelPanel.setLayout(new BoxLayout(objectListPanelPanel, BoxLayout.X_AXIS));
        this.menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        this.midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        this.rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
        this.objectListPanel = new ObjectListPanel(guiManager.language, guiManager.colorScheme);
        this.drawablePanel = new DrawablePanel(guiManager.language, guiManager.colorScheme);
        this.symbolListPanel = new SymbolListPanel(guiManager.colorScheme);

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Schreibansicht", "Annotierungsansicht", "Abmelden", "Lösung einreichen");
            case ENGLISH -> setupGUI("Writing View", "Annotation View", "Logout", "Send Solution");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), guiManager.colorScheme, 0);
        setupListeners();

        this.guiManager = guiManager;
        this.popupCounter = new int[1];
        this.popups = new Popup[1];
    }

    private void setupGUI(String write, String annotate, String logout, String sendTask) {
        this.writeRadioButton.setText(write);
        this.annotationRadioButton.setText(annotate);
        this.logoutButton.setText(logout);
        this.objectListPanelPanel.add(objectListPanel);
        this.midPanel.add(drawablePanel);
        this.sendTaskButton.setText(sendTask);
        this.symbolListPanel.symbolPanel.add(new Stickmanbutton(0,0,drawablePanel));
        this.rightPanel.add(symbolListPanel);
    }

    public void updateGUI(List<Group> groups, List<User> users, AccountType accountType, int remainingTime) {
        this.accountType = accountType;
        this.objectListPanel.updateGUI(groups, users, accountType);
        this.drawablePanel.updateGUI(remainingTime, accountType);
        this.initForAccountType();
    }

    private void setupListeners() {
        this.logoutButton.addActionListener(e -> logout());
        this.writeRadioButton.addActionListener(e1 -> {
            if (isAnnotate()) {
                annotationRadioButton.setSelected(false);
            }
        });
        this.annotationRadioButton.addActionListener(e2 -> {
            if (isWrite()) {
                writeRadioButton.setSelected(false);
            }
        });
        this.sendTaskButton.addActionListener(e3 -> {
            PopupFactory popupFactory = new PopupFactory();
            if (popupCounter[0] % 2 == 0) {
                SubmitTaskPanel submitTaskPanel = new SubmitTaskPanel(guiManager.language, guiManager.colorScheme);
                submitTaskPanel.yesButton.addActionListener(e21 -> {
                    submitTaskPanel.yesFunction();
                    popups[0].hide();
                    popupCounter[0]++;
                });
                submitTaskPanel.noButton.addActionListener(e22 -> {
                    popups[0].hide();
                    popupCounter[0]++;
                });
                Point point = new Point(sendTaskButton.getX() + sendTaskButton.getWidth(), sendTaskButton.getY());
                SwingUtilities.convertPointToScreen(point, this.leftPanel);
                popups[0] = popupFactory.getPopup(this.leftPanel, submitTaskPanel, point.x, point.y);
                popups[0].show();
            } else {
                popups[0].hide();
            }
            popupCounter[0]++;
        });
    }

    private void initForAccountType() {
        if (accountType != AccountType.STUDENT) {
            this.leftPanel.remove(sendTaskButton);
        }
        if (accountType == AccountType.STUDENT) {
            this.leftPanel.remove(annotationRadioButton);
            this.leftPanel.remove(writeRadioButton);
        }
    }

    private void logout() {
        guiManager.switchToLoginGUI();
    }

    public boolean isWrite() {
        return writeRadioButton.isSelected();
    }

    public boolean isAnnotate() {
        return annotationRadioButton.isSelected();
    }

    public void sendRequest(User user) {
        RequestPanel requestPanel = new RequestPanel(guiManager.language, guiManager.colorScheme);
        requestPanel.updateGUI(user);
        PopupFactory popupFactory = new PopupFactory();
        Popup[] popups = new Popup[1];
        Point point = new Point(rightPanel.getX() - rightPanel.getWidth(), rightPanel.getY() + rightPanel.getHeight() -100);
        SwingUtilities.convertPointToScreen(point, this.midPanel);
        popups[0] = popupFactory.getPopup(this, requestPanel, point.x, point.y);
        popups[0].show();
        requestPanel.denyButton.addActionListener(e1 -> {
            requestPanel.denyFunction();
            popups[0].hide();
        });
        requestPanel.acceptButton.addActionListener(e2 -> {
            requestPanel.acceptFunction();
            popups[0].hide();
        });
    }

    public void sendTaskProposition(List<Group> groups) {
        SelectGroupPanel selectGroupPanel = new SelectGroupPanel(guiManager.language, guiManager.colorScheme);
        selectGroupPanel.updateGUI(groups);
        PopupFactory popupFactory = new PopupFactory();
        Popup[] popups = new Popup[1];
        Point point = new Point(midPanel.getX() + midPanel.getWidth() / 2, midPanel.getY() + midPanel.getHeight() / 4);
        SwingUtilities.convertPointToScreen(point, this);
        popups[0] = popupFactory.getPopup(this, selectGroupPanel, point.x, point.y);
        popups[0].show();
        selectGroupPanel.startTaskButton.addActionListener(e -> {
            this.selectedGroup = selectGroupPanel.getSelectedGroup();
            popups[0].hide();
        });
    }

    public void setTask(String task) {
        this.drawablePanel.setTask(task);
    }
}
