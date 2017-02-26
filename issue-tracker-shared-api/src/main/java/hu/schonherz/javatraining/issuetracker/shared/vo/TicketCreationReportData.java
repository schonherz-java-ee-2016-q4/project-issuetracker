package hu.schonherz.javatraining.issuetracker.shared.vo;

import java.io.Serializable;

public class TicketCreationReportData implements Serializable {
	private static final long serialVersionUID = 6782450149551380643L;
	
	private int today;
	private int onThisWeek;
	private int onThisMonth;
	
	public int getToday() {
		return today;
	}
	public void setToday(int today) {
		this.today = today;
	}
	public int getOnThisWeek() {
		return onThisWeek;
	}
	public void setOnThisWeek(int onThisWeek) {
		this.onThisWeek = onThisWeek;
	}
	public int getOnThisMonth() {
		return onThisMonth;
	}
	public void setOnThisMonth(int onThisMonth) {
		this.onThisMonth = onThisMonth;
	}
	
	@Override
	public String toString() {
		return String.format("TicketCreationReportData [today=%s, onThisWeek=%s, onThisMonth=%s]", today, onThisWeek,
				onThisMonth);
	}
	
}
