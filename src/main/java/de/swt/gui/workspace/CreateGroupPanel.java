package de.swt.gui.workspace;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.group.Group;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class CreateGroupPanel extends GUI {
    public JPanel mainPanel;
    private JTextField numberTextField;
    private JTextField sizeTextField;
    private JTextField durationTextField;
    public JButton createButton;
    private JLabel headerLabel;
    private JLabel numberLabel;
    private JLabel sizeLabel;
    private JLabel durationLabel;
    private JLabel unitLabel;

    public CreateGroupPanel(GUIManager guiManager) {
        super(guiManager);
        this.add(mainPanel);
        switch (guiManager.getLanguage()) {
            case GERMAN -> setupGUI("Gruppen erstellen", "Anzahl", "Gruppengröße", "Öffnungsdauer", "in Minuten", "Erstellen");
            case ENGLISH -> setupGUI("Create Groups", "Count", "Size", "Duration", "in minutes", "Create");
        }
    }

    private void setupGUI(String header, String number, String size, String duration, String unit, String create) {
        this.headerLabel.setText(header);
        this.numberLabel.setText(number);
        this.sizeLabel.setText(size);
        this.durationLabel.setText(duration);
        this.unitLabel.setText(unit);
        this.createButton.setText(create);
    }

    public void updateGUI() {
        revalidate();
    }

    @Override
    public void setupListeners() {
        this.createButton.addActionListener(e -> createFunction());
    }

    public int getNumberText() {
        return Integer.parseInt(numberTextField.getText());
    }

    public int getSizeText() {
        return Integer.parseInt(sizeTextField.getText());
    }

    public int getDurationText() {
        return Integer.parseInt(durationTextField.getText());
    }

    private void createFunction() {
        for (int i = 0; i < getNumberText(); i++) {
            Group group = new Group();
            group.setSessionId(getGuiManager().getClient().getCurrentSession().getSessionId());
            group.setTimeTillTermination(System.currentTimeMillis() + (getDurationText() * 60000L));
            group.setMaxGroupSize(getSizeText());
            try {
                getGuiManager().getClient().getServer().updateGroup(group);
                createButton.setBackground(UIManager.getColor("JButton"));
            } catch (RemoteException e) {
                createButton.setBackground(Color.RED);
            }
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
        mainPanel.setLayout(new GridLayoutManager(5, 5, new Insets(5, 5, 5, 5), -1, -1));
        headerLabel = new JLabel();
        headerLabel.setHorizontalAlignment(0);
        headerLabel.setText("Label");
        mainPanel.add(headerLabel, new GridConstraints(0, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numberLabel = new JLabel();
        numberLabel.setText("Label");
        mainPanel.add(numberLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sizeLabel = new JLabel();
        sizeLabel.setText("Label");
        mainPanel.add(sizeLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        durationLabel = new JLabel();
        durationLabel.setText("Label");
        mainPanel.add(durationLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numberTextField = new JTextField();
        numberTextField.setEditable(true);
        mainPanel.add(numberTextField, new GridConstraints(1, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        sizeTextField = new JTextField();
        sizeTextField.setEditable(true);
        mainPanel.add(sizeTextField, new GridConstraints(2, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        durationTextField = new JTextField();
        durationTextField.setEditable(true);
        mainPanel.add(durationTextField, new GridConstraints(3, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        unitLabel = new JLabel();
        unitLabel.setText("Label");
        mainPanel.add(unitLabel, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        createButton = new JButton();
        createButton.setText("Button");
        mainPanel.add(createButton, new GridConstraints(4, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numberLabel.setLabelFor(numberTextField);
        sizeLabel.setLabelFor(sizeTextField);
        durationLabel.setLabelFor(durationTextField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
