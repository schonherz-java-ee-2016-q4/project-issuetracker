package hu.schonherz.javatraining.issuetracker.client.api.service.status;

import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;

public interface StatusService {
	
	StatusVo findById(Long id);
	StatusVo findByName(String name);
	StatusVo save(StatusVo status);
	//rákövetkezők módosításának engedélyezése
	StatusVo modify(StatusVo status);
	//törlést nem engedélyezzük, esetleg később
}
