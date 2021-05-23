package de.swt.drawing.buttonGUIS;

import de.swt.drawing.objects.DrawableObject;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.util.Language;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class StandardGUI extends GUI {
    private JPanel mainPanel;
    private JTextField descriptionTextField;
    private JSlider scaleSlider;
    private JLabel descriptionLabel;
    private JLabel scaleLabel;
    private JLabel colorLabel;
    private JButton colorButton;
    private final JColorChooser colorChooser;
    private final DrawableObject associatedObject;
    private String description;
    private Color color;
    private double scale;

    public StandardGUI(GUIManager guiManager, DrawableObject associatedObject) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.associatedObject = associatedObject;
        colorChooser = new JColorChooser();

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Beschreibung", "Skalierung", "Farbe", "Farbe auswählen");
            case ENGLISH -> setupGUI("Description", "Scale", "Color", "Choose Color");
        }

        setupListeners();
        updateGUI();
    }

    private void setupGUI(String description, String scale, String color, String chooseColor) {
        this.descriptionLabel.setText(description);
        this.scaleLabel.setText(scale);
        this.colorLabel.setText(color);
        this.colorButton.setText(chooseColor);
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

        this.scaleSlider.setValue((int) (associatedObject.scale * 10));
        this.colorChooser.setColor(associatedObject.color);
        this.descriptionTextField.setText(associatedObject.description);
        this.descriptionTextField.setEditable(true);

        initPopups(1);
    }

    public void updateGUI() {
        this.description = associatedObject.description;
        this.color = associatedObject.color;
        this.scale = associatedObject.scale;
        this.descriptionTextField.setEditable(true);
        this.revalidate();
        this.mainPanel.revalidate();
    }

    private void setupListeners() {
        descriptionTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                associatedObject.updateComponent(descriptionTextField.getText(), scale, color);
                updateGUI();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                associatedObject.updateComponent(descriptionTextField.getText(), scale, color);
                updateGUI();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                associatedObject.updateComponent(descriptionTextField.getText(), scale, color);
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
            associatedObject.updateComponent(description, convertSliderValue(scaleSlider.getValue()), color);
            updateGUI();
        });
        colorChooser.getSelectionModel().addChangeListener(e -> {
            associatedObject.updateComponent(description, scale, colorChooser.getColor());
            updateGUI();
        });
    }

    private double convertSliderValue(int val) {
        return ((double) val) / 10;
    }
}
