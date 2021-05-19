package de.swt.drawing;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;


public class MyFrame extends JFrame{


    private JTextPane textArea;


    MyFrame(){
		
		this.setTitle("Workplace");
		this.setSize(1920,1080);
		this.setLayout(null);
		
		MakeContentPane();
	    MakeMenubar();

	    this.pack();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
		
	
	public void MakeMenubar(){
		JMenuBar menuBar = new JMenuBar();

		//File-Menu
		JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener((e) -> newMyFrame());
        
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener((e) -> save());
        
        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.addActionListener((e) -> saveAs());
        
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener((e) -> open());
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener((e) -> exit(1));
        
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(exitItem);

        //Edit-Menu
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
        
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.addActionListener((e) -> undo());
        
        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.addActionListener((e) -> redo());
        
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener((e) -> open());

		editMenu.add(deleteItem);
		editMenu.add(redoItem);
		editMenu.add(undoItem);
		
		
        Action action = new StyledEditorKit.BoldAction();
        action.putValue(Action.NAME, "Bold");
        editMenu.add(action);

        this.setJMenuBar(menuBar);
		this.setVisible(true);
		
	}

	
	public void MakeContentPane() {
        //textArea = new JTextArea();
        this.textArea = new JTextPane();

        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        Container contentPane = this.getContentPane();

        contentPane.setLayout(new BorderLayout());
        //contentPane.add(textArea, BorderLayout.CENTER);
        contentPane.add(scrollPane);
        
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JButton boldItem = new JButton("Bold"); //TODO Create action
        JButton italicItem = new JButton("Italic"); //TODO Create action
        JButton underLineItem = new JButton("Underline"); //TODO Create action

        JPanel buttons = new JPanel();
        //buttons.setLayout(new GridLayout(1,3));
        buttons.add(boldItem);
        buttons.add(italicItem);
        buttons.add(underLineItem);

        contentPane.add(buttons, BorderLayout.NORTH);
        
    }
	
	

	public void newMyFrame() {
		new MyFrame();
	}
	
	public void save() {
		
        final JFileChooser Save = new JFileChooser();
        Save.setApproveButtonText("Save");
        int actionDialog = Save.showSaveDialog(this);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileName = new File(Save.getSelectedFile() + ".txt");

        outFilefunc(fileName);
    }

    private void outFilefunc(File fileName) {
        BufferedWriter outFile = null;

        try {
            outFile = new BufferedWriter(new FileWriter(fileName));

            textArea.write(outFile);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void saveAs() {

        final JFileChooser SaveAs = new JFileChooser();
        
        
        SaveAs.setDialogType(JFileChooser.SAVE_DIALOG);
        SaveAs.setFileFilter(new FileNameExtensionFilter("Extensible Markup Language File (*xml)","xml"));
        SaveAs.setFileFilter(new FileNameExtensionFilter("Text File (*.txt)","txt"));
        SaveAs.setFileFilter(new FileNameExtensionFilter("Portable Document Format File (*pdf)","pdf"));
        SaveAs.setFileFilter(new FileNameExtensionFilter("Hypertext Markup Language File (*html)","html"));
        
        SaveAs.setApproveButtonText("Save As");
        int actionDialog = SaveAs.showSaveDialog(this);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileName = new File(SaveAs.getSelectedFile() + ".txt");

        outFilefunc(fileName);
    }
	
    public void open() {
    	final JFileChooser Open = new JFileChooser();
        
    	Open.setApproveButtonText("Open");
        int actionDialog = Open.showOpenDialog(this);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }
        //TODO
    }

	
    public void exit(int status) {
    	System.exit(status);
    }
    
    public void undo() {
    	
    }
 
    public void redo() {
 	
    }
 
    public void delete() {
 	
    }
}