package hu.schonherz.javatraining.issuetracker.client.api.service.statusorder;

import java.util.List;

import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusOrderVo;

public interface StatusOrderService {
	
	List<StatusOrderVo> findByFromStatusId(Long id);
	List<StatusOrderVo> findByToStatusId(Long id);
	StatusOrderVo findByFromStatusIdAndToStatusId(Long fromId, Long toId);
	StatusOrderVo save(StatusOrderVo statusOrder, String username);
	//rákövetkezők módosításának engedélyezése
	StatusOrderVo update(StatusOrderVo statusOrder, String username);
	//törlést nem engedélyezzük, esetleg később
}
