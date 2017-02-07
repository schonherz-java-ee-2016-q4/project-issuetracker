package hu.schonherz.javatraining.issuetracker.client.api.service.statusorder;

import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusOrderVo;

public interface StatusOrderService {
	
	StatusOrderVo findByFromStatusId(Long id);
	StatusOrderVo findByToStatusId(Long id);
	StatusOrderVo save(StatusOrderVo statusOrder);
	//rákövetkezők módosításának engedélyezése
	StatusOrderVo modify(StatusOrderVo statusOrder);
	//törlést nem engedélyezzük, esetleg később
}
