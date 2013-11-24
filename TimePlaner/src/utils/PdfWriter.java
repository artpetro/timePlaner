package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import view.MainView;

import model.DayPlanModel;
import model.WeekPlanModel;
import model.WorkingLayerModel;

import com.google.gson.Gson;
import com.pdfjet.Box;
import com.pdfjet.Font;
import com.pdfjet.Letter;
import com.pdfjet.Line;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.TextLine;

public class PdfWriter {

	public static void writePdf(WeekPlanModel model, String path) throws Exception {
		
		int[] se = model.getStartEndPeriod();

		if (se[2] > 0) {
			
			FileOutputStream fos = new FileOutputStream(path);

	        PDF pdf = new PDF(fos);

	        // widht 590 height 800
	        Page page = new Page(pdf, Letter.PORTRAIT);
	        
	        Box mainBox = new Box();
	        mainBox.setPosition(60.0f, 40.0f);
	        // TODO height depends layers.length
	        float boxWidht = 490.0f;
	        float boxHeight = 710.0f;
	        mainBox.setSize(boxWidht, boxHeight);
	        
	        mainBox.drawOn(page);
	        
	        // title
	        int titleSize = 12;
	        String title = model.getTitle();
	        
	        Font titleFont = new Font(pdf, "Helvetica-Bold");
	        titleFont.setSize(titleSize);
	        TextLine titleLine = new TextLine(titleFont);
	        titleLine.placeIn(mainBox);
	        titleLine.setPosition((boxWidht - (title.length() * titleSize/2))/2, 20);
	        
	        titleLine.setText(title);
	        titleLine.drawOn(page);
	        
	        // time lines
	        // start later as and => new day
	        if(model.isNewDay()) {
	        	se[1] += 24 * 60;
	        }   
	        int hoursCount = Math.abs(se[1] - se[0]) / 60;
	        
	        if (Math.abs(se[1] - se[0]) % 60 > 0) {
	        	hoursCount++;
	        }
	        
	        float x_linesStart = 40;
	        float x_linesEnd = 60;
	        
	        float hourLength = (boxWidht - x_linesStart - x_linesEnd) / hoursCount;
	        
	        int hour = se[0] / 60;
	        int startHour = hour;
	        
	        System.out.println(se[0]);
	        
	        float y_linesStart = 80.0f;
	        float y_linesEnd = boxHeight - 20;
	        
	        for (int i = 0; i < hoursCount + 1; i++) {
	        	
	        	float x_pos = hourLength * i + x_linesStart;
	        	float hourSize = 10.0f;
	        	Font hourFont = new Font(pdf, "Helvetica");
		        String hourText = (hour++) % 24 + "";
	        	hourFont.setSize(hourSize);
		        TextLine hourLine = new TextLine(hourFont);
		        hourLine.placeIn(mainBox);
		        
		        hourLine.setPosition(x_pos - (hourText.length() * hourSize/3), y_linesStart - hourSize/2);
		        
		        hourLine.setText(hourText);
		        hourLine.drawOn(page);
	        	
	        	
	        			
	        	Line line = new Line(x_pos, y_linesStart, x_pos, y_linesEnd);
	        	line.placeIn(mainBox);
	        	line.drawOn(page);
	        }
	        
	        // working layers
	        float vertikalPadding = 5.0f; // between layers
	        float maxLayerBoxHeight = 20.0f;
	        float layerBoxHeight = Math.min(maxLayerBoxHeight, ((y_linesEnd - y_linesStart) - (se[2] + 1) * vertikalPadding) / se[2]);

	        DayPlanModel[] daysArr = model.getDays();
	        DayPlanModel day;
	        LinkedList<WorkingLayerModel> layers;
	        Box layerBox;
	        float layerBoxLength;
	        int layerCount = 0;

			int[] white = {255, 255, 255};
			int[] black = {0, 0, 0};
	        
			String dayTitle;
			Font dayTitleFont;
			TextLine dayTitleLine;
			
			String timeTitle;
			Font timeTitleFont;
			TextLine timeTitleLine;
			
			float y_layerTitle;
			int timeStart;
			int timeEnd;
			
	        for (int i = 0; i < daysArr.length; i ++) {
	        	
	        	day = daysArr[i];
	        	layers = day.getLayers();
	        	
	        	if (!layers.isEmpty()) {
	        		
		        	// day label
	        		int dayTitleSize = 12;
	        		y_layerTitle = (layerBoxHeight + vertikalPadding) * layerCount + y_linesStart + vertikalPadding * 2 + layerBoxHeight / 2;
	    	        dayTitle = MainView.days[i].substring(0, 2);
	    	        dayTitleFont = new Font(pdf, "Helvetica");
	    	        dayTitleFont.setSize(dayTitleSize);
	    	        dayTitleLine = new TextLine(dayTitleFont);
	    	        dayTitleLine.placeIn(mainBox);
	    	        dayTitleLine.setPosition(x_linesStart/2, y_layerTitle);
	    	        
	    	        dayTitleLine.setText(dayTitle);
	    	        dayTitleLine.drawOn(page);
		
	    	        // layer box
	        		for (WorkingLayerModel wlm : layers) {
	        			
	        			layerBox = new Box();
	        			layerBoxLength = wlm.getTimeCountInMins() * hourLength / 60;
	        			layerBox.setSize(layerBoxLength, layerBoxHeight);
	        			
	        			layerBox.placeIn(mainBox, ((wlm.getStart() / 60 - startHour) * hourLength) + x_linesStart, (layerBoxHeight + vertikalPadding) * layerCount + y_linesStart + vertikalPadding);
	        			
	        			layerBox.setColor(white);
	        			layerBox.setFillShape(true);
	        			layerBox.drawOn(page);
	        			
	        			layerBox.setColor(black);
	        			layerBox.setFillShape(false);
	        			layerBox.drawOn(page);
	        			
	        			// layer-time labels
		        		int timeTitleSize = 10;
		        		y_layerTitle = (layerBoxHeight + vertikalPadding) * layerCount + y_linesStart + vertikalPadding * 2 + layerBoxHeight / 2;
		        		timeStart = wlm.getStart();
		        		timeEnd = wlm.getEnd();
		    	        timeTitle = String.format("%d:%02d - %d:%02d", timeStart / 60, timeStart % 60, timeEnd / 60, timeEnd % 60);
		    	        timeTitleFont = new Font(pdf, "Helvetica");
		    	        timeTitleFont.setSize(timeTitleSize);
		    	        timeTitleLine = new TextLine(timeTitleFont);
		    	        timeTitleLine.placeIn(mainBox);
		    	        timeTitleLine.setPosition(boxWidht - x_linesEnd + 5, y_layerTitle);
		    	        
		    	        timeTitleLine.setText(timeTitle);
		    	        timeTitleLine.drawOn(page);
		    	        
		    	        // name labels
		    	        timeTitle = wlm.getText();
		    	        timeTitleFont = new Font(pdf, "Helvetica");
		    	        timeTitleFont.setSize(timeTitleSize);
		    	        timeTitleLine = new TextLine(timeTitleFont);
		    	        timeTitleLine.placeIn(layerBox);
		    	        timeTitleLine.setPosition((layerBoxLength - (timeTitle.length() * timeTitleSize/2))/2, layerBoxHeight - timeTitleSize / 2);
		    	        
		    	        timeTitleLine.setText(timeTitle);
		    	        timeTitleLine.drawOn(page);
	        			
	        			
	        			
	        			layerCount++;
	        			
	        		}
	        	}
	        }
	        
	        pdf.flush();
	        fos.close();
	        
	        Runtime rt = Runtime.getRuntime();
        	try{
        		@SuppressWarnings("unused")
        		Process p = rt.exec( "rundll32" +" " + "url.dll,FileProtocolHandler" + " " + path);
        	}catch (Exception e1){
        		e1.printStackTrace();
        	}
		}
	}
	
	public static void main(String[] args) {
		
		File file = new File(System.getProperty("user.home")+"\\Schichtpan für 01.01.2013.json");
       
        BufferedReader br = null;
        Gson gson = new Gson();
        WeekPlanModel wpm = null;
        
		try {

			br = new BufferedReader(new FileReader(file.getAbsoluteFile()));

			wpm = gson.fromJson(br, WeekPlanModel.class);
			 
		} catch (IOException e) {
			e.printStackTrace();
    	} finally {
    		try {
    			if (br != null)br.close();
    		} catch (IOException ex) {
    			ex.printStackTrace();
    		}
    	}
		
		
		try {
			writePdf(wpm, "Example_01.pdf");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
