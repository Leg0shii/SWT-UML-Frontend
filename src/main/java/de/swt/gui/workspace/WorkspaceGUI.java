package de.swt.gui.workspace;

import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.group.Group;
import de.swt.logic.user.User;
import de.swt.util.AccountType;

import javax.swing.*;
import java.awt.*;
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
    private JMenuBar menuBar;
    private final ObjectListPanel objectListPanel;
    private final DrawablePanel drawablePanel;
    private Group selectedGroup;
    private final SymbolListPanel symbolListPanel;

    public WorkspaceGUI(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.objectListPanelPanel.setLayout(new BoxLayout(objectListPanelPanel, BoxLayout.X_AXIS));
        this.menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        this.midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        this.rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
        this.objectListPanel = new ObjectListPanel(guiManager);
        this.drawablePanel = new DrawablePanel(guiManager);
        this.symbolListPanel = new SymbolListPanel(guiManager);

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Schreibansicht", "Annotierungsansicht", "Abmelden", "Lösung einreichen");
            case ENGLISH -> setupGUI("Writing View", "Annotation View", "Logout", "Send Solution");
        }

        setupListeners();

        this.guiManager = guiManager;

        initPopups(3);
    }

    private void setupGUI(String write, String annotate, String logout, String sendTask) {
        this.writeRadioButton.setText(write);
        this.annotationRadioButton.setText(annotate);
        this.logoutButton.setText(logout);
        this.objectListPanelPanel.add(objectListPanel);
        this.midPanel.add(drawablePanel);
        this.sendTaskButton.setText(sendTask);
        this.rightPanel.add(symbolListPanel);
    }

    public void updateGUI(List<Group> groups, List<User> users, int remainingTime) {
        this.objectListPanel.updateGUI(groups, users);
        this.drawablePanel.updateGUI(remainingTime);
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
            if (popupCounter.get(0) % 2 == 0) {
                SubmitTaskPanel submitTaskPanel = new SubmitTaskPanel(guiManager);
                submitTaskPanel.yesButton.addActionListener(e21 -> {
                    submitTaskPanel.yesFunction();
                    popups.get(0).hide();
                    incrementPopupCounter(0);
                });
                submitTaskPanel.noButton.addActionListener(e22 -> {
                    popups.get(0).hide();
                    incrementPopupCounter(0);
                });
                Point point = new Point(sendTaskButton.getX() + sendTaskButton.getWidth(), sendTaskButton.getY());
                SwingUtilities.convertPointToScreen(point, this.leftPanel);
                popups.get(0).hide();
                popups.set(0, factory.getPopup(this.leftPanel, submitTaskPanel, point.x, point.y));
                popups.get(0).show();
            } else {
                popups.get(0).hide();
            }
            incrementPopupCounter(0);
        });
    }

    private void initForAccountType() {
        if (guiManager.accountType != AccountType.STUDENT) {
            this.leftPanel.remove(sendTaskButton);
        }
        if (guiManager.accountType == AccountType.STUDENT) {
            this.leftPanel.remove(annotationRadioButton);
            this.leftPanel.remove(writeRadioButton);
        }
    }

    private void initMenu(String file){
        JMenu fileMenu = new JMenu(file);
    }

    private void logout() {
        // logs out user
        guiManager.getClient().userManager.userLogout();
        guiManager.switchToLoginGUI();
    }

    public boolean isWrite() {
        return writeRadioButton.isSelected();
    }

    public boolean isAnnotate() {
        return annotationRadioButton.isSelected();
    }

    public void sendRequest(User user) {
        RequestPanel requestPanel = new RequestPanel(guiManager);
        requestPanel.updateGUI(user);
        Point point = new Point(rightPanel.getX() - rightPanel.getWidth(), rightPanel.getY() + rightPanel.getHeight() -100);
        SwingUtilities.convertPointToScreen(point, this.midPanel);
        popups.get(1).hide();
        popups.set(1, factory.getPopup(this, requestPanel, point.x, point.y));
        popups.get(1).show();
        requestPanel.denyButton.addActionListener(e1 -> {
            requestPanel.denyFunction();
            popups.get(1).hide();
        });
        requestPanel.acceptButton.addActionListener(e2 -> {
            requestPanel.acceptFunction();
            popups.get(1).hide();
        });
    }

    public void sendTaskProposition(List<Group> groups) {
        SelectGroupPanel selectGroupPanel = new SelectGroupPanel(guiManager);
        selectGroupPanel.updateGUI(groups);
        Point point = new Point(midPanel.getX() + midPanel.getWidth() / 2, midPanel.getY() + midPanel.getHeight() / 4);
        SwingUtilities.convertPointToScreen(point, this);
        popups.get(2).hide();
        popups.set(2, factory.getPopup(this, selectGroupPanel, point.x, point.y));
        popups.get(2).show();
        selectGroupPanel.startTaskButton.addActionListener(e -> {
            this.selectedGroup = selectGroupPanel.getSelectedGroup();
            popups.get(2).hide();
        });
    }

    public void setTask(String task) {
        this.drawablePanel.setTask(task);
    }

    public void addToDrawPanel(JComponent component) {
        drawablePanel.addToDrawPanel(component);
    }

    public boolean removeLastDrawnObject() {
        return drawablePanel.removeLastDrawnObject();
    }

    public Group getSelectedGroup(){
        return selectedGroup;
    }

    public Component[] getDrawnObjects() {
        return drawablePanel.getDrawnObjects();
    }
}
