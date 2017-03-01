package hu.schonherz.javatraining.issuetracker.web.startup;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.company.DefaultCompanyConstants;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.DefaultRoleConstants;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.DefaultUserConstants;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import lombok.extern.log4j.Log4j;

@WebListener
@Log4j
public class StartUpConfig implements ServletContextListener {
	
	@EJB
	private UserServiceRemote userServiceRemote;

	@EJB
	private RoleServiceRemote roleServiceRemote;
	
	@EJB
	private CompanyServiceRemote companyServiceRemote;
	
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
		
		RoleVo userRole = roleServiceRemote.findByName(DefaultRoleConstants.ROLE_USER);
		if (userRole == null) {
			userRole = new RoleVo();
			userRole.setName(DefaultRoleConstants.ROLE_USER);
			userRole = roleServiceRemote.save(userRole);
		}
		
		CompanyVo testCompany = companyServiceRemote.findByName(DefaultCompanyConstants.COMPANY_TEST);
		if (testCompany == null) {
			CompanyVo companyVo = new CompanyVo();
			companyVo.setName(DefaultCompanyConstants.COMPANY_TEST);
			testCompany = companyServiceRemote.save(companyVo);
		}

		log.debug("User role id: " + userRole.getId());
		
		if (userServiceRemote.findByUsername(DefaultUserConstants.USER_ADMIN) == null) {
			UserVo adminUser = new UserVo();
			adminUser.setUsername(DefaultUserConstants.USER_ADMIN);
			adminUser.setPassword(bCryptPasswordEncoder.encode(DefaultUserConstants.USER_ADMIN));
			adminUser.setCompany(testCompany);
			
			List<RoleVo> adminRoles = new ArrayList<RoleVo>();
			adminRoles.add(userRole);
			adminRoles.add(managerRole);
			adminRoles.add(adminRole);
			
			adminUser.setRoles(adminRoles);
			userServiceRemote.save(adminUser);
		}
		
		if (userServiceRemote.findByUsername(DefaultUserConstants.USER_MANAGER) == null) {
			UserVo managerUser = new UserVo();
			managerUser.setUsername(DefaultUserConstants.USER_MANAGER);
			managerUser.setPassword(bCryptPasswordEncoder.encode(DefaultUserConstants.USER_MANAGER));
			managerUser.setCompany(testCompany);
			
			List<RoleVo> managerRoles = new ArrayList<RoleVo>();
			managerRoles.add(userRole);
			managerRoles.add(managerRole);
			
			managerUser.setRoles(managerRoles);
			userServiceRemote.save(managerUser);
		}
		
		if (userServiceRemote.findByUsername(DefaultUserConstants.USER_USER) == null) {
			UserVo userUser = new UserVo();
			userUser.setUsername(DefaultUserConstants.USER_USER);
			userUser.setPassword(bCryptPasswordEncoder.encode(DefaultUserConstants.USER_USER));
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