package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.MainView;

public class CloseButtonListener implements ActionListener {

	private MainView mainView;
	
	public CloseButtonListener(MainView mainView) {
		this.mainView = mainView;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		this.mainView.dispose();
		
	}
}
