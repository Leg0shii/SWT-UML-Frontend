package de.swt.drawing.buttonGUIS;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.swt.drawing.objects.ResizableObject;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.util.Language;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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
        this.add(mainPanel);
        this.associatedObject = associatedObject;
        colorChooser = new JColorChooser();

        switch (guiManager.getLanguage()) {
            case GERMAN -> setupGUI("Beschreibung", "Liniendicke", "Farbe", "Farbe auswählen", "Breite", "Höhe");
            case ENGLISH -> setupGUI("Description", "Line Thickness", "Color", "Choose Color", "Width", "Height");
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

        this.widthSlider.setMinimum(10);
        this.widthSlider.setMaximum(getGuiManager().getWidth() / 2);
        this.widthSlider.setMinorTickSpacing(1);
        this.widthSlider.setPaintLabels(true);

        this.heightSlider.setMinimum(10);
        this.heightSlider.setMaximum(3 * getGuiManager().getHeight() / 4);
        this.heightSlider.setMinorTickSpacing(1);
        this.heightSlider.setPaintLabels(true);

        this.scaleSlider.setValue((int) (associatedObject.scale * 10));
        this.widthSlider.setValue(associatedObject.width);
        this.heightSlider.setValue(associatedObject.height);
        this.colorChooser.setColor(associatedObject.color);
        this.descriptionTextField.setText(associatedObject.description);
        this.descriptionTextField.setEditable(true);
    }

    @Override
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

    @Override
    public void setupListeners() {
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

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(colorChooser);
        colorButton.setComponentPopupMenu(popupMenu);
        popupMenu.setVisible(true);
        popupMenu.setVisible(false);
        colorButton.addActionListener(e -> colorButton.getComponentPopupMenu().show(associatedObject, associatedObject.getWidth(), associatedObject.getHeight() / 2 - popupMenu.getHeight() / 2));

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
        widthLabel = new JLabel();
        widthLabel.setText("Label");
        mainPanel.add(widthLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        heightLabel = new JLabel();
        heightLabel.setText("Label");
        mainPanel.add(heightLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        colorLabel = new JLabel();
        colorLabel.setText("Label");
        mainPanel.add(colorLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        colorButton = new JButton();
        colorButton.setText("Button");
        mainPanel.add(colorButton, new GridConstraints(4, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scaleSlider = new JSlider();
        mainPanel.add(scaleSlider, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        widthSlider = new JSlider();
        mainPanel.add(widthSlider, new GridConstraints(2, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        heightSlider = new JSlider();
        mainPanel.add(heightSlider, new GridConstraints(3, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        descriptionTextField = new JTextField();
        mainPanel.add(descriptionTextField, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
