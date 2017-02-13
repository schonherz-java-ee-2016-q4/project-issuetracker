package hu.schonherz.javatraining.issuetracker.client.api.service.status;

import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;

public interface StatusService {
	
	StatusVo findById(Long id);
	StatusVo findByName(String name);
	StatusVo save(StatusVo status, String username);
	//rákövetkezők módosításának engedélyezése
	StatusVo update(StatusVo status, String username);
	//törlést nem engedélyezzük, esetleg később
}
