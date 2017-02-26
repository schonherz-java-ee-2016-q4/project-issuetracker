package hu.schonherz.javatraining.issuetracker.client.api.service.history;

import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryVo;

public interface HistoryService {
	HistoryVo findById(Long id);
	HistoryVo save(HistoryVo history, String username);
	HistoryVo update(HistoryVo history, String username);
	HistoryVo saveInNewTransaction(HistoryVo history, String username);
}
