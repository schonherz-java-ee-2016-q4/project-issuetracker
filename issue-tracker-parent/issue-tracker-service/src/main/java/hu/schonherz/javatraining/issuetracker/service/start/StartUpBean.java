package hu.schonherz.javatraining.issuetracker.service.start;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;

//@Startup
//@Singleton
public class StartUpBean {
	
	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	@EJB
	UserServiceRemote userServiceRemote;

	@EJB
	RoleServiceRemote roleServiceRemote;
	
	@PostConstruct
	public void init() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		
		RoleVo adminRole = roleServiceRemote.findByName(ROLE_ADMIN);
		if (adminRole == null) {
			adminRole = new RoleVo();
			adminRole.setName(ROLE_ADMIN);
			adminRole = roleServiceRemote.save(adminRole);
		}
		
		RoleVo userRole = roleServiceRemote.findByName(ROLE_USER);
		if (userRole == null) {
			userRole = new RoleVo();
			userRole.setName(ROLE_USER);
			userRole = roleServiceRemote.save(userRole);
		}
		System.out.println("User role id: " + userRole.getId());
		
		if (userServiceRemote.findByUsername("admin") == null) {
			UserVo adminUser = new UserVo();
			adminUser.setUsername("admin");
			adminUser.setPassword(bCryptPasswordEncoder.encode("admin"));
			
			List<RoleVo> adminRoles = new ArrayList<RoleVo>();
			adminRoles.add(userRole);
			adminRoles.add(adminRole);
			
			adminUser.setRoles(adminRoles);
			userServiceRemote.save(adminUser);
		}
		
		if (userServiceRemote.findByUsername("user") == null) {
			UserVo userUser = new UserVo();
			userUser.setUsername("user");
			userUser.setPassword(bCryptPasswordEncoder.encode("user"));
			
			List<RoleVo> userRoles = new ArrayList<RoleVo>();
			userRoles.add(userRole);
			
			userUser.setRoles(userRoles);
			userServiceRemote.save(userUser);
		}
	}
}