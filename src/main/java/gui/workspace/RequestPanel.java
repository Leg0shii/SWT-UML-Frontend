package gui.workspace;

import gui.GUI;
import logic.user.User;
import util.Language;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RequestPanel extends GUI {
    private JPanel mainPanel;
    public JButton acceptButton;
    public JButton denyButton;
    private JLabel requestLabel;

    public RequestPanel(Language language, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        switch (language) {
            case GERMAN -> setupGUI(" möchte beitreten", "Akzeptieren", "Verweigern");
            case ENGLISH -> setupGUI(" wants to join", "Accept", "Deny");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
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
