package view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

import model.WeekPlanModel;

import com.google.gson.Gson;

public class JsonIO {
	
	public static boolean saveAs(String dirpPfad, WeekPlanModel wpm, MainView mainView) {

        JFileChooser chooser;
        if (dirpPfad == null)
        	dirpPfad = System.getProperty("user.home");
        File file = new File(dirpPfad.trim());

        chooser = new JFileChooser(dirpPfad);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
          
        chooser.setDialogTitle("Speichern unter...");
        chooser.setSelectedFile(new File(mainView.getTitle() + ".json"));
        chooser.setVisible(true);

        int result = chooser.showSaveDialog(mainView);

        if (result == JFileChooser.APPROVE_OPTION) {

            String filePfad = chooser.getSelectedFile().toString();
            file = new File(filePfad);
            
            FileWriter fw;
			try {
				fw = new FileWriter(file.getAbsoluteFile());
			
 			 BufferedWriter bw = new BufferedWriter(fw);
 			
 			 Gson json = new Gson();
 			 
 			 bw.write(json.toJson(wpm));
 			 bw.close();
 			 
			} catch (IOException e) {
				e.printStackTrace();
			}

            chooser.setVisible(false);
            return true;
        }
        
        chooser.setVisible(false);
        return false;
    
	} 
	
	public static WeekPlanModel load(String dirpPfad, MainView mainView) {
		
		WeekPlanModel wpm = null;

        JFileChooser chooser;
        if (dirpPfad == null)
        	dirpPfad = System.getProperty("user.home");
        File file = new File(dirpPfad.trim());

        chooser = new JFileChooser(dirpPfad);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        
        chooser.setDialogTitle("Laden...");
        chooser.setVisible(true);

        int result = chooser.showOpenDialog(mainView);

        if (result == JFileChooser.APPROVE_OPTION) {

            String filePfad = chooser.getSelectedFile().toString();
            file = new File(filePfad);
            
            BufferedReader br = null;
            Gson gson = new Gson();
            
			try {

				br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
 
				wpm = gson.fromJson(br, WeekPlanModel.class);
 			 
			} catch (IOException e) {
				// TODO errDialog
				e.printStackTrace();
        	} finally {
			
        		try {
        			if (br != null)br.close();
        		} catch (IOException ex) {
        			ex.printStackTrace();
        		}
        	}

            chooser.setVisible(false);
            return wpm;
        }
        
        chooser.setVisible(false);
        return null;
    
	}
}
