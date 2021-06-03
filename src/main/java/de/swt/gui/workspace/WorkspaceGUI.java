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
    private final SymbolListPanel symbolListPanel;

    public WorkspaceGUI(GUIManager guiManager) {
        super(guiManager);
        this.add(mainPanel);
        this.objectListPanelPanel.setLayout(new GridLayout(1, 1));
        this.menuPanel.setLayout(new GridLayout(1, 1));
        this.midPanel.setLayout(new GridLayout(1, 1));
        this.rightPanel.setLayout(new GridLayout(1, 1));
        this.objectListPanel = new ObjectListPanel(guiManager);
        this.drawablePanel = new DrawablePanel(guiManager);
        this.symbolListPanel = new SymbolListPanel(guiManager);

        switch (guiManager.getLanguage()) {
            case GERMAN -> setupGUI("Schreibansicht", "Annotierungsansicht", "Abmelden", "Lösung einreichen");
            case ENGLISH -> setupGUI("Writing View", "Annotation View", "Logout", "Send Solution");
        }

        setupListeners();
    }

    private void setupGUI(String write, String annotate, String logout, String sendTask) {
        this.writeRadioButton.setText(write);
        this.annotationRadioButton.setText(annotate);
        this.logoutButton.setText(logout);
        this.objectListPanelPanel.add(objectListPanel);
        this.midPanel.add(drawablePanel);
        this.sendTaskButton.setText(sendTask);
        this.rightPanel.add(symbolListPanel);

        this.menuPanel.add(new Menu(getGuiManager()));
        initForAccountType();
    }

    @Override
    public void updateGUI() {
        drawablePanel.updateGUI();
        objectListPanel.updateGUI();
        symbolListPanel.updateGUI();
        initForWorkspaceState();
        revalidate();
    }

    private void initForWorkspaceState() {
        switch (getGuiManager().getWorkspaceState()) {
            case VIEWING -> {
                this.mainPanel.removeAll();
                this.mainPanel.revalidate();
                this.mainPanel.add(midPanel, BorderLayout.CENTER);
                this.drawablePanel.removeEditingOptions();
                this.mainPanel.revalidate();
            }
            case ANNOTATING -> {
                this.mainPanel.removeAll();
                this.mainPanel.revalidate();
                this.mainPanel.add(leftPanel, BorderLayout.WEST);
                this.mainPanel.add(midPanel, BorderLayout.CENTER);
                this.mainPanel.add(rightPanel, BorderLayout.EAST);
                this.mainPanel.revalidate();
                this.symbolListPanel.addAnnotationsOptions();
                this.symbolListPanel.removeEditingOptions();
                this.drawablePanel.addEditingOptions();
            }
            case EDITING -> {
                this.mainPanel.removeAll();
                this.mainPanel.revalidate();
                this.mainPanel.add(leftPanel, BorderLayout.WEST);
                this.mainPanel.add(midPanel, BorderLayout.CENTER);
                this.mainPanel.add(rightPanel, BorderLayout.EAST);
                this.mainPanel.revalidate();
                this.symbolListPanel.removeAnnotationOptions();
                this.symbolListPanel.addEditingOptions();
                this.drawablePanel.addEditingOptions();
            }
        }
    }

    @Override
    public void setupListeners() {
        this.logoutButton.addActionListener(e -> logout());
        setupPopupToTheRight(sendTaskButton, new SubmitTaskPanel(getGuiManager()));
    }

    private void initForAccountType() {
        if (getGuiManager().getAccountType() != AccountType.STUDENT) {
            this.leftPanel.remove(sendTaskButton);
        }
        if (getGuiManager().getAccountType() == AccountType.STUDENT) {
            this.leftPanel.remove(annotationRadioButton);
            this.leftPanel.remove(writeRadioButton);
        }
    }

    private void logout() {
        getGuiManager().switchToLoginGUI();
    }

    public void sendRequest(User user) {
        RequestPanel requestPanel = new RequestPanel(getGuiManager());
        requestPanel.setUser(user);
        requestPanel.updateGUI();
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(requestPanel);
        popupMenu.show(getGuiManager(), getGuiManager().getWidth() - 100, getGuiManager().getHeight() - 100);
    }

    public void sendTaskProposition() {
        SelectGroupPanel selectGroupPanel = new SelectGroupPanel(getGuiManager());
        selectGroupPanel.updateGUI();
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(selectGroupPanel);
        popupMenu.show(getGuiManager(), getGuiManager().getWidth() / 2, 100);
    }

    public void setTimeTilLTermination(int minutes) {
        drawablePanel.setTimeTillTermination(minutes);
    }

    public void setTask(String task) {
        this.drawablePanel.setTask(task);
    }

    public void addToDrawPanel(DrawableObject object) {
        drawablePanel.addToDrawPanel(object);
    }

    public boolean removeLastDrawnObject() {
        return drawablePanel.removeLastDrawnObject();
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

    public void removeLastObject() {
        drawablePanel.removeLastObject();
    }

    public int increaseObjectCounter() {
        return drawablePanel.increaseObjectCounter();
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
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(writeRadioButton);
        buttonGroup.add(annotationRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
