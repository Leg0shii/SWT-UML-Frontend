package de.swt.drawing.buttonGUIS;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.swt.drawing.objects.RotatableObject;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import static java.lang.Math.abs;

public class RotatingGUI extends GUI {
    private JPanel mainPanel;
    private JCheckBox arrowHeadOrientationCheckBox;
    private JButton colorButton;
    private JTextField descriptionTextField;
    private JSlider scaleSlider;
    private JSlider rotationSlider;
    private JLabel descriptionLabel;
    private JLabel scaleLabel;
    private JLabel rotationLabel;
    private JLabel colorLabel;
    private final JColorChooser colorChooser;
    private final RotatableObject associatedObject;
    private String description;
    private Color color;
    private double scale;
    public int startYOffset;
    public int endYOffset;
    public boolean switchSides;

    public RotatingGUI(GUIManager guiManager, RotatableObject associatedObject) {
        super(guiManager);
        this.add(mainPanel);
        this.associatedObject = associatedObject;
        colorChooser = new JColorChooser();

        switch (guiManager.getLanguage()) {
            case GERMAN -> setupGUI("Beschreibung", "Skalierung", "Farbe", "Farbe auswählen", "Rotation", "Pfeilspitze rechts");
            case ENGLISH -> setupGUI("Description", "Scale", "Color", "Choose Color", "Rotation", "Arrow Head on the right");
        }

        setupListeners();
        updateGUI();
    }

    private void setupGUI(String description, String scale, String color, String chooseColor, String rotation, String arrowHeadOrientation) {
        this.descriptionLabel.setText(description);
        this.scaleLabel.setText(scale);
        this.colorLabel.setText(color);
        this.colorButton.setText(chooseColor);
        this.rotationLabel.setText(rotation);
        this.arrowHeadOrientationCheckBox.setText(arrowHeadOrientation);
        this.scaleSlider.setMinimum(5);
        this.scaleSlider.setMaximum(30);
        this.scaleSlider.setMinorTickSpacing(1);
        this.scaleSlider.setPaintTicks(true);
        this.scaleSlider.setSnapToTicks(true);
        this.scaleSlider.setPaintLabels(true);
        for (AbstractColorChooserPanel panel : colorChooser.getChooserPanels()) {
            if (!panel.getDisplayName().equals("HSV")) {
                colorChooser.removeChooserPanel(panel);
            } else {
                panel.setBorder(BorderFactory.createEtchedBorder());
            }
        }
        colorChooser.setPreviewPanel(new JPanel());

        this.rotationSlider.setMinimum((int) (-100 * associatedObject.scale));
        this.rotationSlider.setMaximum((int) (100 * associatedObject.scale));
        this.rotationSlider.setMinorTickSpacing(1);
        this.rotationSlider.setPaintLabels(true);

        this.scaleSlider.setValue((int) (associatedObject.scale * 10));
        this.rotationSlider.setValue(associatedObject.endYOffset - associatedObject.startYOffset);
        this.arrowHeadOrientationCheckBox.setSelected(associatedObject.switchSides);
        this.colorChooser.setColor(associatedObject.color);
        this.descriptionTextField.setText(associatedObject.description);
        this.descriptionTextField.setEditable(true);
    }

    @Override
    public void updateGUI() {
        this.description = associatedObject.description;
        this.color = associatedObject.color;
        this.scale = associatedObject.scale;
        this.startYOffset = associatedObject.startYOffset;
        this.endYOffset = associatedObject.endYOffset;
        this.switchSides = associatedObject.switchSides;
        this.descriptionTextField.setEditable(true);
        this.revalidate();
        this.mainPanel.revalidate();
    }

    @Override
    public void setupListeners() {
        descriptionTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                associatedObject.updateComponent(descriptionTextField.getText(), scale, color, startYOffset, endYOffset, switchSides);
                updateGUI();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                associatedObject.updateComponent(descriptionTextField.getText(), scale, color, startYOffset, endYOffset, switchSides);
                updateGUI();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                associatedObject.updateComponent(descriptionTextField.getText(), scale, color, startYOffset, endYOffset, switchSides);
                updateGUI();
            }
        });
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(colorChooser);
        colorButton.setComponentPopupMenu(popupMenu);
        colorButton.addActionListener(e -> colorButton.getComponentPopupMenu().show(associatedObject, 0, 0));

        scaleSlider.addChangeListener(e -> {
            associatedObject.updateComponent(description, convertSliderValue(scaleSlider.getValue()), color, startYOffset, endYOffset, switchSides);
            updateGUI();
        });
        colorChooser.getSelectionModel().addChangeListener(e -> {
            associatedObject.updateComponent(description, scale, colorChooser.getColor(), startYOffset, endYOffset, switchSides);
            updateGUI();
        });
        rotationSlider.addChangeListener(e -> {
            if (rotationSlider.getValue() >= 0) {
                associatedObject.updateComponent(description, scale, color, startYOffset, (int) (rotationSlider.getValue() * scale), switchSides);
            } else {
                associatedObject.updateComponent(description, scale, color, (int) abs(rotationSlider.getValue() * scale), endYOffset, switchSides);
            }
            updateGUI();
        });
        arrowHeadOrientationCheckBox.addChangeListener(e -> {
            associatedObject.updateComponent(description, scale, color, startYOffset, endYOffset, arrowHeadOrientationCheckBox.isSelected());
            updateGUI();
        });
    }

    private double convertSliderValue(int val) {
        return ((double) val) / 10;
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
        mainPanel.setLayout(new GridLayoutManager(5, 6, new Insets(5, 5, 5, 5), -1, -1));
        descriptionLabel = new JLabel();
        descriptionLabel.setText("Label");
        mainPanel.add(descriptionLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scaleLabel = new JLabel();
        scaleLabel.setText("Label");
        mainPanel.add(scaleLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rotationLabel = new JLabel();
        rotationLabel.setText("Label");
        mainPanel.add(rotationLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        arrowHeadOrientationCheckBox = new JCheckBox();
        arrowHeadOrientationCheckBox.setText("CheckBox");
        mainPanel.add(arrowHeadOrientationCheckBox, new GridConstraints(3, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        colorLabel = new JLabel();
        colorLabel.setText("Label");
        mainPanel.add(colorLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        colorButton = new JButton();
        colorButton.setText("Button");
        mainPanel.add(colorButton, new GridConstraints(4, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        descriptionTextField = new JTextField();
        mainPanel.add(descriptionTextField, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        scaleSlider = new JSlider();
        mainPanel.add(scaleSlider, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rotationSlider = new JSlider();
        mainPanel.add(rotationSlider, new GridConstraints(2, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
