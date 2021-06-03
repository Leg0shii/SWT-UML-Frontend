package de.swt.gui.workspace;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.swt.drawing.objects.DrawableObject;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.group.Group;
import de.swt.logic.user.User;
import de.swt.util.AccountType;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

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

        this.menuPanel.add(new Menu(guiManager));
    }

    public void updateGUI() {
        ArrayList<User> users = new ArrayList<>();
        if (guiManager.currentGroup != null) {
            for (int id : guiManager.currentGroup.getParticipants()) {
                if (guiManager.getClient().userManager.getUserHashMap().containsKey(id)) {
                    users.add(guiManager.getClient().userManager.getUserHashMap().get(id));
                }
            }
            this.drawablePanel.updateGUI();
            this.objectListPanel.updateGUI(guiManager.getRelevantGroups(), users);
            this.initForAccountType();
            this.initForWorkspaceState();
        } else if (guiManager.currentSession != null) {
            for (int id : guiManager.currentSession.getParticipants()) {
                if (guiManager.getClient().userManager.getUserHashMap().containsKey(id)) {
                    users.add(guiManager.getClient().userManager.getUserHashMap().get(id));
                }
            }
            this.drawablePanel.updateGUI();
            this.objectListPanel.updateGUI(guiManager.getRelevantGroups(), users);
            this.initForAccountType();
            this.initForWorkspaceState();
        }

    }

    private void initForWorkspaceState() {
        switch (guiManager.state) {
            case VIEWING: {
                this.mainPanel.removeAll();
                this.mainPanel.revalidate();
                this.mainPanel.add(midPanel, BorderLayout.CENTER);
                this.drawablePanel.removeEditingOptions();
                this.mainPanel.revalidate();
                break;
            }
            case ANNOTATING: {
                this.mainPanel.removeAll();
                this.mainPanel.revalidate();
                this.mainPanel.add(leftPanel, BorderLayout.WEST);
                this.mainPanel.add(midPanel, BorderLayout.CENTER);
                this.mainPanel.add(rightPanel, BorderLayout.EAST);
                this.mainPanel.revalidate();
                this.symbolListPanel.addAnnotationsOptions();
                this.symbolListPanel.removeEditingOptions();
                this.drawablePanel.addEditingOptions();
                break;
            }
            case EDITING: {
                this.mainPanel.removeAll();
                this.mainPanel.revalidate();
                this.mainPanel.add(leftPanel, BorderLayout.WEST);
                this.mainPanel.add(midPanel, BorderLayout.CENTER);
                this.mainPanel.add(rightPanel, BorderLayout.EAST);
                this.mainPanel.revalidate();
                this.symbolListPanel.removeAnnotationOptions();
                this.symbolListPanel.addEditingOptions();
                this.drawablePanel.addEditingOptions();
                break;
            }
        }
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

    private void logout() {
        // logs out user
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
        Point point = new Point(rightPanel.getX() - rightPanel.getWidth(), rightPanel.getY() + rightPanel.getHeight() - 100);
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

    public void sendTaskProposition() {
        SelectGroupPanel selectGroupPanel = new SelectGroupPanel(guiManager);
        selectGroupPanel.updateGUI();
        Point point = new Point(midPanel.getX() + midPanel.getWidth() / 2, midPanel.getY() + midPanel.getHeight() / 4);
        SwingUtilities.convertPointToScreen(point, this);
        popups.get(2).hide();
        popups.set(2, factory.getPopup(this, selectGroupPanel, point.x, point.y));
        popups.get(2).show();
        selectGroupPanel.startTaskButton.addActionListener(e -> {
            guiManager.currentGroup = selectGroupPanel.getSelectedGroup();
            guiManager.currentGroup.getParticipants().add(guiManager.getClient().userid);
            try {
                guiManager.getClient().server.sendGroup(guiManager.currentGroup, guiManager.currentGroup.getId(), true);
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            popups.get(2).hide();
        });
    }

    public void setTimeTilLTermination(int minutes) {
        drawablePanel.setTimeTillTermination(minutes);
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

    public Group getSelectedGroup() {
        return selectedGroup;
    }

    public DrawableObject[] getDrawnObjects() {
        return drawablePanel.getDrawnObjects();
    }

    public DrawableObject[] getAnnotations() {
        return drawablePanel.getAnnotations();
    }

    public boolean removeLastAnnotations() {
        return drawablePanel.removeLastAnnotations();
    }

    public void removeAllIndexedObjects(DrawableObject[] objects) {
        drawablePanel.removeAllIndexedObjects(objects);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(leftPanel, BorderLayout.WEST);
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        leftPanel.add(menuPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, new Dimension(-1, 50), 0, false));
        objectListPanelPanel = new JPanel();
        objectListPanelPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        leftPanel.add(objectListPanelPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        writeRadioButton = new JRadioButton();
        writeRadioButton.setText("RadioButton");
        leftPanel.add(writeRadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        annotationRadioButton = new JRadioButton();
        annotationRadioButton.setText("RadioButton");
        leftPanel.add(annotationRadioButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logoutButton = new JButton();
        logoutButton.setText("Button");
        leftPanel.add(logoutButton, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sendTaskButton = new JButton();
        sendTaskButton.setText("Button");
        leftPanel.add(sendTaskButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        midPanel = new JPanel();
        midPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(midPanel, BorderLayout.CENTER);
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(rightPanel, BorderLayout.EAST);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    public int increaseObjectCounter() {
    }
}
