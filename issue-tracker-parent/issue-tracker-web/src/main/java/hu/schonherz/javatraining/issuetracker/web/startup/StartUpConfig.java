package hu.schonherz.javatraining.issuetracker.web.startup;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.history.HistoryServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.DefaultRoleConstants;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.statusorder.StatusOrderServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.*;
import lombok.extern.log4j.Log4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebListener
@Log4j
public class StartUpConfig implements ServletContextListener {

	@EJB
	UserServiceRemote userServiceRemote;

	@EJB
	RoleServiceRemote roleServiceRemote;

	@EJB
	CompanyServiceRemote companyServiceRemote;

	@EJB
	TypeServiceRemote  typeServiceRemote;

	@EJB
	StatusServiceRemote statusServiceRemote;

	@EJB
	StatusOrderServiceRemote statusOrderServiceRemote;

	@EJB
	TicketServiceRemote ticketServiceRemote;

	@EJB
	HistoryServiceRemote historyServiceRemote;

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

		StatusVo testStartStatus = statusServiceRemote.findByName("TestStartStatus");
		if (testStartStatus == null) {
			StatusVo statusVo = new StatusVo();
			statusVo.setName("TestStartStatus");
			statusVo.setDescription("This is a test start status.");
			statusVo.setIsEndStatus(false);
			testStartStatus = statusServiceRemote.save(statusVo, "admin");
		}

		StatusVo testMidStatus = statusServiceRemote.findByName("TestMidStatus");
		if (testMidStatus == null) {
			StatusVo statusVo2 = new StatusVo();
			statusVo2.setName("TestMidStatus");
			statusVo2.setDescription("This is a test 'in progress' status.");
			statusVo2.setIsEndStatus(false);
			statusServiceRemote.save(statusVo2, "admin");
		}

		StatusVo testEndStatus = statusServiceRemote.findByName("TestEndStatus");
		if (testEndStatus == null) {
			StatusVo statusVo3 = new StatusVo();
			statusVo3.setName("TestEndStatus");
			statusVo3.setDescription("This is a test end status.");
			statusVo3.setIsEndStatus(true);
			testEndStatus = statusServiceRemote.save(statusVo3, "admin");
		}

		StatusOrderVo testStatusOrder = statusOrderServiceRemote.findByFromStatusIdAndToStatusId
				(statusServiceRemote.findByName("TestStartStatus").getId(),
						statusServiceRemote.findByName("TestMidStatus").getId());
		if (testStatusOrder == null) {
            StatusOrderVo statusOrderVo = new StatusOrderVo();
			statusOrderVo.setFromStatusId(statusServiceRemote.findByName("TestStartStatus").getId());
			statusOrderVo.setToStatusId(statusServiceRemote.findByName("TestMidStatus").getId());
            statusOrderServiceRemote.save(statusOrderVo, "admin");
        }

		StatusOrderVo testStatusOrder2 = statusOrderServiceRemote.findByFromStatusIdAndToStatusId
				(statusServiceRemote.findByName("TestMidStatus").getId(),
						statusServiceRemote.findByName("TestEndStatus").getId());
		if (testStatusOrder2 == null) {
			StatusOrderVo statusOrderVo2 = new StatusOrderVo();
			statusOrderVo2.setFromStatusId(statusServiceRemote.findByName("TestMidStatus").getId());
			statusOrderVo2.setToStatusId(statusServiceRemote.findByName("TestEndStatus").getId());
			statusOrderServiceRemote.save(statusOrderVo2, "admin");
		}

		TypeVo testType = typeServiceRemote.findByName("TestType");
		if (testType == null) {
			TypeVo typeVo = new TypeVo();
			typeVo.setCompany(testCompany);
			typeVo.setDescription("This is a test type.");
			typeVo.setName("TestType");
			typeVo.setStartEntity(testStartStatus);
			testType = typeServiceRemote.save(typeVo,"admin");
		}

		HistoryVo historyVo = new HistoryVo();
		historyVo.setModStatus(HistoryEnum.CREATED);
		HistoryVo testHistory = historyServiceRemote.save(historyVo,"admin");
		List<HistoryVo> testHistoryList = new ArrayList<>();
		testHistoryList.add(testHistory);

		Date today = new Date();
		Date yesterday = new Date(today.getTime() - (1000 * 60 * 60 * 24));
		Date yesterday2 = new Date(today.getTime() - 2 * (1000 * 60 * 60 * 24));

		TicketVo ticketVo = new TicketVo();
		ticketVo.setHistory(testHistoryList);
		ticketVo.setComments(null);
		ticketVo.setTitle("TestTicket");
		ticketVo.setClientMail("test@ticket.test");
		ticketVo.setCompany(testCompany);
		ticketVo.setCurrentStatus(testEndStatus);
		ticketVo.setDescription("This is a test ticket.");
		ticketVo.setType(testType);
		ticketVo.setUid("testuid");
		ticketVo.setUser(null);
		ticketServiceRemote.save(ticketVo,"admin");

		TicketVo ticketVo2 = new TicketVo();
		ticketVo2.setHistory(testHistoryList);
		ticketVo2.setComments(null);
		ticketVo2.setTitle("TestTicket2");
		ticketVo2.setClientMail("test@ticket.test");
		ticketVo2.setCompany(testCompany);
		ticketVo2.setCurrentStatus(testEndStatus);
		ticketVo2.setDescription("This is a test ticket.");
		ticketVo2.setType(testType);
		ticketVo2.setUid("testuid");
		ticketVo2.setUser(null);
		ticketServiceRemote.save(ticketVo2,"admin");

		TicketVo ticketVo3 = new TicketVo();
		ticketVo3.setHistory(testHistoryList);
		ticketVo3.setComments(null);
		ticketVo3.setTitle("TestTicket");
		ticketVo3.setClientMail("test@ticket.test");
		ticketVo3.setCompany(testCompany);
		ticketVo3.setCurrentStatus(testStartStatus);
		ticketVo3.setDescription("This is a test ticket.");
		ticketVo3.setType(testType);
		ticketVo3.setUid("testuid");
		ticketVo3.setUser(null);
		ticketVo3.setRecDate(yesterday);
		ticketServiceRemote.save(ticketVo3,"admin");

		TicketVo ticketVo4 = new TicketVo();
		ticketVo4.setHistory(testHistoryList);
		ticketVo4.setComments(null);
		ticketVo4.setTitle("TestTicket");
		ticketVo4.setClientMail("test@ticket.test");
		ticketVo4.setCompany(testCompany);
		ticketVo4.setCurrentStatus(testStartStatus);
		ticketVo4.setDescription("This is a test ticket.");
		ticketVo4.setType(testType);
		ticketVo4.setUid("testuid");
		ticketVo4.setUser(null);
		ticketVo4.setRecDate(yesterday);
		ticketServiceRemote.save(ticketVo4,"admin");

		TicketVo ticketVo5 = new TicketVo();
		ticketVo5.setHistory(testHistoryList);
		ticketVo5.setComments(null);
		ticketVo5.setTitle("TestTicket");
		ticketVo5.setClientMail("test@ticket.test");
		ticketVo5.setCompany(testCompany);
		ticketVo5.setCurrentStatus(testStartStatus);
		ticketVo5.setDescription("This is a test ticket.");
		ticketVo5.setType(testType);
		ticketVo5.setUid("testuid");
		ticketVo5.setUser(null);
		ticketVo5.setRecDate(yesterday2);
		ticketServiceRemote.save(ticketVo5,"admin");

		TicketVo ticketVo6 = new TicketVo();
		ticketVo6.setHistory(testHistoryList);
		ticketVo6.setComments(null);
		ticketVo6.setTitle("TestTicket");
		ticketVo6.setClientMail("test@ticket.test");
		ticketVo6.setCompany(testCompany);
		ticketVo6.setCurrentStatus(testStartStatus);
		ticketVo6.setDescription("This is a test ticket.");
		ticketVo6.setType(testType);
		ticketVo6.setUid("testuid");
		ticketVo6.setUser(null);
		ticketVo6.setRecDate(yesterday2);
		ticketServiceRemote.save(ticketVo6,"admin");

		TicketVo ticketVo7 = new TicketVo();
		ticketVo7.setHistory(testHistoryList);
		ticketVo7.setComments(null);
		ticketVo7.setTitle("TestTicket");
		ticketVo7.setClientMail("test@ticket.test");
		ticketVo7.setCompany(testCompany);
		ticketVo7.setCurrentStatus(testStartStatus);
		ticketVo7.setDescription("This is a test ticket.");
		ticketVo7.setType(testType);
		ticketVo7.setUid("testuid");
		ticketVo7.setUser(null);
		ticketVo7.setRecDate(yesterday2);
		ticketServiceRemote.save(ticketVo7,"admin");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

}