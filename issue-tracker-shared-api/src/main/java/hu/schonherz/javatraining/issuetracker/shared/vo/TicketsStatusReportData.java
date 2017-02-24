package hu.schonherz.javatraining.issuetracker.shared.vo;

public class TicketsStatusReportData {
	private int openedTickets;
	private int closedTickets;
	
	public int getOpenedTickets() {
		return openedTickets;
	}
	public void setOpenedTickets(int openedTickets) {
		this.openedTickets = openedTickets;
	}
	public int getClosedTickets() {
		return closedTickets;
	}
	public void setClosedTickets(int closedTickets) {
		this.closedTickets = closedTickets;
	}
	
}
