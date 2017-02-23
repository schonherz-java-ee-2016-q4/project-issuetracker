package hu.schonherz.javatraining.issuetracker.shared.api;

import java.util.Date;

import hu.schonherz.javatraining.issuetracker.shared.vo.TicketData;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketsStatusReportData;

public interface ForHelpdeskRemote {
	Boolean registerNewTicket(TicketData ticketData);
	TicketsStatusReportData getTicketsStatusByuserReport(String userName);
	Integer getNumberOfCreatedTicketsByUser(String userName);
	Integer getNumberOfCreatedTicketsByUser(String userName, Date fromDate, Date untilDate);
}
