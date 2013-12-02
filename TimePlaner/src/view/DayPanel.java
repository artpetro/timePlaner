package view;

import java.awt.Color;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import model.DayPlanModel;
import model.WorkingLayerModel;
import net.java.dev.designgridlayout.DesignGridLayout;
import utils.NameDocument;
import utils.TimeDocument;
import controller.SpinnerListener;

public class DayPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String day;
	
	private JLabel[] labels;
	private TimeSpinner[] startSpinners;
	private TimeSpinner[] endSpinners;
	private JTextField[] timeCountFields;
	private JTextField[] nameFields;

	private MainView mainView;
	

	
	public DayPanel(String day, MainView mainView) {
		
		this.day = day;
		
		this.mainView = mainView;
		
		this.labels = new JLabel[10];
		this.startSpinners = new TimeSpinner[9];
		this.endSpinners = new TimeSpinner[9];
		this.timeCountFields = new JTextField[10];
		this.nameFields = new JTextField[9];
		
		this.buildView();
		
	}
	
	
	private void buildView() {
		
		DesignGridLayout thisLayout = new DesignGridLayout(this);
		
		this.setBorder(new TitledBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY ), this.day));
		
		thisLayout.row().grid(new JLabel("")).add(new JLabel("von")).add(new JLabel("bis")).add(new JLabel("Std")).add(new JLabel("Text"), 2);
		
		for (int i = 0; i < 9; i++) {
			
			this.labels[i] = new JLabel(i + 1 + ".");
			this.startSpinners[i] = new TimeSpinner();
			this.endSpinners[i] = new TimeSpinner();
			this.timeCountFields[i] = new JTextField(new TimeDocument(5), "", 5);
			this.nameFields[i] = new JTextField(new NameDocument(20), "", 10);
			
			this.timeCountFields[i].setEditable(false);
			this.timeCountFields[i].setText("0:00");
			
			thisLayout.row().grid(this.labels[i]).add(this.startSpinners[i]).add(this.endSpinners[i]).add(this.timeCountFields[i]).add(this.nameFields[i], 2);
			
			// add listener
			SpinnerListener listener = new SpinnerListener(this, i);
			this.startSpinners[i].addChangeListener(listener);
			this.endSpinners[i].addChangeListener(listener);
			
		}
		
		this.timeCountFields[9] = new JTextField(new TimeDocument(10), "", 5);
		this.timeCountFields[9].setEditable(false);
		this.timeCountFields[9].setText("0:00");
		
		thisLayout.row().grid(new JLabel("ges")).add(new JLabel("")).add(new JLabel("")).add(this.timeCountFields[9]).add(new JLabel(""), 2);
		
	}
	
	// minutes from 00:00 for start
	public int getMinFromBeginStart(int number) {
		
		TimeSpinner start = this.startSpinners[number];
		Date startDate = (Date)start.getValue();
		
		@SuppressWarnings("deprecation")
		int startHours = startDate.getHours();
		@SuppressWarnings("deprecation")
		int startMins = startDate.getMinutes();
		
		return startHours * 60 + startMins;
		
	}
	
	public int getMinFromBeginEnd(int number) {
		
		TimeSpinner end = this.endSpinners[number];
		Date endDate = (Date)end.getValue();
		
		@SuppressWarnings("deprecation")
		int endHours = endDate.getHours();
		@SuppressWarnings("deprecation")
		int endMins = endDate.getMinutes();
		
		return endHours * 60 + endMins;
		
	}
	
	public String getText(int number) {
		return this.nameFields[number].getText();
	}


	public JTextField getTimeCountField(int number) {
		return this.timeCountFields[number];
	}


	public void updateCount() {
		
		int timeMins = 0;
		
		for (int i = 0; i < 9; i++) {
			String[] time = this.timeCountFields[i].getText().split(":");
			timeMins += Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]);
			
		}
		
		this.timeCountFields[9].setText(timeMins/60 + ":" + String.format("%02d", timeMins%60));
		
		this.mainView.updateCount();
		
	}


	public void setModel(DayPlanModel dayPlanModel) {
		
		LinkedList<WorkingLayerModel> layers = dayPlanModel.getLayers();
		int layerCount = layers.size();
		
		for (int i = 0; i < layerCount; i++) {
			
			this.startSpinners[i].setTime(layers.get(i).getStart());
			this.endSpinners[i].setTime(layers.get(i).getEnd());
			this.nameFields[i].setText(layers.get(i).getText());
			this.updateLayerView(i);
			
		}
		
		// reset empty layers
		for (int i = layerCount; i < 9; i++) {
			
			this.startSpinners[i].setTime(0);
			this.endSpinners[i].setTime(0);
			this.nameFields[i].setText("");
			this.updateLayerView(i);
		
		}
		
		this.updateCount();
		
	}

	

	public void updateLayer(int number) {
		
		this.updateLayerView(number);
		this.updateCount();
		
	}
	
	private void updateLayerView(int number) {
	
		JTextField time = this.timeCountFields[number];
		
		int minFromBeginStart = this.getMinFromBeginStart(number);
		int minFromBeginEnd = this.getMinFromBeginEnd(number);
		
		int timeMins = 0;
		
		if (minFromBeginStart < minFromBeginEnd) {
			timeMins = minFromBeginEnd - minFromBeginStart;
		}
		
		else if (minFromBeginStart > minFromBeginEnd) {
			timeMins = 24 * 60 - minFromBeginStart + minFromBeginEnd;
		}
		
		time.setText(timeMins/60 + ":" + String.format("%02d", timeMins%60));
	
	}
}
