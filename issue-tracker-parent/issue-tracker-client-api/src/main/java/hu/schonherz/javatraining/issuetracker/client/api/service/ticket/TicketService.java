package hu.schonherz.javatraining.issuetracker.client.api.service.ticket;

import java.util.List;

import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;

public interface TicketService {
	TicketVo findById(Long id);
	TicketVo findByUid(String uid);
	List<TicketVo> findByUser(UserVo user);
	TicketVo save(TicketVo ticket);
	TicketVo update(TicketVo ticket);
}
