package de.swt.drawing.buttonGUIS;

import de.swt.drawing.objects.RotatableObject;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;

import javax.swing.*;
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
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.associatedObject = associatedObject;
        colorChooser = new JColorChooser();

        switch (guiManager.language) {
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

        this.rotationSlider.setMinimum((int) (-100*associatedObject.scale));
        this.rotationSlider.setMaximum((int) (100*associatedObject.scale));
        this.rotationSlider.setMinorTickSpacing(1);
        this.rotationSlider.setPaintLabels(true);

        this.scaleSlider.setValue((int) (associatedObject.scale * 10));
        this.rotationSlider.setValue(associatedObject.endYOffset - associatedObject.startYOffset);
        this.arrowHeadOrientationCheckBox.setSelected(associatedObject.switchSides);
        this.colorChooser.setColor(associatedObject.color);
        this.descriptionTextField.setText(associatedObject.description);
        this.descriptionTextField.setEditable(true);

        initPopups(1);
    }

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

    private void setupListeners() {
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
        colorButton.addActionListener(e -> {
            if (popupCounter.get(0) % 2 == 0) {
                Point point = new Point(getX() + getWidth(), getY());
                SwingUtilities.convertPointToScreen(point, getParent());
                popups.set(0, factory.getPopup(guiManager, colorChooser, point.x, point.y));
                popups.get(0).show();
            } else {
                popups.get(0).hide();
            }
            incrementPopupCounter(0);
        });
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
                associatedObject.updateComponent(description, scale, color, startYOffset, (int) (rotationSlider.getValue()*scale), switchSides);
                updateGUI();
            } else {
                associatedObject.updateComponent(description, scale, color, (int) abs(rotationSlider.getValue()*scale), endYOffset, switchSides);
                updateGUI();
            }
        });
        arrowHeadOrientationCheckBox.addChangeListener(e -> {
            associatedObject.updateComponent(description, scale, color, startYOffset, endYOffset, arrowHeadOrientationCheckBox.isSelected());
            updateGUI();
        });
    }

    private double convertSliderValue(int val) {
        return ((double) val) / 10;
    }
}
