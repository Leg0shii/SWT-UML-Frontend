package de.swt.gui.workspace;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.user.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.rmi.RemoteException;

public class RequestPanel extends GUI {
    private JPanel mainPanel;
    public JButton acceptButton;
    public JButton denyButton;
    private User user;
    private JLabel requestLabel;

    public RequestPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        switch (guiManager.language) {
            case GERMAN -> setupGUI(" möchte beitreten", "Akzeptieren", "Verweigern");
            case ENGLISH -> setupGUI(" wants to join", "Accept", "Deny");
        }

        setupListeners();
    }

    private void setupGUI(String request, String accept, String deny) {
        this.requestLabel.setText(request);
        this.acceptButton.setText(accept);
        this.denyButton.setText(deny);
    }

    public void updateGUI(User user) {
        this.user = user;
        String[] split = requestLabel.getText().split(" ");
        this.requestLabel.setText(user.getFullName() + " " + split[1] + " " + split[2]);
    }

    private void setupListeners() {

    }

    private void initForAccountType() {

    }

    public void acceptFunction() {
        try {
            guiManager.getClient().server.sendAnswer(user.getId(), 1, guiManager.getClient().userid);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void denyFunction() {
        try {
            guiManager.getClient().server.sendAnswer(user.getId(), 0, guiManager.getClient().userid);
        } catch (RemoteException e) {
            e.printStackTrace();
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
        mainPanel.setLayout(new GridLayoutManager(2, 2, new Insets(5, 5, 5, 5), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        requestLabel = new JLabel();
        requestLabel.setHorizontalAlignment(0);
        requestLabel.setText("Label");
        mainPanel.add(requestLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        acceptButton = new JButton();
        acceptButton.setText("Button");
        mainPanel.add(acceptButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        denyButton = new JButton();
        denyButton.setText("Button");
        mainPanel.add(denyButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
