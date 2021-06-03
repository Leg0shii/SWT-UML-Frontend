package de.swt.gui.workspace;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.swt.drawing.objects.DrawableObject;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;

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
    private final Stack<DrawableObject> drawableObjects;
    private final ShowTaskPanel showTaskPanel;

    public DrawablePanel(GUIManager guiManager) {
        super(guiManager);
        this.drawPanel.setLayout(null);
        this.drawableObjects = new Stack<>();

        switch (guiManager.getLanguage()) {
            case GERMAN -> setupGUI("Aufgabe erstellen", "Verbleibende Zeit:  Minuten", "Aufgabe anzeigen");
            case ENGLISH -> setupGUI("Create Task", "Remaining Time:  Minutes", "Show Task");
        }

        setupListeners();
        initForAccountType();
        showTaskPanel = new ShowTaskPanel(getGuiManager());

        this.drawPanel.setBackground(Color.WHITE);
    }

    private void setupGUI(String task, String remaining, String showTask) {
        this.taskButton.setText(task);
        this.remainingLabel.setText(remaining);
        this.showTaskButton.setText(showTask);
    }

    public void updateGUI() {
        showTaskPanel.updateGUI();
        revalidate();
    }

    public void setupListeners() {
        switch (getGuiManager().getAccountType()) {
            case STUDENT -> setupStandardPopup(showTaskButton, showTaskPanel);
            case TEACHER -> setupStandardPopup(taskButton, new CreateTaskPanel(getGuiManager()));
        }
    }

    private void initForAccountType() {
        switch (getGuiManager().getAccountType()) {
            case STUDENT -> mainPanel.remove(taskButton);
            case TEACHER -> mainPanel.remove(showTaskButton);
            case ADMIN -> {
                mainPanel.remove(showTaskButton);
                mainPanel.remove(taskButton);
            }
        }
    }

    public void setTask(String task) {
        showTaskPanel.setTask(task);
    }

    public void addToDrawPanel(DrawableObject object) {
        if (object == null) {
            return;
        }
        object.init(getGuiManager());
        drawPanel.add(object);
        drawableObjects.add(object);
        drawPanel.repaint();
    }

    public void removeInteraction() {
        for (DrawableObject object : drawableObjects) {
            object.removeInteraction();
        }
    }

    public void addInteraction() {
        for (DrawableObject object : drawableObjects) {
            object.init(getGuiManager());
        }
    }

    public boolean removeLastDrawnObject() {
        if (!drawableObjects.isEmpty()) {
            DrawableObject object = drawableObjects.pop();
            if (!object.getClass().getSimpleName().matches("Thumb.*")) {
                drawPanel.remove(object);
                drawPanel.repaint();
                return true;
            } else {
                removeLastDrawnObject();
                drawableObjects.push(object);
                return false;
            }
        }
        return false;
    }

    public DrawableObject[] getDrawnObjects() {
        ArrayList<DrawableObject> objects = new ArrayList<>();
        for (Component component : drawPanel.getComponents()) {
            DrawableObject object = (DrawableObject) component;
            if (!object.getClass().getSimpleName().matches("Thumb.*")) {
                objects.add(object);
            }
        }
        return (DrawableObject[]) objects.toArray();
    }

    public DrawableObject[] getAnnotations() {
        ArrayList<DrawableObject> objects = new ArrayList<>();
        for (Component component : drawPanel.getComponents()) {
            DrawableObject object = (DrawableObject) component;
            if (object.getClass().getSimpleName().matches("Thumb.*")) {
                objects.add(object);
            }
        }
        return (DrawableObject[]) objects.toArray();
    }

    public boolean removeLastAnnotations() {
        if (!drawableObjects.isEmpty()) {
            DrawableObject object = drawableObjects.pop();
            if (object.getClass().getSimpleName().matches("Thumb.*")) {
                drawPanel.remove(object);
                drawPanel.repaint();
                return true;
            } else {
                removeLastAnnotations();
                drawableObjects.push(object);
                return false;
            }
        }
        return false;
    }

    public void removeAllIndexedObjects(DrawableObject[] objects) {
        ArrayList<DrawableObject> objectsToRemove = new ArrayList<>();
        for (DrawableObject object : objects) {
            if (object == null) {
                return;
            }
            for (DrawableObject stackObject : drawableObjects) {
                if (Arrays.equals(object.getID(), stackObject.getID())) {
                    objectsToRemove.add(stackObject);
                }
            }
        }
        for (DrawableObject object : objectsToRemove) {
            drawPanel.remove(object);
            drawableObjects.remove(object);
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

    public void setTimeTillTermination(int minutes) {
        String[] split = this.remainingLabel.getText().split(" ");
        this.remainingLabel.setText(split[0] + " " + split[1] + " " + minutes + " " + split[3]);
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
