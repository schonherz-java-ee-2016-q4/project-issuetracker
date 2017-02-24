package hu.schonherz.javatraining.issuetracker.client.api.service.ticket;

import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;

import java.util.Date;
import java.util.List;

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
}
