package de.swt;

import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;
import de.swt.gui.GUIManager;
import de.swt.util.Client;
import de.swt.util.Language;
import javax.swing.*;

public class Application {
    private final GUIManager guiManager;

    public Application(Language language){
        try {
            UIManager.setLookAndFeel(new FlatSolarizedLightIJTheme());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        guiManager = new GUIManager(language);
        guiManager.setupGUIS();
        guiManager.switchToLoginGUI();
        new Thread(() -> {
            Client client = new Client();
            client.setGuiManager(guiManager);
            client.onStart();
            guiManager.setClient(client);
        }).start();
    }
}
