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
import hu.schonherz.javatraining.issuetracker.client.api.service.history.HistoryServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.DefaultRoleConstants;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryEnum;
import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import hu.schonherz.javatraining.issuetracker.core.dao.UserDao;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMappers;
import hu.schonherz.project.remote.admin.api.vo.issuetracker.RemoteUserVo;
import hu.schonherz.project.remote.admin.api.vo.issuetracker.UserRoles;
import lombok.extern.log4j.Log4j;


@Stateless(mappedName = "UserService")
@Local(UserServiceLocal.class)
@Remote(UserServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Log4j
public class UserServiceBean implements UserServiceRemote, UserServiceLocal {
	
	@Autowired
	private UserDao userDao;

	@EJB
	private RoleServiceLocal roleService;
	
	@EJB
	private CompanyServiceLocal companyService;
	
	@EJB
	private TicketServiceLocal ticketService;
	
	@EJB
	private HistoryServiceLocal historyService;
	
	@Override
	public List<UserVo> findAll(){
        return GenericVoMappers.USER_VO_MAPPER.toVo(userDao.findAll());
	}

	@Override
	public List<UserVo> findAllByCompany(CompanyVo companyVo){
        return GenericVoMappers.USER_VO_MAPPER.toVo(userDao.findAllByCompany(GenericVoMappers.COMPANY_VO_MAPPER.toEntity(companyVo)));
	}

	@Override
	public UserVo findById(Long id) {
		return GenericVoMappers.USER_VO_MAPPER.toVo(userDao.findById(id));
	}

	@Override
	public UserVo findByUsername(String username) {
		return GenericVoMappers.USER_VO_MAPPER.toVo(userDao.findByUsername(username));
	}

	@Override
	public UserVo save(UserVo user) {
		return GenericVoMappers.USER_VO_MAPPER.toVo(userDao.save(GenericVoMappers.USER_VO_MAPPER.toEntity(user)));
	}

	public UserVo mapRemoteUserVoToUserVo(RemoteUserVo remoteUserVo) {
		if (remoteUserVo == null) {
			log.debug("no user found");
			return null;
		}
		if ((remoteUserVo.getEmployerCompanyName() == null || "".equals(remoteUserVo.getEmployerCompanyName()))
				&& !UserRoles.ADMIN.equals(remoteUserVo.getRole())) {
			log.debug("user has no company");
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
		
		String currentCompanyName = userVo.getCompany() == null ? "" : userVo.getCompany().getName();
		String newCompanyName = remoteUserVo.getEmployerCompanyName() == null || "".equals(remoteUserVo.getEmployerCompanyName())
				? "" : remoteUserVo.getEmployerCompanyName();
		if (!newCompanyName.equals(currentCompanyName)) {
			back.setCompany(companyChanged(back, newCompanyName));
			modified = true;
		}

		return modified ? back : null;
	}
	
	private CompanyVo companyChanged(UserVo user, String newCompanyName) {
		CompanyVo company = null;
		
		if (newCompanyName != null && !"".equals(newCompanyName)) {
			company = companyService.findByName(newCompanyName);
			
			if (company == null) {
				company = CompanyVo.builder()
						.name(newCompanyName)
						.build();
				company = companyService.save(company);
			}
		}
		
		List<TicketVo> findByUser = ticketService.findByUser(user);
		HistoryVo changedHistory = HistoryVo.builder()
				.modStatus(HistoryEnum.UPDATED)
				.build();
		
		for (TicketVo ticketVo : findByUser) {
			ticketVo.setUser(null);
			HistoryVo savedHistory = historyService.saveInNewTransaction(changedHistory, user.getUsername());
			ticketVo.getHistory().add(savedHistory);
			ticketService.update(ticketVo, user.getUsername());
		}

		return company;
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
		
		CompanyVo company = null;
		if (remoteUserVo.getEmployerCompanyName() != null && !"".equals(remoteUserVo.getEmployerCompanyName())) {
			company = companyService.findByName(remoteUserVo.getEmployerCompanyName());
			if (company == null) {
				company = new CompanyVo();
				company.setName(remoteUserVo.getEmployerCompanyName());
				company = companyService.save(company);
			}
		}
		
		return UserVo.builder()
				.username(remoteUserVo.getUsername())
				.password(remoteUserVo.getEncryptedPassword())
				.company(company)
				.roles(roles)
				.build();
	}
	
}
