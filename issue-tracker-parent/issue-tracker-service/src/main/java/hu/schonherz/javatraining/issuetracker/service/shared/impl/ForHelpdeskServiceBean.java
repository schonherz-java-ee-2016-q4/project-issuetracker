package hu.schonherz.javatraining.issuetracker.service.shared.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.history.HistoryServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.DefaultTicketQuotasConstants;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.shared.AdminJNDIConstants;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryEnum;
import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import hu.schonherz.javatraining.issuetracker.shared.api.ForHelpdeskServiceRemote;
import hu.schonherz.javatraining.issuetracker.shared.vo.QuotaReachedException;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketData;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketsStatusReportData;
import hu.schonherz.project.remote.admin.api.rpc.issuetracker.RemoteLoginService;
import hu.schonherz.project.remote.admin.api.rpc.issuetracker.RemoteQuotasService;
import hu.schonherz.project.remote.admin.api.vo.issuetracker.RemoteQuotasVo;
import hu.schonherz.project.remote.admin.api.vo.issuetracker.RemoteUserVo;
import lombok.extern.log4j.Log4j;

@Stateless(mappedName = "ForHelpdeskService")
@Remote(ForHelpdeskServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Log4j
public class ForHelpdeskServiceBean implements ForHelpdeskServiceRemote {

	@EJB
	private TicketServiceLocal ticketService;

	@EJB
	private CompanyServiceLocal companyService;

	@EJB
	private TypeServiceLocal typeService;

	@EJB
	private UserServiceLocal userService;

	@EJB
	private HistoryServiceLocal historyService;

	@Override
	public Boolean registerNewTicket(TicketData ticketData) throws QuotaReachedException {
		log.debug(String.format("registerNewTicket(%s)", ticketData));
		try {
			if (ticketData.getTicketName().length() > 30) {
				log.debug(String.format("too long ticket name in registerNewTicket: %s", ticketData.getTicketName()));
				return false;
			}

			UserVo recUser;
			try {
				recUser = getUserDataFromAdmin(ticketData.getRecUser());
			} catch (NameNotFoundException e) {
				recUser = userService.findByUsername(ticketData.getRecUser());
			}
			if (recUser == null) {
				log.debug(String.format("invalid rec username in registerNewTicket: %s", ticketData.getRecUser()));
				return false;
			}

			UserVo bindUser = null;
			if (ticketData.getBindedUser() != null && !"".equals(ticketData.getBindedUser())) {
				try {
					bindUser = getUserDataFromAdmin(ticketData.getBindedUser());
				} catch (NameNotFoundException e) {
					bindUser = userService.findByUsername(ticketData.getBindedUser());
				}
			}

			CompanyVo company = companyService.findByName(ticketData.getCompanyName());
			if (company == null) {
				log.debug(String.format("invalid company name in registerNewTicket: %s", ticketData.getCompanyName()));
				return false;
			}

			if (!canCreateNewTicket(company)) {
				throw new QuotaReachedException();
			}

			TypeVo type = typeService.findByNameAndCompany(ticketData.getTicketTypeName(), company);
			if (type == null) {
				log.debug(String.format("invalid type name in registerNewTicket: %s", ticketData.getTicketTypeName()));
				return false;
			}

			HistoryVo createdHistory = HistoryVo.builder().modStatus(HistoryEnum.CREATED).build();
			createdHistory = historyService.save(createdHistory, recUser.getUsername());
			List<HistoryVo> history = new ArrayList<>();
			history.add(createdHistory);

			TicketVo ticket = TicketVo.builder().title(ticketData.getTicketName())
					.description(ticketData.getTicketDescription()).clientMail(ticketData.getClientMail())
					.company(company).user(bindUser).currentStatus(type.getStartEntity()).type(type).history(history)
					.build();
			log.debug(String.format("prepare to save: ", ticket));
			ticket = ticketService.save(ticket, recUser.getUsername());
			log.debug(String.format("succesfully saved: %s", ticket));
			return true;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	@Override
	public TicketsStatusReportData getTicketsStatusByuserReport(String userName) {
		try {
			UserVo user = userService.findByUsername(userName);
			TicketsStatusReportData back = new TicketsStatusReportData();
			back.setClosedTickets(ticketService.getNumberOfClosedTicketsByUser(user));
			back.setOpenedTickets(ticketService.getNumberOfOpenedTicketsByUser(user));
			log.debug(String.format("getTicketsStatusByuserReport(%s)", userName));
			log.debug(String.format("back: %s", back));
			return back;
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	@Override
	public Integer getNumberOfCreatedTicketsByUser(String userName) {
		try {
			Integer back = ticketService.getNumberOfCreatedTicketsByUser(userName);
			log.debug(String.format("getNumberOfCreatedTicketsByUser(%s)", userName));
			log.debug(String.format("back: %s", back));
			return back;
		} catch (Exception e) {
			log.debug(e);
			return null;
		}
	}

	@Override
	public Integer getNumberOfCreatedTicketsByUser(String userName, Date fromDate, Date untilDate) {
		try {
			Integer back = ticketService.getNumberOfCreatedTicketsByUserBetweenTime(userName, fromDate, untilDate);
			log.debug(String.format("getNumberOfCreatedTicketsByUser(%s, %s, %s)", userName, fromDate, untilDate));
			log.debug(String.format("back: %s", back));
			return back;
		} catch (Exception e) {
			log.debug(e);
			return null;
		}
	}

	@Override
	public List<String> getTypesByCompany(String companyName) {
		try {
			CompanyVo company = companyService.findByName(companyName);
			if (company == null) {
				log.debug(String.format("invalid company name in getTypesByCompany: %s", companyName));
				return null;
			}

			List<TypeVo> findByCompany = typeService.findByCompany(company);
			List<String> back = new ArrayList<>();
			findByCompany.forEach(x -> back.add(x.getName()));
			log.debug(String.format("getTypesByCompany(%s)", companyName));
			log.debug(String.format("back: %s", back));
			return back;
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	private UserVo getUserDataFromAdmin(String username) throws NamingException {
		UserVo user;
		Context context = new InitialContext();
		log.debug("get login service context");
		RemoteLoginService adminLoginService = (RemoteLoginService) context
				.lookup(AdminJNDIConstants.JNDI_LOGIN_SERVICE);
		RemoteUserVo remoteUserVo = adminLoginService.login(username);
		user = userService.mapRemoteUserVoToUserVo(remoteUserVo);
		if (user.getId() == null) {
			user = userService.save(user);
		}
		return user;
	}

	private boolean canCreateNewTicket(CompanyVo company) {
		int numberOfTicketsOnCompanyDaily = ticketService.getNumberOfCreatedTicketsByCompanyToday(company);
		int numberOfTicketsOnCompanyWeekly = ticketService.getNumberOfCreatedTicketsByCompanyThisWeek(company);
		int numberOfTicketsOnCompanyMonthly = ticketService.getNumberOfCreatedTicketsByCompanyThisMonth(company);

		int dailyMax = DefaultTicketQuotasConstants.DAILY_MAX_NUMBER_OF_TICKET_ON_COMPANY;
		int weeklyMax = DefaultTicketQuotasConstants.WEEKLY_MAX_NUMBER_OF_TICKET_ON_COMPANY;
		int monthlyMax = DefaultTicketQuotasConstants.MONTHLY_MAX_NUMBER_OF_TICKET_ON_COMPANY;

		try {
			Context contextForEJB = new InitialContext();
			log.debug("get admin qoutas service context");
			RemoteQuotasService adminQoutaService = (RemoteQuotasService) contextForEJB.lookup(AdminJNDIConstants.JNDI_QOUTAS_SERVICE);
			RemoteQuotasVo quotasOfCompany = adminQoutaService.getQuotasOfCompany(company.getName());
			dailyMax = quotasOfCompany.getMaxDayTickets();
			weeklyMax = quotasOfCompany.getMaxWeekTickets();
			monthlyMax = quotasOfCompany.getMaxMonthTickets();
		} catch (NamingException e) {
		}

		return numberOfTicketsOnCompanyDaily < dailyMax && numberOfTicketsOnCompanyWeekly < weeklyMax
				&& numberOfTicketsOnCompanyMonthly < monthlyMax;
	}
}
