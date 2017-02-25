package hu.schonherz.javatraining.issuetracker.web.startup;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.DefaultRoleConstants;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import lombok.extern.log4j.Log4j;

@WebListener
@Log4j
public class StartUpConfig implements ServletContextListener {
	
	@EJB
	UserServiceRemote userServiceRemote;

	@EJB
	RoleServiceRemote roleServiceRemote;
	
	@EJB
	CompanyServiceRemote companyServiceRemote;
	
    @Override
    public void contextInitialized(ServletContextEvent event) {
    	
    	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		
		RoleVo adminRole = roleServiceRemote.findByName(DefaultRoleConstants.ROLE_ADMIN);
		if (adminRole == null) {
			adminRole = new RoleVo();
			adminRole.setName(DefaultRoleConstants.ROLE_ADMIN);
			adminRole = roleServiceRemote.save(adminRole);
		}
		
		RoleVo managerRole = roleServiceRemote.findByName(DefaultRoleConstants.ROLE_MANAGER);
		if (managerRole == null) {
			managerRole = new RoleVo();
			managerRole.setName(DefaultRoleConstants.ROLE_MANAGER);
			managerRole = roleServiceRemote.save(managerRole);
		}
		
		RoleVo userRole = roleServiceRemote.findByName(DefaultRoleConstants.ROLE_MANAGER);
		if (userRole == null) {
			userRole = new RoleVo();
			userRole.setName(DefaultRoleConstants.ROLE_MANAGER);
			userRole = roleServiceRemote.save(userRole);
		}
		
		CompanyVo testCompany = companyServiceRemote.findByName("test");
		if (testCompany == null) {
			CompanyVo companyVo = new CompanyVo();
			companyVo.setName("test");
			testCompany = companyServiceRemote.save(companyVo);
		}

		log.debug("User role id: " + userRole.getId());
		
		if (userServiceRemote.findByUsername("admin") == null) {
			UserVo adminUser = new UserVo();
			adminUser.setUsername("admin");
			adminUser.setPassword(bCryptPasswordEncoder.encode("admin"));
			adminUser.setCompany(testCompany);
			
			List<RoleVo> adminRoles = new ArrayList<RoleVo>();
			adminRoles.add(userRole);
			adminRoles.add(managerRole);
			adminRoles.add(adminRole);
			
			adminUser.setRoles(adminRoles);
			userServiceRemote.save(adminUser);
		}
		
		if (userServiceRemote.findByUsername("manager") == null) {
			UserVo managerUser = new UserVo();
			managerUser.setUsername("manager");
			managerUser.setPassword(bCryptPasswordEncoder.encode("manager"));
			managerUser.setCompany(testCompany);
			
			List<RoleVo> managerRoles = new ArrayList<RoleVo>();
			managerRoles.add(userRole);
			managerRoles.add(managerRole);
			
			managerUser.setRoles(managerRoles);
			userServiceRemote.save(managerUser);
		}
		
		if (userServiceRemote.findByUsername("user") == null) {
			UserVo userUser = new UserVo();
			userUser.setUsername("user");
			userUser.setPassword(bCryptPasswordEncoder.encode("user"));
			userUser.setCompany(testCompany);
			
			List<RoleVo> userRoles = new ArrayList<RoleVo>();
			userRoles.add(userRole);
			
			userUser.setRoles(userRoles);
			userServiceRemote.save(userUser);
		}
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

}