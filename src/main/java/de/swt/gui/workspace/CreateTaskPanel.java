package de.swt.gui.workspace;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.swt.gui.GUI;
import de.swt.gui.GUIManager;
import de.swt.util.Language;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CreateTaskPanel extends GUI {
    private JPanel mainPanel;
    private JTextArea taskTextArea;
    private JPanel filePanel;
    private JButton selectFileButton;
    private JLabel headerLabel;
    private JLabel pictureLabel;
    private JLabel selectedFileLabel;
    public JButton cancelButton;
    public JButton createButton;
    public JScrollPane taskScrollPanel;
    private File selectedFile;

    public CreateTaskPanel(GUIManager guiManager) {
        super(guiManager);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(mainPanel);
        this.filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));
        this.taskScrollPanel.setViewportView(taskTextArea);

        switch (guiManager.language) {
            case GERMAN -> setupGUI("Aufgabenstellung", "Bild", "Erstellen", "Abbrechen");
            case ENGLISH -> setupGUI("Task", "Picture", "Create", "Cancel");
        }

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
        this.selectFileButton.addActionListener(e -> fileChooserFunction());
    }

    private void initForAccountType() {

    }

    private void fileChooserFunction() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF, PNG, JPG & GIF", "pdf", "png", "jpg", "gif");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.selectedFile = fileChooser.getSelectedFile();
            this.selectedFileLabel.setText(selectedFile.getName().substring(0, 10) + "... " + selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".")));
        }
    }

    public String getTaskText() {
        return taskTextArea.getText();
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void createFunction() {
        String task = getTaskText();
        try {
            byte[] workspaceBytes = FileUtils.readFileToByteArray(getSelectedFile());
            byte[] taskBytes = task.getBytes(StandardCharsets.UTF_8);
            guiManager.getClient().server.sendTask(workspaceBytes, taskBytes, guiManager.getClient().userid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(4, 7, new Insets(5, 5, 5, 5), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        headerLabel = new JLabel();
        headerLabel.setHorizontalAlignment(0);
        headerLabel.setText("Label");
        mainPanel.add(headerLabel, new GridConstraints(0, 0, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pictureLabel = new JLabel();
        pictureLabel.setText("Label");
        mainPanel.add(pictureLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filePanel = new JPanel();
        filePanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(filePanel, new GridConstraints(2, 1, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        selectedFileLabel = new JLabel();
        selectedFileLabel.setText("Label");
        filePanel.add(selectedFileLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        selectFileButton = new JButton();
        selectFileButton.setText("Button");
        filePanel.add(selectFileButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Button");
        mainPanel.add(cancelButton, new GridConstraints(3, 4, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        createButton = new JButton();
        createButton.setText("Button");
        mainPanel.add(createButton, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        taskScrollPanel = new JScrollPane();
        mainPanel.add(taskScrollPanel, new GridConstraints(1, 0, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        taskTextArea = new JTextArea();
        taskTextArea.setInheritsPopupMenu(false);
        taskTextArea.putClientProperty("html.disable", Boolean.FALSE);
        taskScrollPanel.setViewportView(taskTextArea);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
