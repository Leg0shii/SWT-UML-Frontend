package de.swt.gui.workspace;

import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.logic.user.User;

import javax.swing.*;

public class RequestPanel extends GUI {
    private JPanel mainPanel;
    public JButton acceptButton;
    public JButton denyButton;
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
        String[] split = requestLabel.getText().split(" ");
        this.requestLabel.setText(user.getFullName() + " " + split[1] + " " + split[2]);
    }

    private void setupListeners() {

    }

    private void initForAccountType() {

    }

    // TODO: Implemented by other Group
    public void acceptFunction() {

    }

    // TODO: Implemented by other Group
    public void denyFunction() {

    }
}
