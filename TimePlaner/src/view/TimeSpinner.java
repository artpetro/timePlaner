package view;

import java.util.Date;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

public class TimeSpinner extends JSpinner {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	public TimeSpinner() {
		
		super(new SpinnerDateModel());
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(this, "HH:mm");
		this.setEditor(timeEditor);
		Date date = new Date();
		date.setHours(0);
		date.setMinutes(0);
		this.setValue(date);
	}

	@SuppressWarnings("deprecation")
	public void setTime(int time) {
		
		Date date = new Date();
		date.setHours(time/60);
		date.setMinutes(time%60);
		this.setValue(date);
		
	}
}
