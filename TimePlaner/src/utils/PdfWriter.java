package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;

import model.DayPlanModel;
import model.WeekPlanModel;
import model.WorkingLayerModel;
import view.MainView;

import com.pdfjet.Box;
import com.pdfjet.Font;
import com.pdfjet.Letter;
import com.pdfjet.Line;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.TextLine;

public class PdfWriter {

	public static void writePdf(MainView mainView, File file) throws FileNotFoundException, Exception {
		
		WeekPlanModel model = mainView.getModell();
		
		int[] se = model.getStartEndPeriod();

		if (se[2] > 0) {
			
			FileOutputStream fos = new FileOutputStream(file);

	        PDF pdf;
			
			pdf = new PDF(fos);
			

	        // widht 590 height 800
	        Page page = new Page(pdf, Letter.PORTRAIT);
	        
	        int[] white = {255, 255, 255};
			int[] black = {0, 0, 0};
	        
	        Box mainBox = new Box();
	        mainBox.setPosition(30.0f, 20.0f);
	        float boxWidht = 550.0f;
	        float boxHeight = 760.0f;
	        mainBox.setSize(boxWidht, boxHeight);
	        mainBox.setColor(white);
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
	        
	        System.out.println("start: "+se[0]);
	        System.out.println("end: "+se[1]);
	        System.out.println("layers:"+se[2]);	        
	        
	        int hoursCount = 24;
	      
	        if (se[1] <= se[0] || model.isNewDay()) {	    	   
	        	hoursCount = 24 - se[0] / 60 + se[1] / 60;	       
	        }
	       
	        else {	    	
	        	hoursCount = se[1] / 60 - se[0] / 60;	       
	        }       
	        if (se[1] % 60 > 0) {	    	
	        	hoursCount++;       
	        }
	        
	        System.out.println("hoursCount: " + hoursCount);
	        
	        float x_linesStart = 40;
	        float x_linesEnd = 60;
	        
	        float hourLength = (boxWidht - x_linesStart - x_linesEnd) / hoursCount;
	        
	        int hour = se[0] / 60;
	        int startHour = hour;
	        
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
	        			
	        			layerBox.placeIn(mainBox, ((wlm.getStart() / 60.0f - startHour) * hourLength) + x_linesStart, (layerBoxHeight + vertikalPadding) * layerCount + y_linesStart + vertikalPadding);
	        			
	        			layerBox.setColor(white);
	        			layerBox.setFillShape(true);
	        			layerBox.drawOn(page);
	        			
	        			layerBox.setColor(black);
	        			layerBox.setFillShape(false);
	        			layerBox.drawOn(page);
	        			
	        			// layer-time labels
		        		float timeTitleSize = 10.0f;
		        		if (layerBoxHeight <= timeTitleSize) {
		        			timeTitleSize = layerBoxHeight - 2.0f ;
		        		}
		        		y_layerTitle = (layerBoxHeight + vertikalPadding) * layerCount + y_linesStart + vertikalPadding * 2 + layerBoxHeight / 2;
		        		timeStart = wlm.getStart();
		        		timeEnd = wlm.getEnd();
		    	        timeTitle = String.format("%02d:%02d - %02d:%02d", timeStart / 60, timeStart % 60, timeEnd / 60, timeEnd % 60);
		    	        timeTitleFont = new Font(pdf, "Helvetica");
		    	        timeTitleFont.setSize(timeTitleSize);
		    	        timeTitleLine = new TextLine(timeTitleFont);
		    	        timeTitleLine.placeIn(mainBox);
		    	        timeTitleLine.setPosition(boxWidht - x_linesEnd + 80 - timeTitle.length() * timeTitleSize/2, y_layerTitle);
		    	        
		    	        timeTitleLine.setText(timeTitle);
		    	        timeTitleLine.drawOn(page);
		    	        
		    	        // name labels
		    	        timeTitle = wlm.getText();
		    	        timeTitleFont = new Font(pdf, "Helvetica");
		    	        timeTitleFont.setSize(timeTitleSize);
		    	        timeTitleLine = new TextLine(timeTitleFont);
		    	        timeTitleLine.placeIn(layerBox);
		    	        timeTitleLine.setPosition(Math.abs(layerBoxLength - (timeTitle.length() * timeTitleSize/2))/2, layerBoxHeight - Math.max((layerBoxHeight - timeTitleSize)/2, 1));
		    	        
		    	        timeTitleLine.setText(timeTitle);
		    	        timeTitleLine.drawOn(page);
	        			
	        			
	        			
	        			layerCount++;
	        			
	        		}
	        	}
	        }
	        
	        // foot line
	        int footerSize = 10;
	        int mins = model.getMinsCounter();
	        String footer = String.format("Schichtplanung V. 1.0 - RS Gastronomie GmbH & Co.KG, Herford - gesamte Stunden: %d:%02d",mins / 60, mins % 60);
	        
	        Font footerFont = new Font(pdf, "Helvetica");
	        footerFont.setSize(footerSize);
	        TextLine footerLine = new TextLine(footerFont);
	        footerLine.setPosition((Letter.PORTRAIT[0] - (footer.length() * footerSize/2))/2, 20 + boxHeight);
	        
	        footerLine.setText(footer);
	        footerLine.drawOn(page);
	        
	        
	        pdf.flush();
	        fos.close();
	        
	        Runtime rt = Runtime.getRuntime();
        	try{
        		rt.exec( "rundll32" +" " + "url.dll,FileProtocolHandler" + " " + file);
        	}catch (Exception e1){
        		e1.printStackTrace();
        	}
		}
	}
}
