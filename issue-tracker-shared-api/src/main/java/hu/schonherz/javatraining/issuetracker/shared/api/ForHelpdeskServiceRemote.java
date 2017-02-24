package hu.schonherz.javatraining.issuetracker.shared.api;

import java.util.Date;
import java.util.List;

import hu.schonherz.javatraining.issuetracker.shared.vo.TicketData;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketsStatusReportData;

public interface ForHelpdeskServiceRemote {
	Boolean registerNewTicket(TicketData ticketData);
	TicketsStatusReportData getTicketsStatusByuserReport(String userName);
	Integer getNumberOfCreatedTicketsByUser(String userName);
	Integer getNumberOfCreatedTicketsByUser(String userName, Date fromDate, Date untilDate);
	List<String> getTypesByCompany(String companyName);
}
