package de.swt.gui.workspace;

import de.swt.gui.GUIManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class Menu extends JMenuBar {
    private GUIManager guiManager;
    private JMenu file;
    private JMenuItem saveFile;
    private JMenuItem loadFile;
    private JMenu navigate;
    private JMenuItem logoff;
    private JMenuItem toClassroomGUI;

    public Menu(GUIManager guiManager) {
        this.guiManager = guiManager;
        switch (guiManager.getLanguage()) {
            case GERMAN -> setupMenu("Datei", "Datei speichern", "Datei laden", "Zur Klassenraumübersicht", "Ausloggen");
            case ENGLISH -> setupMenu("File", "Save file", "Load file", "To Classrooms", "Log off");
        }
        setupListeners();
    }

    private void setupMenu(String file, String saveFile, String loadFile, String toClassroomGUI, String logoff) {
        this.file = new JMenu(file);
        this.add(this.file);
        this.saveFile = new JMenuItem(saveFile);
        this.loadFile = new JMenuItem(loadFile);
        this.file.add(this.saveFile);
        this.file.add(this.loadFile);
        this.navigate = new JMenu("Navigation");
        this.add(this.navigate);
        this.toClassroomGUI = new JMenuItem(toClassroomGUI);
        this.logoff = new JMenuItem(logoff);
        this.navigate.add(this.toClassroomGUI);
        this.navigate.add(this.logoff);
    }

    private void setupListeners() {
        saveFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showSaveDialog(guiManager);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".ser")) {
                    guiManager.saveWorkspace(new File(file + ".ser"));
                } else {
                    guiManager.saveWorkspace(file);
                }
            }
        });
        loadFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Serialized Object (*.ser)", "ser");
            fileChooser.setFileFilter(filter);
            int returnVal = fileChooser.showOpenDialog(guiManager);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                guiManager.loadWorkspace(file);
            }
        });
        toClassroomGUI.addActionListener(e -> guiManager.switchToClassRoomGUI());
        logoff.addActionListener(e -> guiManager.switchToLoginGUI());
    }
}
