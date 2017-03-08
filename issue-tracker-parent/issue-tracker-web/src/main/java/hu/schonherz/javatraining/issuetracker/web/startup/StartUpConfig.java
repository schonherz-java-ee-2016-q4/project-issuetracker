package hu.schonherz.javatraining.issuetracker.web.startup;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.DefaultRoleConstants;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
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
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

}