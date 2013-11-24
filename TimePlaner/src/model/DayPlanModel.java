package model;

import java.util.Collections;
import java.util.LinkedList;


public class DayPlanModel {

	private int day;
	
	private LinkedList<WorkingLayerModel> layers;
	
	public DayPlanModel(int day, WorkingLayerModel[] layers) {
		
		this.day = day;
		
		this.layers = new LinkedList<WorkingLayerModel>();
		
		for (int i = 0; i < layers.length; i++) {
			if (layers[i].getTimeCountInMins() > 0) {
				this.layers.add(layers[i]);
			}
		}
		
		Collections.sort(this.layers);

	}

	public int getDay() {
		return this.day;
	}
	
	public LinkedList<WorkingLayerModel> getLayers() {
		return this.layers;
	}
}
