package hu.schonherz.javatraining.issuetracker.service.shared.impl;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.shared.api.ForAdminServiceRemote;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketCreationReportData;
import lombok.extern.log4j.Log4j;

@Stateless(mappedName = "ForAdminService")
@Remote(ForAdminServiceRemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Log4j
public class ForAdminServiceBean implements ForAdminServiceRemote {

	@EJB
	private CompanyServiceLocal companyService;
	
	@EJB
	private TicketServiceLocal ticketService;
	
	@Override
	public TicketCreationReportData getTicketCreationByCompanyReport(String companyName) {
		try {
			CompanyVo company = companyService.findByName(companyName);
			if (company == null) {
				return null;
			}
			
			TicketCreationReportData back = new TicketCreationReportData();
			back.setToday(ticketService.getNumberOfCreatedTicketsByCompanyToday(company));
			back.setOnThisWeek(ticketService.getNumberOfCreatedTicketsByCompanyThisWeek(company));
			back.setOnThisMonth(ticketService.getNumberOfCreatedTicketsByCompanyThisMonth(company));
			
			log.debug(String.format("getTicketCreationByCompanyReport(%s)", companyName));
			log.debug(String.format("back: %s", back));
			
			return back;
		} catch(Exception e) {
			log.debug(e);
			return null;
		}
	}

}
