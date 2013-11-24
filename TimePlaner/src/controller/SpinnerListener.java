package controller;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import view.DayPanel;

public class SpinnerListener implements ChangeListener{

	private DayPanel dayPanel;
	private int number;
	
	public SpinnerListener(DayPanel dayPanel, int number) {
		
		this.dayPanel = dayPanel;
		this.number = number;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		
		this.dayPanel.updateLayer(number);
		
	}
}
	