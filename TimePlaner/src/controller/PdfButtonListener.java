package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import utils.PdfWriter;
import view.FileSaveChooser;
import view.MainView;

public class PdfButtonListener implements ActionListener {

	private MainView mainView;
	
	public PdfButtonListener(MainView mainView) {
		this.mainView = mainView;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		FileSaveChooser chooser = new FileSaveChooser(mainView, "pdf");
     
        int result = chooser.showSaveDialog(mainView);

        if (result == JFileChooser.APPROVE_OPTION) {

            try {
				PdfWriter.writePdf(this.mainView, chooser.getFile());
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this.mainView, "Die Datei " + chooser.getFile().getName() + " wird von einem Programm verwendet! Speichern nicht möglich.", "Fehler beim Speichern", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            chooser.setVisible(false);
            
        }
        
        chooser.setVisible(false);
        
   	} 		
	
}
