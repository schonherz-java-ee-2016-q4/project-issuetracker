package hu.schonherz.javatraining.issuetracker.service.impl.user;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.DefaultRoleConstants;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import hu.schonherz.javatraining.issuetracker.core.dao.UserDao;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMappers;
import hu.schonherz.project.remote.admin.api.vo.issuetracker.RemoteUserVo;
import hu.schonherz.project.remote.admin.api.vo.issuetracker.UserRoles;


@Stateless(mappedName = "UserService")
@Local(UserServiceLocal.class)
@Remote(UserServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserServiceBean implements UserServiceRemote, UserServiceLocal {
	
	@Autowired
	private UserDao userDao;

	@EJB
	private RoleServiceLocal roleService;
	
	@EJB
	private CompanyServiceLocal companyService;
	
	@Override
	public List<UserVo> findAll(){
        return GenericVoMappers.userVoMapper.toVo(userDao.findAll());
	}

	@Override
	public List<UserVo> findAllByCompany(CompanyVo companyVo){
        return GenericVoMappers.userVoMapper.toVo(userDao.findAllByCompany(GenericVoMappers.companyVoMapper.toEntity(companyVo)));
	}

	@Override
	public UserVo findById(Long id) {
		return GenericVoMappers.userVoMapper.toVo(userDao.findById(id));
	}

	@Override
	public UserVo findByUsername(String username) {
		return GenericVoMappers.userVoMapper.toVo(userDao.findByUsername(username));
	}

	@Override
	public UserVo save(UserVo user) {
		return GenericVoMappers.userVoMapper.toVo(userDao.save(GenericVoMappers.userVoMapper.toEntity(user)));
	}

	public UserVo mapRemoteUserVoToUserVo(RemoteUserVo remoteUserVo) {
		if (remoteUserVo == null || remoteUserVo.getEmployerCompanyName() == null
				|| "".equals(remoteUserVo.getEmployerCompanyName())) {
			return null;
		}
		
		UserVo back = findByUsername(remoteUserVo.getUsername());
		if (back != null) {
			UserVo changedUserVo = makeChangesInUserByRemoteData(remoteUserVo, back);
			
			if (changedUserVo != null) {
				back = save(changedUserVo);
			}
			
			return back;
		}
		
		return createNewFromRemoteUserVo(remoteUserVo);
	}
	
	private UserVo makeChangesInUserByRemoteData(RemoteUserVo remoteUserVo, UserVo userVo) {
		boolean modified = false;
		UserVo back = userVo;
		
		if (!remoteUserVo.getEncryptedPassword().equals(userVo.getPassword())) {
			back.setPassword(remoteUserVo.getEncryptedPassword());
			modified = true;
		}
		
		List<RoleVo> newRoles = new ArrayList<>();
		switch (remoteUserVo.getRole()) {
			case UserRoles.ADMIN:
				if (!userVo.getRoles().stream().filter(x -> x.getName() == DefaultRoleConstants.ROLE_ADMIN).findFirst().isPresent()) {
					newRoles.add(roleService.findByName(DefaultRoleConstants.ROLE_ADMIN));
					newRoles.add(roleService.findByName(DefaultRoleConstants.ROLE_MANAGER));
					newRoles.add(roleService.findByName(DefaultRoleConstants.ROLE_USER));
					back.setRoles(newRoles);
					modified = true;
				}
				break;
			case UserRoles.COMPANY_ADMIN:
				if (!userVo.getRoles().stream().filter(x -> x.getName() == DefaultRoleConstants.ROLE_MANAGER).findFirst().isPresent()) {
					newRoles.add(roleService.findByName(DefaultRoleConstants.ROLE_MANAGER));
					newRoles.add(roleService.findByName(DefaultRoleConstants.ROLE_USER));
					back.setRoles(newRoles);
					modified = true;
				}
				break;
			case UserRoles.AGENT:
				if (!userVo.getRoles().stream().filter(x -> x.getName() == DefaultRoleConstants.ROLE_USER).findFirst().isPresent()) {
					newRoles.add(roleService.findByName(DefaultRoleConstants.ROLE_USER));
					back.setRoles(newRoles);
					modified = true;
				}
				break;
			default:
				break;
		}

		return modified ? back : null;
	}
	
	private UserVo createNewFromRemoteUserVo(RemoteUserVo remoteUserVo) {
		List<RoleVo> roles = new ArrayList<>();
		
		switch (remoteUserVo.getRole()) {
			case UserRoles.ADMIN:
				roles.add(roleService.findByName(DefaultRoleConstants.ROLE_ADMIN));
				roles.add(roleService.findByName(DefaultRoleConstants.ROLE_MANAGER));
				roles.add(roleService.findByName(DefaultRoleConstants.ROLE_USER));
				break;
			case UserRoles.COMPANY_ADMIN:
				roles.add(roleService.findByName(DefaultRoleConstants.ROLE_MANAGER));
				roles.add(roleService.findByName(DefaultRoleConstants.ROLE_USER));
				break;
			case UserRoles.AGENT:
				roles.add(roleService.findByName(DefaultRoleConstants.ROLE_USER));
				break;
			default:
				break;
		}
		
		CompanyVo company = companyService.findByName(remoteUserVo.getEmployerCompanyName());
		if (company == null) {
			company = new CompanyVo();
			company.setName(remoteUserVo.getEmployerCompanyName());
			company = companyService.save(company);
		}
		
		return UserVo.builder()
				.username(remoteUserVo.getUsername())
				.password(remoteUserVo.getEncryptedPassword())
				.company(company)
				.roles(roles)
				.build();
	}
	
}
