package hu.schonherz.javatraining.issuetracker.client.api.service.user;

import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;

public interface UserService {
	UserVo findByUsername(String username);
}
