# Help Desk Platform - Issue Tracker Shared API

## For Admin

### VOs

**TicketCreationReportData**:

- `today` Number of tickets that were created today.
- `onThisWeek` Number of tickets that were created this week.
- `onThisMonth` Number of tickets that were created this month.

### APIs

- `TicketCreationReportData getTicketCreationByCompanyReport(String companyName)` Returns with a `TicketCreationReportData` which represents how many tickets were created today, in this week, and in this month for the given company. Returns `null`, if something went wrong.

## For Helpdesk

### VOs

**TicketData**:

- `ticketName` Name of the ticket.
- `ticketDescription` Description of the ticket.
- `companyName` Name of the company.
- `bindedUser` A username, who have to work on this ticket, can be nullable.
- `recUser` A username, who creates this ticket.
- `ticketType` Type of the ticket.
- `clientMail` Mail of the client.

**UserByTicketStatusReportData**:

- `openedTickets` Number of the opened tickets.
- `closedTickets` Number of the closed tickets.

### APIs

- `void registerNewTicket(TicketData ticketData)` Registers the passed `TicketData` in the Issue Tracker platform. Returns with `false`, if the save failed.
- `TicketsStatusReportData getTicketsStatusByuserReport(String userName)` Returns with `TicketsStatusReportData` which represents that how many openned and closed issue tickets binded to the given user. Returns `null`, if something went wrong.
- `int getNumberOfCreatedTicketsByUser(String userName)` Returns with a number that represents how many tickets were created by the given user. Returns `null`, if something went wrong.
- `int getNumberOfCreatedTicketsByUser(String userName, Date fromDate, Date untilDate)` Returns with a number that represents how many tickets were created by the given user between the given `fromDate` and `untilDate`. Returns `null`, if something went wrong.

