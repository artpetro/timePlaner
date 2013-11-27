package model;

import java.util.LinkedList;

public class WeekPlanModel {
	
	private String title;
	
	private DayPlanModel[] days;
	
	public WeekPlanModel (String title, DayPlanModel[] days) {
		
		this.title = title;
		this.days = days;
		
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public DayPlanModel[] getDays() {
		return this.days;
	}
	

	public int[] getStartEndPeriod() {
		
		int[] se = new int[3];
	
		int start = 23 * 60 + 59; 
		int end = 0;
		int newDay_end = -1;
		int count = 0;
		
		
		for (int i = 0; i < this.days.length; i++) {
			
			DayPlanModel day = days[i];
			
			LinkedList<WorkingLayerModel > layers = day.getLayers();
			
			count += layers.size();
			
			if (!layers.isEmpty()) {
				
					if(layers.getFirst().getStart() < start) {
						start = layers.getFirst().getStart();
					}
					
					for (WorkingLayerModel wlm : layers) {
						
						if (wlm.getEnd() < wlm.getStart()) {
							if (wlm.getEnd() > newDay_end) {
								newDay_end = wlm.getEnd();
							}
						}
						
						if(wlm.getEnd() > end) {
							end = wlm.getEnd();
						}
					}
			}
		}
		
		if(count == 0) {
			start = 0;
		}
		
		if (newDay_end >= 0) {
			end = newDay_end;
		}
		
		se[0] = start;
		se[1] = end;
		se[2] = count;
		
		return se;
	
	}

	public boolean isNewDay() {

		for (int i = 0; i < this.days.length; i++) {
			
			DayPlanModel day = days[i];
			
			LinkedList<WorkingLayerModel> layers = day.getLayers();
			
			for (WorkingLayerModel layer : layers) {
				if(layer.isNewDay()) {
					return true;
				}
			}
		}
		
		return false;
	}

	public int getMinsCounter() {
		
		int mins = 0;

		for (int i = 0; i < this.days.length; i++) {
			
			DayPlanModel day = days[i];
			
			LinkedList<WorkingLayerModel > layers = day.getLayers();
			
					
			for (WorkingLayerModel wlm : layers) {
				mins += wlm.getTimeCountInMins();
			}
		}
		return mins;
	}
}
