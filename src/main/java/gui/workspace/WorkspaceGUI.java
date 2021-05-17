package gui.workspace;

import gui.GUI;
import gui.GUIManager;

import javax.swing.*;

public class WorkspaceGUI extends GUI {
    private JPanel mainPanel;
    private GUIManager guiManager;

    public WorkspaceGUI(GUIManager guiManager){
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        this.guiManager = guiManager;
    }

    private void setupGUI(){

    }

    public void updateGUI(){

    }

    private void setupListeners(){

    }

    private void initForAccountType(){

    }
}
