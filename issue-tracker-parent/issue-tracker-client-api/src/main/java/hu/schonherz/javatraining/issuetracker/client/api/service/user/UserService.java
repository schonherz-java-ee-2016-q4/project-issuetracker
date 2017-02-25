package hu.schonherz.javatraining.issuetracker.client.api.service.user;

import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import hu.schonherz.project.remote.admin.api.vo.issuetracker.RemoteUserVo;

import java.util.List;

public interface UserService {
	UserVo findById(Long id);
	UserVo findByUsername(String username);
	List<UserVo> findAll();
	List<UserVo> findAllByCompany(CompanyVo companyVo);
	UserVo save(UserVo user);
	UserVo mapRemoteUserVoToUserVo(RemoteUserVo remoteUserVo);
}
