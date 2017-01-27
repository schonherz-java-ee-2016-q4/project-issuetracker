package hu.schonherz.javatraining.issuetracker.service.start;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;

@Startup
@Singleton
public class StartUpBean {
	
	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	@EJB
	UserServiceRemote userServiceRemote;

	@EJB
	RoleServiceRemote roleServiceRemote;
	
	@PostConstruct
	public void init() {
		if (userServiceRemote.findByUsername("admin") == null) {
			List<RoleVo> roles = new ArrayList<RoleVo>();
			RoleVo adminRole = roleServiceRemote.findByName(ROLE_ADMIN);
			if (adminRole == null) {
				adminRole = new RoleVo();
				adminRole.setName(ROLE_ADMIN);
			}
			RoleVo userRole = roleServiceRemote.findByName(ROLE_USER);
			if (userRole == null) {
				userRole = new RoleVo();
				userRole.setName(ROLE_USER);
			}
			roles.add(userRole);
			roles.add(adminRole);
			
			UserVo adminUser = new UserVo();
			adminUser.setUsername("admin");
			adminUser.setPassword("admin");
			adminUser.setRoles(roles);
			
			userServiceRemote.save(adminUser);
		}
	}
}