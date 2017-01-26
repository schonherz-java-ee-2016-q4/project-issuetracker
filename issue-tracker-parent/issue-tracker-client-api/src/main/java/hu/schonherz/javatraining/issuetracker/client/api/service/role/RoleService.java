package hu.schonherz.javatraining.issuetracker.client.api.service.role;

import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;

public interface RoleService {
	RoleVo findByName(String name);
}
