package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import utils.PdfWriter;
import view.MainView;

public class PdfButtonListener implements ActionListener {

	private MainView mainView;
	
	public PdfButtonListener(MainView mainView) {
		this.mainView = mainView;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		JFileChooser chooser;
        
        String dirpPfad = System.getProperty("user.home");
     
        chooser = new JFileChooser(dirpPfad);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
          
        chooser.setDialogTitle("Speichern unter...");
        chooser.setSelectedFile(new File(mainView.getTitle() + ".pdf"));
        chooser.setVisible(true);

        int result = chooser.showSaveDialog(mainView);

        if (result == JFileChooser.APPROVE_OPTION) {

            String filePfad = chooser.getSelectedFile().toString();
            
            try {
				PdfWriter.writePdf(this.mainView.getModell(), filePfad);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            chooser.setVisible(false);
            
        }
        
        chooser.setVisible(false);
        
   	} 		
	
}
