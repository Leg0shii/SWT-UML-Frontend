package de.swt;

import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;
import de.swt.gui.GUIManager;
import de.swt.logic.user.User;
import de.swt.util.AccountType;
import de.swt.util.Client;
import de.swt.util.Language;

import javax.swing.*;
import java.util.ArrayList;

public class Application {
    Language language;
    Client client;
    GUIManager guiManager;

    public Application(Language language){
        try {
            UIManager.setLookAndFeel(new FlatSolarizedLightIJTheme());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.language = language;
        guiManager = new GUIManager(language);
        guiManager.setupGUIS();
        onStart();
        this.client = new Client();
        client.guiManager = guiManager;
        client.onStart();
        guiManager.updateGUIManager(client);
    }

    private void onStart(){
        guiManager.switchToLoginGUI();
    }
}
