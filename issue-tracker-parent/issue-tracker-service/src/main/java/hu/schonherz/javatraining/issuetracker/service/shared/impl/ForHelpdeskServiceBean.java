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

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.history.HistoryServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryEnum;
import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import hu.schonherz.javatraining.issuetracker.shared.api.ForHelpdeskServiceRemote;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketData;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketsStatusReportData;
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
	public Boolean registerNewTicket(TicketData ticketData) {
		log.debug(String.format("registerNewTicket(%s)", ticketData));
		try {
			if (ticketData.getTicketName().length() > 30) {
				return false;
			}
			
			UserVo recUser = userService.findByUsername(ticketData.getRecUser());
			
			if (recUser == null) {
				return false;
			}
			
			UserVo bindUser = null;
			
			if (ticketData.getBindedUser() != null && !"".equals(ticketData.getBindedUser())) {
				bindUser = userService.findByUsername(ticketData.getBindedUser());
			}
			
			CompanyVo company = companyService.findByName(ticketData.getCompanyName());
			TypeVo type = typeService.findByNameAndCompany(ticketData.getTicketTypeName(), company);
			
			HistoryVo createdHistory = HistoryVo.builder()
					.modStatus(HistoryEnum.CREATED)
					.build();
			createdHistory = historyService.save(createdHistory, recUser.getUsername());
			List<HistoryVo> history = new ArrayList<>();
			history.add(createdHistory);
			
			TicketVo ticket = TicketVo.builder()
					.title(ticketData.getTicketName())
					.description(ticketData.getTicketDescription())
					.clientMail(ticketData.getClientMail())
					.company(company)
					.user(bindUser)
					.currentStatus(type.getStartEntity())
					.type(type)
					.history(history)
					.build();
			log.debug(String.format("prepare to save: ", ticket));
			ticket = ticketService.save(ticket , recUser.getUsername());
			log.debug(String.format("succesfully saved: %s", ticket));
			return true;
		} catch(Exception e) {
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
		} catch(Exception e) {
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
		} catch(Exception e) {
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
		} catch(Exception e) {
			log.debug(e);
			return null;
		}
	}

	@Override
	public List<String> getTypesByCompany(String companyName) {
		try {
			CompanyVo company = companyService.findByName(companyName);
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
	
}
