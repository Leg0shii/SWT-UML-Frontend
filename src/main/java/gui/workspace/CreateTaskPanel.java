package gui.workspace;

import gui.GUI;
import util.Language;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class CreateTaskPanel extends GUI {
    private JPanel mainPanel;
    private JTextArea taskTextArea;
    private JPanel filePanel;
    private JButton selectFileButton;
    private JLabel headerLabel;
    private JLabel pictureLabel;
    private JLabel selectedFileLabel;
    private JButton cancelButton;
    private JButton createButton;
    private JScrollPane taskScrollPanel;
    private File selectedFile;

    public CreateTaskPanel(Language language, Color[] colors) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.filePanel.setLayout(new BoxLayout(filePanel,BoxLayout.X_AXIS));
        this.taskScrollPanel.setViewportView(taskTextArea);

        switch (language) {
            case GERMAN -> setupGUI("Aufgabenstellung", "Bild", "Erstellen", "Abbrechen");
            case ENGLISH -> setupGUI("Task", "Picture", "Create", "Cancel");
        }

        colorComponents(this.getAllComponents(this, new ArrayList<>()), colors, 1);
        setupListeners();

        this.selectedFile = null;
    }

    private void setupGUI(String header, String picture, String create, String cancel) {
        this.headerLabel.setText(header);
        this.pictureLabel.setText(picture);
        this.createButton.setText(create);
        this.cancelButton.setText(cancel);
        this.selectFileButton.setText("Browse...");
        this.selectedFileLabel.setText(" ");

    }

    public void updateGUI() {

    }

    // TODO: Cancel Button Listener in PopUp superclass
    private void setupListeners() {
        this.createButton.addActionListener(e -> createFunction());
        this.selectFileButton.addActionListener(e -> fileChooserFunction());
    }

    private void initForAccountType() {

    }

    private void fileChooserFunction(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF, PNG, JPG & GIF", "pdf", "png", "jpg", "gif");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            this.selectedFile = fileChooser.getSelectedFile();
            this.selectedFileLabel.setText(selectedFile.getName());
        }
    }

    public String getTaskText(){
        return taskTextArea.getText();
    }

    public File getSelectedFile(){
        return selectedFile;
    }

    // TODO: Implemented by other Group
    private void createFunction(){

    }

}
