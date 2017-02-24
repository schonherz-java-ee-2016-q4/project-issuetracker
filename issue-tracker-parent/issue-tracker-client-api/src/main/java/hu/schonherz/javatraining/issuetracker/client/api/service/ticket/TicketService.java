package hu.schonherz.javatraining.issuetracker.client.api.service.ticket;

import java.util.List;

import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;

public interface TicketService {
	TicketVo findById(Long id);
	TicketVo findByUid(String uid);
	List<TicketVo> findByUser(UserVo user);
	List<TicketVo> findAll();
	TicketVo save(TicketVo ticket, String username);
	TicketVo update(TicketVo ticket, String username);
	List<TicketVo> findByType(TypeVo type);
	
	int getNumberOfClosedTicketsByUser(UserVo user);
	int getNumberOfOpenedTicketsByUser(UserVo user);
}
