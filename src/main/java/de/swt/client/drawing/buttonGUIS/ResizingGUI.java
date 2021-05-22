package de.swt.client.drawing.buttonGUIS;

import de.swt.client.drawing.objects.ResizableObject;
import de.swt.client.gui.GUI;
import de.swt.client.gui.GUIManager;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ResizingGUI extends GUI {
    private JPanel mainPanel;
    private JButton colorButton;
    private JSlider scaleSlider;
    private JSlider widthSlider;
    private JSlider heightSlider;
    private JTextField descriptionTextField;
    private JLabel descriptionLabel;
    private JLabel scaleLabel;
    private JLabel widthLabel;
    private JLabel heightLabel;
    private JLabel colorLabel;
    private final JColorChooser colorChooser;
    private final ResizableObject associatedObject;
    private String description;
    private Color color;
    private double scale;
    public int height;
    public int width;

    public ResizingGUI(GUIManager guiManager, ResizableObject associatedObject) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.associatedObject = associatedObject;
        colorChooser = new JColorChooser();

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Beschreibung", "Skalierung", "Farbe", "Farbe auswählen", "Breite", "Höhe");
            case ENGLISH -> setupGUI("Description", "Scale", "Color", "Choose Color", "Width", "Height");
        }

        setupListeners();
        updateGUI();
    }

    private void setupGUI(String description, String scale, String color, String chooseColor, String width, String height) {
        this.descriptionLabel.setText(description);
        this.scaleLabel.setText(scale);
        this.colorLabel.setText(color);
        this.colorButton.setText(chooseColor);
        this.widthLabel.setText(width);
        this.heightLabel.setText(height);
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

        this.widthSlider.setMinimum(0);
        this.widthSlider.setMaximum(guiManager.getWidth() / 2);
        this.widthSlider.setMinorTickSpacing(1);
        this.widthSlider.setPaintLabels(true);

        this.heightSlider.setMinimum(0);
        this.heightSlider.setMaximum(3 * guiManager.getHeight() / 4);
        this.heightSlider.setMinorTickSpacing(1);
        this.heightSlider.setPaintLabels(true);

        this.scaleSlider.setValue((int) (associatedObject.scale * 10));
        this.widthSlider.setValue(associatedObject.width);
        this.heightSlider.setValue(associatedObject.height);
        this.colorChooser.setColor(associatedObject.color);
        this.descriptionTextField.setText(associatedObject.description);
        this.descriptionTextField.setEditable(true);

        initPopups(1);
    }

    public void updateGUI() {
        this.description = associatedObject.description;
        this.color = associatedObject.color;
        this.scale = associatedObject.scale;
        this.width = associatedObject.width;
        this.height = associatedObject.height;
        this.descriptionTextField.setEditable(true);
        this.revalidate();
        this.mainPanel.revalidate();
    }

    private void setupListeners() {
        descriptionTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                associatedObject.updateComponent(descriptionTextField.getText(), scale, color, width, height);
                updateGUI();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                associatedObject.updateComponent(descriptionTextField.getText(), scale, color, width, height);
                updateGUI();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                associatedObject.updateComponent(descriptionTextField.getText(), scale, color, width, height);
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
            associatedObject.updateComponent(description, convertSliderValue(scaleSlider.getValue()), color, width, height);
            updateGUI();
        });
        colorChooser.getSelectionModel().addChangeListener(e -> {
            associatedObject.updateComponent(description, scale, colorChooser.getColor(), width, height);
            updateGUI();
        });
        widthSlider.addChangeListener(e -> {
            associatedObject.updateComponent(description, scale, color, widthSlider.getValue(), height);
            updateGUI();
        });
        heightSlider.addChangeListener(e -> {
            associatedObject.updateComponent(description, scale, color, width, heightSlider.getValue());
            updateGUI();
        });
    }

    private double convertSliderValue(int val) {
        return ((double) val) / 10;
    }
}
