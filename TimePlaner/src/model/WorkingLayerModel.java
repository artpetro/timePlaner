package model;

public class WorkingLayerModel implements Comparable<Object>{
	
	
	
	private String text;
	private int minFromBeginStart;
	private int minFromBeginEnd;
	
	
	public WorkingLayerModel(int minFromBeginStart, int minFromBeginEnd, String text) {
		
		this.text = text;
		this.minFromBeginStart = minFromBeginStart;
		this.minFromBeginEnd = minFromBeginEnd;
	}
	
	@Override
	public int compareTo(Object arg0) {	
		return this.minFromBeginStart - ((WorkingLayerModel)arg0).getStart();
	}

	public int getStart() {
		return this.minFromBeginStart;
	}
	
	public int getEnd() {
		return this.minFromBeginEnd;
	}
	
	public int getTimeCountInMins() {
		
		int timeMins = 0;
		
		if (minFromBeginStart < minFromBeginEnd) {
			timeMins = minFromBeginEnd - minFromBeginStart;
		}
		
		else if (minFromBeginStart > minFromBeginEnd) {
			timeMins = 24 * 60 - minFromBeginStart + minFromBeginEnd;
		}
		
		return timeMins;
	}
	
	public String toString() {
		return this.minFromBeginStart + "|" + this.minFromBeginEnd + "|" + this.text;
	}

	public String getText() {
		return this.text;
	}
	
	public boolean isNewDay() {
		return minFromBeginStart > minFromBeginEnd;
	}
	
}
