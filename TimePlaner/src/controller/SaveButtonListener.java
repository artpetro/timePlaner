package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.WeekPlanModel;
import view.JsonIO;
import view.MainView;

public class SaveButtonListener implements ActionListener {

	private MainView mainView;
	
	public SaveButtonListener(MainView mainView) {
		this.mainView = mainView;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		WeekPlanModel wpm = this.mainView.getModell();
		
		JsonIO.saveAs(this.mainView.getDirPath(), wpm, this.mainView);
		
	}
}
