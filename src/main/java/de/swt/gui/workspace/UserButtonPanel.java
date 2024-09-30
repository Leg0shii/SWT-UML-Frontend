package de.swt.gui.workspace;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.user.User;
import de.swt.util.Language;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.rmi.RemoteException;

@Setter
public class UserButtonPanel extends GUI {
    private JPanel mainPanel;
    private JButton kickButton;
    private User user;

    public UserButtonPanel(GUIManager guiManager) {
        super(guiManager);
        this.add(mainPanel);

        switch (guiManager.getLanguage()) {
            case GERMAN -> setupGUI("Kicken");
            case ENGLISH -> setupGUI("Kick");
        }

    }

    private void setupGUI(String kick) {
        this.kickButton.setText(kick);
    }

    public void updateGUI() {

    }

    public void setupListeners() {
        kickButton.addActionListener(e -> kickFunction());
    }

    private void kickFunction() {
        getGuiManager().getClient().getCurrentSession().getUserIds().remove((Integer) user.getUserId());
        try {
            getGuiManager().getClient().getServer().updateSession(getGuiManager().getClient().getCurrentSession());
            kickButton.setBackground(UIManager.getColor("JButton"));
        } catch (RemoteException e) {
            kickButton.setBackground(Color.RED);
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
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        kickButton = new JButton();
        kickButton.setText("Button");
        mainPanel.add(kickButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
