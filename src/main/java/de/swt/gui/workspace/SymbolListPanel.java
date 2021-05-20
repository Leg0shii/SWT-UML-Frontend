package de.swt.gui.workspace;

import de.swt.drawing.Stickmanbutton;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * <div>Thumbs Up made by <a href="https://www.flaticon.com/authors/pixel-perfect" title="Pixel perfect">Pixel perfect</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
 * <div>Thumbs Down made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
 * <div>Pencil made by <a href="https://www.flaticon.com/authors/prosymbols" title="Prosymbols">Prosymbols</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
 * <div>Trash bin made by <a href="https://www.flaticon.com/authors/bqlqn" title="bqlqn">bqlqn</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
 */

public class SymbolListPanel extends GUI {
    private JPanel mainPanel;
    private JButton thumbsUpButton;
    private JButton thumbsDownButton;
    private JButton drawButton;
    private JButton clearButton;
    private JPanel toolPanel;
    public JPanel symbolPanel;
    private int[] clickCounter;

    public SymbolListPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        colorComponents(this.getAllComponents(this, new ArrayList<>()), guiManager.colorScheme, 1);
        setupListeners();
        try {
            setupGUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupGUI() throws IOException {
        this.toolPanel.setLayout(new GridLayout(1,4));
        this.toolPanel.removeAll();
        this.thumbsUpButton.setText("");
        this.thumbsDownButton.setText("");
        this.drawButton.setText("");
        this.clearButton.setText("");
        BufferedImage image1 = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResource("like.png")));
        BufferedImage ret1 = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        ret1.getGraphics().drawImage(image1, 0, 0, 20, 20, null);
        this.thumbsUpButton.setIcon(new ImageIcon(ret1));
        BufferedImage image2 = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResource("thumb-down.png")));
        BufferedImage ret2 = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        ret2.getGraphics().drawImage(image2, 0, 0, 20, 20, null);
        this.thumbsDownButton.setIcon(new ImageIcon(ret2));
        BufferedImage image3 = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResource("pencil.png")));
        BufferedImage ret3 = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        ret3.getGraphics().drawImage(image3, 0, 0, 20, 20, null);
        this.drawButton.setIcon(new ImageIcon(ret3));
        BufferedImage image4 = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResource("trash.png")));
        BufferedImage ret4 = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        ret4.getGraphics().drawImage(image4, 0, 0, 20, 20, null);
        this.clearButton.setIcon(new ImageIcon(ret4));
        thumbsUpButton.setContentAreaFilled(false);
        thumbsUpButton.setFocusPainted(false);
        thumbsUpButton.setBorderPainted(true);
        thumbsDownButton.setContentAreaFilled(false);
        thumbsDownButton.setFocusPainted(false);
        thumbsDownButton.setBorderPainted(true);
        clearButton.setContentAreaFilled(false);
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(true);
        drawButton.setContentAreaFilled(true);
        drawButton.setFocusPainted(false);
        drawButton.setBorderPainted(true);
        drawButton.setPreferredSize(new Dimension(20,20));

        this.toolPanel.add(thumbsUpButton);
        this.toolPanel.add(thumbsDownButton);
        this.toolPanel.add(drawButton);
        this.toolPanel.add(clearButton);

        this.clickCounter = new int[1];

        //this.add(Stickmanbutton())
    }

    public void updateGUI() {

    }

    private void setupListeners() {
        thumbsUpButton.addActionListener(e1 -> drawThumbUp());
        thumbsDownButton.addActionListener(e2 -> drawThumbDown());
        drawButton.addActionListener(e3 -> {
            if (clickCounter[0] % 2 == 0) {
                enableDraw();
                drawButton.setBorder(BorderFactory.createLoweredBevelBorder());
            } else {
                disableDraw();
                drawButton.setBorder(BorderFactory.createRaisedBevelBorder());
            }
            clickCounter[0]++;
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

    }





}
