package gui.workspace;

import gui.GUI;
import gui.GUIManager;
import logic.group.Group;
import logic.user.User;
import util.AccountType;

import javax.swing.*;
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
    private GUIManager guiManager;
    private ObjectListPanel objectListPanel;

    public WorkspaceGUI(GUIManager guiManager) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.objectListPanelPanel.setLayout(new BoxLayout(objectListPanelPanel, BoxLayout.X_AXIS));
        this.menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        this.midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        this.rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
        this.objectListPanel = new ObjectListPanel(guiManager.language, guiManager.colorScheme);

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Schreibansicht", "Annotierungsansicht", "Abmelden");
            case ENGLISH -> setupGUI("Writing View", "Annotation View", "Logout");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), guiManager.colorScheme, 0);
        setupListeners();

        this.guiManager = guiManager;
    }

    private void setupGUI(String write, String annotate, String logout) {
        this.writeRadioButton.setText(write);
        this.annotationRadioButton.setText(annotate);
        this.logoutButton.setText(logout);
        this.objectListPanelPanel.add(objectListPanel);
    }

    public void updateGUI(List<Group> groups, List<User> users, AccountType accountType) {
        this.objectListPanel.updateGUI(groups, users, accountType);
    }

    private void setupListeners() {
        this.logoutButton.addActionListener(e -> logout());
    }

    private void initForAccountType() {
    }

    private void logout(){
        guiManager.switchToLoginGUI();
    }
}
