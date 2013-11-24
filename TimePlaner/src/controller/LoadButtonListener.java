package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.WeekPlanModel;
import view.JsonIO;
import view.MainView;

public class LoadButtonListener implements ActionListener {

	private MainView mainView;
	
	public LoadButtonListener(MainView mainView) {
		this.mainView = mainView;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		WeekPlanModel wpm = JsonIO.load(this.mainView.getDirPath(), this.mainView);
		
		this.mainView.setModell(wpm);
		
	}
}
