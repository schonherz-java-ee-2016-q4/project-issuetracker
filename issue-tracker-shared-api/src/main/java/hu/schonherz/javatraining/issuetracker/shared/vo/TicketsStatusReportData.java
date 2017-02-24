package hu.schonherz.javatraining.issuetracker.shared.vo;

import java.io.Serializable;

public class TicketsStatusReportData implements Serializable {
	private static final long serialVersionUID = -3849047846122955851L;

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
