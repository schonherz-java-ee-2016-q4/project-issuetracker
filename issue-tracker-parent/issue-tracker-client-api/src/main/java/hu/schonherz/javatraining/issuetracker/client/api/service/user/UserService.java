package hu.schonherz.javatraining.issuetracker.client.api.service.user;

import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;

import java.util.List;

public interface UserService {
	UserVo findById(Long id);
	UserVo findByUsername(String username);
	List<UserVo> findAll();
	List<UserVo> findAllByCompany(CompanyVo companyVo);
	UserVo save(UserVo user);
}
