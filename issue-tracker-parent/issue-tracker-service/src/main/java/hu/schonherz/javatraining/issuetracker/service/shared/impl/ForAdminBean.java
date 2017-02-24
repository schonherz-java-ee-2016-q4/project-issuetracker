package hu.schonherz.javatraining.issuetracker.service.shared.impl;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import hu.schonherz.javatraining.issuetracker.shared.api.ForAdminRemote;
import hu.schonherz.javatraining.issuetracker.shared.vo.TicketCreationReportData;

@Stateless(mappedName = "ForAdmin")
@Remote(ForAdminRemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ForAdminBean implements ForAdminRemote {

	@Override
	public TicketCreationReportData getTicketCreationByCompanyReport(String companyName) {
		// TODO Auto-generated method stub
		return null;
	}

}
