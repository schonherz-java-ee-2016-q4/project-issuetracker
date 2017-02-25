package hu.schonherz.javatraining.issuetracker.client.api.service.ticket;

import java.util.Date;
import java.util.List;

import hu.schonherz.javatraining.issuetracker.client.api.vo.*;

public interface TicketService {
	TicketVo findById(Long id);
	TicketVo findByUid(String uid);
	List<TicketVo> findByUser(UserVo user);
	List<TicketVo> findAll();
	TicketVo save(TicketVo ticket, String username);
	TicketVo update(TicketVo ticket, String username);
	List<TicketVo> findByType(TypeVo type);
	List<TicketVo> findByCompany(CompanyVo company);
	List<TicketVo> getTicketsByCompanyAndTime(CompanyVo company, Date date);
	int getNumberOfTicketsByTypeAndStatus(TypeVo type, StatusVo currentStatus);

	int getNumberOfClosedTicketsByUser(UserVo user);
	int getNumberOfOpenedTicketsByUser(UserVo user);
	int getNumberOfCreatedTicketsByUser(String userName);
	int getNumberOfCreatedTicketsByUserBetweenTime(String userName, Date fromDate, Date untilDate);
	int getNumberOfCreatedTicketsByCompanyToday(CompanyVo company);
	int getNumberOfCreatedTicketsByCompanyThisWeek(CompanyVo company);
	int getNumberOfCreatedTicketsByCompanyThisMonth(CompanyVo company);
}
