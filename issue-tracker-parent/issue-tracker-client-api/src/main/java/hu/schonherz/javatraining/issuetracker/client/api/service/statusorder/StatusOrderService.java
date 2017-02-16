package hu.schonherz.javatraining.issuetracker.client.api.service.statusorder;

import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusOrderVo;

public interface StatusOrderService {
	
	StatusOrderVo findByFromStatusId(Long id);
	StatusOrderVo findByToStatusId(Long id);
	StatusOrderVo save(StatusOrderVo statusOrder, String username);
	//rákövetkezők módosításának engedélyezése
	StatusOrderVo update(StatusOrderVo statusOrder, String username);
	//törlést nem engedélyezzük, esetleg később
}
