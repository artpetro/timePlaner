package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.DayPlanModel;
import model.WeekPlanModel;
import model.WorkingLayerModel;

public class MainView extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel;
	private HeaderPanel headerPanel;
	private JPanel daysPanel;
	
	private ButtonsPanel buttonsPanel;
	private DayPanel[] dayPanels;
	
	private String dirPath;
	
	public static final String[] days = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"}; 
	
	public MainView() {

		Preferences user = Preferences.userRoot();
		
		this.dirPath = user.get("timePlanerDirPath", System.getProperty("user.home"));
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final Dimension d = this.getToolkit().getScreenSize();
		this.setSize((int)Math.min(1800, d.getWidth()), (int)Math.min(800, d.getHeight()));
		this.setLocation((int) ((d.getWidth() - this.getWidth()) / 2), (int) ((d.getHeight() - this.getHeight()) / 2));
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);  
		
		// header panel
		this.headerPanel = new HeaderPanel();
		
		// main panel
		this.daysPanel = new JPanel();
		
		daysPanel.setLayout(new GridLayout(2, 0, 6, 3));
		
		this.dayPanels = new DayPanel[7];
		
		for (int i = 0; i < 7; i++) {
			
			this.dayPanels[i] = new DayPanel(MainView.days[i], this);
			daysPanel.add(this.dayPanels[i]);
		
		}
		
		// buttons panel
		this.buttonsPanel = new ButtonsPanel(this);
		this.daysPanel.add(buttonsPanel);
		
		// main panel
		this.mainPanel = new JPanel();
		
		this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS)); 
		
		this.mainPanel.add(headerPanel);
		this.mainPanel.add(daysPanel);
		
		this.add(new JScrollPane(this.mainPanel));
		this.setVisible(true);
		
	}

	public void updateCount() {
		
		int timeMins = 0;
		
		for (int i = 0; i < 7; i++) {
			String[] time = this.dayPanels[i].getTimeCountField(9).getText().split(":");
			timeMins += Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]);
			
		}
		
		this.buttonsPanel.getTimeFeld().setText(timeMins/60 + ":" + String.format("%02d", timeMins%60));
		
	}
	
	public String getDirPath() {
		return this.dirPath;
	}
	
	public void setDirPath(String path) {
		this.dirPath = path;

		Preferences user = Preferences.userRoot();
		user.put("timePlanerDirPath", path);
		
	}
	
	
	// Modell
	public WeekPlanModel getModell() {
		
		DayPlanModel[] dpArr = new DayPlanModel[7];
		
		for (int i = 0; i < 7; i++) {
			
			DayPanel panel = this.dayPanels[i];
			WorkingLayerModel[] wlArr = new WorkingLayerModel[9]; 
					
			for (int k = 0; k < 9; k++) {
				
				WorkingLayerModel wl = new WorkingLayerModel(panel.getMinFromBeginStart(k), panel.getMinFromBeginEnd(k), panel.getText(k));
				wlArr[k] = wl;
			
			}
			
			DayPlanModel dp = new DayPlanModel(i, wlArr);
			dpArr[i] = dp;
			
		}
		
		return new WeekPlanModel(this.headerPanel.getTitle(), dpArr);
		
	}
	
	public void setModell(WeekPlanModel wpm) {
		
		this.headerPanel.setTitle(wpm.getTitle());
		
		DayPlanModel[] dayPlanArr = wpm.getDays();
		
		for (int i = 0; i < 7; i++) {
			
			this.dayPanels[i].setModel(dayPlanArr[i]);
			
		}	
	}
	
	public String getTitle() {
		return this.headerPanel.getTitle();
	}

}
