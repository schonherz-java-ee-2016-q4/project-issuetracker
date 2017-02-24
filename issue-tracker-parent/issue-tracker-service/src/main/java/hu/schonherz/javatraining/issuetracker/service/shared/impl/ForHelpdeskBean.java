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
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import hu.schonherz.javatraining.issuetracker.shared.api.ForHelpdeskRemote;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketData;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketsStatusReportData;
import lombok.extern.log4j.Log4j;


@Stateless(mappedName = "ForHelpdesk")
@Remote(ForHelpdeskRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Log4j
public class ForHelpdeskBean implements ForHelpdeskRemote {

	@EJB
	private TicketServiceLocal ticketService;
	
	@EJB
	private CompanyServiceLocal companyService;
	
	@EJB
	private TypeServiceLocal typeService;
	
	@EJB
	private UserServiceLocal userService;
	
	@Override
	public Boolean registerNewTicket(TicketData ticketData) {
		try {
			UserVo recUser = userService.findByUsername(ticketData.getRecUser());
			UserVo bindUser = null;
			
			if (ticketData.getBindedUser() != null && !"".equals(ticketData.getBindedUser())) {
				bindUser = userService.findByUsername(ticketData.getBindedUser());
			}
			
			CompanyVo company = companyService.findByName(ticketData.getCompanyName());
			TypeVo type = typeService.findByNameAndCompany(ticketData.getTicketTypeName(), company);
			
			TicketVo ticket = TicketVo.builder()
					.title(ticketData.getTicketName())
					.description(ticketData.getTicketDescription())
					.clientMail(ticketData.getClientMail())
					.company(company)
					.user(bindUser)
					.currentStatus(type.getStartEntity())
					.build();
			
			ticketService.save(ticket , recUser.getUsername());
			return true;
		}
		catch(Exception e) {
			log.error(e);
			return false;
		}
	}
	
	@Override
	public TicketsStatusReportData getTicketsStatusByuserReport(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNumberOfCreatedTicketsByUser(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNumberOfCreatedTicketsByUser(String userName, Date fromDate, Date untilDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getTypesByCompany(String companyName) {
		try {
			CompanyVo company = companyService.findByName(companyName);
			List<TypeVo> findByCompany = typeService.findByCompany(company);
			List<String> back = new ArrayList<>();
			findByCompany.forEach(x -> back.add(x.getName()));
			return back;
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}
	
}
