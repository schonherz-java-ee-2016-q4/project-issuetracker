# Help Desk Platform - Issue Tracker Shared API

## For Admin Service

**JNDI**: `java:global/issue-tracker-ear-0.0.1-SNAPSHOT/issue-tracker-service-0.0.1-SNAPSHOT/ForAdminServiceBean!hu.schonherz.javatraining.issuetracker.shared.api.ForAdminServiceRemote`

### VOs

**TicketCreationReportData**:

- `today` Number of tickets that were created today.
- `onThisWeek` Number of tickets that were created this week.
- `onThisMonth` Number of tickets that were created this month.

### APIs

- `TicketCreationReportData getTicketCreationByCompanyReport(String companyName)`<br />
**Description**: Returns with a `TicketCreationReportData` which represents how many tickets were created today, in this week, and in this month for the given company. Returns `null`, if something went wrong.


## For Helpdesk Service

**JNDI**: `java:global/issue-tracker-ear-0.0.1-SNAPSHOT/issue-tracker-service-0.0.1-SNAPSHOT/ForHelpdeskServiceBean!hu.schonherz.javatraining.issuetracker.shared.api.ForHelpdeskServiceRemote`

### VOs

**TicketData**:

- `ticketName` Name of the ticket. Maximum length is 30 characters.
- `ticketDescription` Description of the ticket.
- `companyName` Name of the company.
- `bindedUser` A username, who have to work on this ticket, can be nullable.
- `recUser` A username, who creates this ticket.
- `ticketTypeName` Type of the ticket.
- `clientMail` Mail of the client.

**UserByTicketStatusReportData**:

- `openedTickets` Number of the opened tickets.
- `closedTickets` Number of the closed tickets.

### APIs

- `void registerNewTicket(TicketData ticketData)`<br />
	**Description**: Registers the passed `TicketData` in the Issue Tracker platform. Returns with `false`, if the save failed.<br />
	**Throws**: Throws `QutaReachedException` if the company has reached the maximum recordable number of tickets.
- `TicketsStatusReportData getTicketsStatusByuserReport(String userName)`<br />
	**Description**: Returns with `TicketsStatusReportData` which represents that how many openned and closed issue tickets binded to the given user. Returns `null`, if something went wrong.
- `int getNumberOfCreatedTicketsByUser(String userName)`<br />
	**Description**: Returns with a number that represents how many tickets were created by the given user. Returns `null`, if something went wrong.
- `int getNumberOfCreatedTicketsByUser(String userName, Date fromDate, Date untilDate)`<br />
	**Description**: Returns with a number that represents how many tickets were created by the given user between the given `fromDate` and `untilDate`. Returns `null`, if something went wrong.
- `List<String> getTypesByCompany(String companyName)`<br />
	**Description**: Returns with a list that contains the available types for the given company. Returns `null`, if something went wrong.

