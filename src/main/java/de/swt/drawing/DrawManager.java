package de.swt.drawing;

import de.swt.gui.GUIManager;
import de.swt.gui.workspace.DrawablePanel;
import de.swt.gui.workspace.WorkspaceGUI;
import de.swt.util.AccountType;
import de.swt.util.Client;
import de.swt.util.Language;

import javax.swing.*;
import java.awt.*;

public class DrawManager {

    public static void main(String[] args) throws InterruptedException {

        JFrame drawing = new JFrame("DRAWING");

        Color[] colors = new Color[4];
        colors[0] = Color.decode("#EDEAE5"); // Background
        colors[1] = Color.decode("#FEF9C7"); // Second Background
        colors[2] = Color.decode("#000000"); // Text
        colors[3] = Color.decode("#9FEDD7"); // Button Background

        drawing.setLocationRelativeTo(null);
        drawing.setSize(new Dimension(500,500));
        drawing.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        drawing.setVisible(true);
        WorkspaceGUI wgui = new WorkspaceGUI(new GUIManager(colors, Language.GERMAN, AccountType.TEACHER));
        drawing.add(wgui);



    }







}
