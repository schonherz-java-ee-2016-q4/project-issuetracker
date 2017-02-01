package hu.schonherz.javatraining.issuetracker.client.api.service.history;

import java.util.List;

import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;

public interface HistoryService {
	HistoryVo findById(Long id);
	List<HistoryVo> findByTicket(TicketVo ticket);
	HistoryVo save(HistoryVo history);
	HistoryVo update(HistoryVo history);
}
