package hu.schonherz.javatraining.issuetracker.shared.api;

import hu.schonherz.javatraining.issuetracker.shared.vo.TicketCreationReportData;

public interface ForAdminServiceRemote {
	TicketCreationReportData getTicketCreationByCompanyReport(String companyName);
}
