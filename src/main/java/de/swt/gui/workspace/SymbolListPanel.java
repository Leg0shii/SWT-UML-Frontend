package de.swt.gui.workspace;

import com.formdev.flatlaf.icons.*;
import de.swt.drawing.buttons.*;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import org.kordamp.ikonli.IkonliIkonProvider;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignL;
import org.kordamp.ikonli.materialdesign2.MaterialDesignT;
import org.kordamp.ikonli.swing.FontIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * <div>Icons from <a href="https://kordamp.org/ikonli/cheat-sheet-fluentui.html" title="Kordamp">Kordamp</a></div>
*/

public class SymbolListPanel extends GUI {
    private JPanel mainPanel;
    private JButton thumbsUpButton;
    private JButton thumbsDownButton;
    private JToggleButton drawButton;
    private JButton clearButton;
    private JPanel toolPanel;
    public JPanel symbolPanel;

    public SymbolListPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        setupListeners();
        setupGUI();
    }

    private void setupGUI(){
        this.toolPanel.setLayout(new GridLayout(1, 4));
        this.toolPanel.removeAll();
        this.thumbsUpButton.setText("");
        this.thumbsDownButton.setText("");
        this.drawButton.setText("");
        this.clearButton.setText("");

        this.toolPanel.add(thumbsUpButton);
        this.toolPanel.add(thumbsDownButton);
        this.toolPanel.add(drawButton);
        this.toolPanel.add(clearButton);

        FontIcon icon1 = new FontIcon();
        icon1.setIkon(MaterialDesignT.THUMB_UP);
        icon1.setIconSize(25);
        thumbsUpButton.setIcon(icon1);
        FontIcon icon2 = new FontIcon();
        icon2.setIkon(MaterialDesignT.THUMB_DOWN);
        icon2.setIconSize(25);
        thumbsDownButton.setIcon(icon2);
        FontIcon icon3 = new FontIcon();
        icon3.setIkon(MaterialDesignL.LEAD_PENCIL);
        icon3.setIconSize(25);
        drawButton.setIcon(icon3);
        FontIcon icon4 = new FontIcon();
        icon4.setIkon(MaterialDesignT.TRASH_CAN);
        icon4.setIconSize(25);
        clearButton.setIcon(icon4);


        this.symbolPanel.setLayout(new FlowLayout());
        this.symbolPanel.add(new ActorButton(guiManager));
        this.symbolPanel.add(new UseCaseButton(guiManager));
        this.symbolPanel.add(new ArrowButton(guiManager));
        this.symbolPanel.add(new DottedArrowButton(guiManager));
        this.symbolPanel.add(new SystemBoxButton(guiManager));

        updateGUI();
    }

    public void updateGUI() {
        symbolPanel.setPreferredSize(new Dimension(3*guiManager.getWidth()/16, guiManager.getHeight()));
    }

    private void setupListeners() {
        thumbsUpButton.addActionListener(e1 -> drawThumbUp());
        thumbsDownButton.addActionListener(e2 -> drawThumbDown());
        drawButton.addActionListener(e3 -> {
            if (drawButton.isSelected()) {
                enableDraw();
            } else {
                disableDraw();
            }
        });
        clearButton.addActionListener(e4 -> deleteLastDrawnObject());
    }

    private void initForAccountType() {

    }

    private void drawThumbUp() {

    }

    private void drawThumbDown() {

    }

    private void enableDraw() {

    }

    private void disableDraw() {

    }

    private void deleteLastDrawnObject() {
        guiManager.removeLastDrawnObject();
    }


}
