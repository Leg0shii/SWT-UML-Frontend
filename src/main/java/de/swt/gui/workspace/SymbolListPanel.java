package de.swt.gui.workspace;

import de.swt.gui.GUI;

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

    public SymbolListPanel(Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
        setupListeners();
        try {
            setupGUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupGUI() throws IOException {
        this.thumbsDownButton.setText("");
        this.clearButton.setText("");
        this.drawButton.setText("");
        this.thumbsUpButton.setText("");
        System.out.println("URL: " + getClass().getResource("src/main/java/resources/like.png"));
        /*
        BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResource("resources/like.png")));
        BufferedImage ret = new BufferedImage(32,32,BufferedImage.TYPE_INT_RGB);
        ret.getGraphics().drawImage(image,0,0,32,32,null);
        this.thumbsUpButton.setIcon(new ImageIcon(ret));
        */
    }

    public void updateGUI() {

    }

    private void setupListeners() {

    }

    private void initForAccountType() {

    }
}
