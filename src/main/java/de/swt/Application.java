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
    AccountType accountType;
    Language language;
    Client client;
    GUIManager guiManager;

    public Application(AccountType accountType, Language language){
        try {
            UIManager.setLookAndFeel(new FlatSolarizedLightIJTheme());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.accountType = accountType;
        this.language = language;
        guiManager = new GUIManager(language, accountType);
        guiManager.setupGUIS();
        onStart();
        this.client = new Client();
        client.onStart();
        client.guiManager = guiManager;
        guiManager.updateGUIManager(client);
        guiManager.updateGUIS(new ArrayList<>(guiManager.getClient().userManager.getUserHashMap().values()));
    }

    private void onStart(){
        guiManager.switchToLoginGUI();
    }
}
