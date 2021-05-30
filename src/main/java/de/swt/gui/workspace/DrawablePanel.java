package de.swt.gui.workspace;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.swt.drawing.objects.DrawableObject;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.util.AccountType;
import de.swt.util.Language;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class DrawablePanel extends GUI {
    private JPanel mainPanel;
    private JButton taskButton;
    public JPanel drawPanel;
    private JLabel remainingLabel;
    private JButton showTaskButton;
    private String task;
    private final Stack<JComponent> drawableObjects;

    public DrawablePanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.drawPanel.setLayout(null);
        this.drawableObjects = new Stack<>();

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Aufgabe erstellen", "Verbleibende Zeit:  Minuten", "Aufgabe anzeigen");
            case ENGLISH -> setupGUI("Create Task", "Remaining Time:  Minutes", "Show Task");
        }

        setupListeners();

        this.drawPanel.setBackground(Color.WHITE);
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

    public void updateGUI() {
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
                popups.set(0, factory.getPopup(guiManager, createTaskPanel, point.x, point.y));
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
                popups.set(1, popupFactory.getPopup(guiManager, showTaskPanel, point.x, point.y));
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
        DrawableObject object = (DrawableObject) component;
        if (object == null) {
            return;
        }
        object.init(guiManager);
        drawPanel.add(object);
        drawableObjects.add(object);
        drawPanel.repaint();
    }

    public void removeInteraction() {
        for (JComponent object : drawableObjects) {
            DrawableObject object1 = (DrawableObject) object;
            object1.removeInteraction();
        }
    }

    public void addInteraction() {
        for (JComponent object : drawableObjects) {
            DrawableObject object1 = (DrawableObject) object;
            object1.init(guiManager);
        }
    }

    public boolean removeLastDrawnObject() {
        if (!drawableObjects.isEmpty()) {
            DrawableObject object = (DrawableObject) drawableObjects.pop();
            if (!object.getClass().getSimpleName().matches("Thumb.*")) {
                object.closeAllPopups();
                drawPanel.remove(object);
                drawPanel.repaint();
                return true;
            } else {
                drawableObjects.push(object);
                return false;
            }
        }
        return false;
    }

    public void closeAllPopups() {
        for (Popup popup : popups) {
            popupCounter.set(popups.indexOf(popup), 0);
            popup.hide();
        }
        for (Component component : drawPanel.getComponents()) {
            DrawableObject object = (DrawableObject) component;
            object.closeAllPopups();
        }
    }

    public Component[] getDrawnObjects() {
        ArrayList<Component> components = new ArrayList<>();
        for (Component component : drawPanel.getComponents()) {
            if (!component.getClass().getSimpleName().matches("Thumb.*")) {
                components.add(component);
            }
        }
        return components.toArray(new Component[0]);
    }

    public Component[] getAnnotations() {
        ArrayList<Component> components = new ArrayList<>();
        for (Component component : drawPanel.getComponents()) {
            if (component.getClass().getSimpleName().matches("Thumb.*")) {
                components.add(component);
            }
        }
        return components.toArray(new Component[0]);
    }

    public boolean removeLastAnnotations() {
        if (!drawableObjects.isEmpty()) {
            DrawableObject object = (DrawableObject) drawableObjects.pop();
            if (object.getClass().getSimpleName().matches("Thumb.*")) {
                object.closeAllPopups();
                drawPanel.remove(object);
                drawPanel.repaint();
                return true;
            } else {
                drawableObjects.push(object);
                return false;
            }
        }
        return false;
    }

    public void removeAllIndexedObjects(Component[] objects) {
        for (Component component : objects) {
            DrawableObject object = (DrawableObject) component;
            if (object == null) {
                return;
            }
            for (JComponent stackComponent : drawableObjects) {
                DrawableObject stackObject = (DrawableObject) stackComponent;
                if (Arrays.equals(object.getID(), stackObject.getID())) {
                    drawPanel.remove(stackObject);
                    drawableObjects.remove(stackObject);
                }
            }
        }
    }

    public void removeEditingOptions() {
        removeInteraction();
    }

    public void addEditingOptions() {
        if (!drawableObjects.isEmpty() && drawableObjects.get(0).getMouseListeners().length == 0) {
            addInteraction();
        }
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
        mainPanel.setLayout(new GridLayoutManager(2, 5, new Insets(5, 5, 5, 5), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        drawPanel = new JPanel();
        drawPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(drawPanel, new GridConstraints(1, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        taskButton = new JButton();
        taskButton.setText("Button");
        mainPanel.add(taskButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        remainingLabel = new JLabel();
        remainingLabel.setText("Label");
        mainPanel.add(remainingLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        showTaskButton = new JButton();
        showTaskButton.setText("Button");
        mainPanel.add(showTaskButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
