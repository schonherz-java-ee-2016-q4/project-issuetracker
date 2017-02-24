package hu.schonherz.javatraining.issuetracker.service.shared.impl;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.shared.api.ForAdminRemote;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketCreationReportData;
import lombok.extern.log4j.Log4j;

@Stateless(mappedName = "ForAdmin")
@Remote(ForAdminRemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Log4j
public class ForAdminBean implements ForAdminRemote {

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
			
			Date now = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(now);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			
			Date today = c.getTime();
			back.setToday(ticketService.getNumberOfCreatedTicketsByCompanyBetweenTime(company, today, now));
			
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			Date thisWeek = c.getTime();
			back.setOnThisWeek(ticketService.getNumberOfCreatedTicketsByCompanyBetweenTime(company, thisWeek, now));
			
			c.set(Calendar.DAY_OF_MONTH, 1);
			Date thisMonth = c.getTime();
			back.setOnThisMonth(ticketService.getNumberOfCreatedTicketsByCompanyBetweenTime(company, thisMonth, now));
			
			log.debug(String.format("getTicketCreationByCompanyReport(%s)", companyName));
			log.debug(String.format("back: %s", back));
			
			return back;
		} catch(Exception e) {
			log.debug(e);
			return null;
		}
	}

}
