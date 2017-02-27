package hu.schonherz.javatraining.issuetracker.service.impl.ticket;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import hu.schonherz.javatraining.issuetracker.core.dao.TicketDao;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMappers;

@Stateless(mappedName = "TicketService")
@Local(TicketServiceLocal.class)
@Remote(TicketServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TicketServiceBean implements TicketServiceLocal, TicketServiceRemote {

    @Autowired
    private TicketDao ticketDao;

    @Override
    public TicketVo findById(Long id) {
        return GenericVoMappers.TICKET_VO_MAPPER.toVo(ticketDao.findById(id));
    }

    @Override
    public List<TicketVo> findByUser(UserVo user) {
        return GenericVoMappers.TICKET_VO_MAPPER.toVo(ticketDao.findByUser(GenericVoMappers.USER_VO_MAPPER.toEntity(user)));
    }

    @Override
    public List<TicketVo> findByType(TypeVo type) {
        return GenericVoMappers.TICKET_VO_MAPPER.toVo(ticketDao.findByType(GenericVoMappers.TYPE_VO_MAPPER.toEntity(type)));
    }

    @Override
    public List<TicketVo> findByCompany(CompanyVo company) {
        return GenericVoMappers.TICKET_VO_MAPPER.toVo(ticketDao.findByCompany(GenericVoMappers.COMPANY_VO_MAPPER.toEntity(company)));
    }

	@Override
	public List<TicketVo> getTicketsByCompanyAndBetweenTime(CompanyVo company, Date stepStart, Date stepEnd) {
		return GenericVoMappers.TICKET_VO_MAPPER.toVo(ticketDao.getTicketsByCompanyAndBetweenTime(GenericVoMappers.COMPANY_VO_MAPPER.toEntity(company),stepStart,stepEnd));
	}

	@Override
    public int getNumberOfTicketsByTypeAndStatus(TypeVo type, StatusVo currentStatus) {
        return ticketDao.getNumberOfTicketsByTypeAndStatus(
        		GenericVoMappers.TYPE_VO_MAPPER.toEntity(type),GenericVoMappers.STATUS_VO_MAPPER.toEntity(currentStatus));
    }

    @Override
    public List<TicketVo> findAll() {
        return GenericVoMappers.TICKET_VO_MAPPER.toVo(ticketDao.findAll());
    }

    @Override
    public TicketVo save(TicketVo ticket, String username) {
        ticket.setRecUserName(username);
        return GenericVoMappers.TICKET_VO_MAPPER.toVo(ticketDao.save(GenericVoMappers.TICKET_VO_MAPPER.toEntity(ticket)));
    }

    @Override
    public TicketVo update(TicketVo ticket, String username) {
        ticket.setModUserName(username);
        return GenericVoMappers.TICKET_VO_MAPPER.toVo(ticketDao.save(GenericVoMappers.TICKET_VO_MAPPER.toEntity(ticket)));
    }

	@Override
	public int getNumberOfClosedTicketsByUser(UserVo user) {
		return ticketDao.getNumberOfClosedTicketsByUser(GenericVoMappers.USER_VO_MAPPER.toEntity(user));
	}

	@Override
	public int getNumberOfOpenedTicketsByUser(UserVo user) {
		return ticketDao.getNumberOfOpenedTicketsByUser(GenericVoMappers.USER_VO_MAPPER.toEntity(user));
	}

	@Override
	public int getNumberOfCreatedTicketsByUser(String userName) {
		return ticketDao.getNumberOfCreatedTicketsByUser(userName);
	}

	@Override
	public int getNumberOfCreatedTicketsByUserBetweenTime(String userName, Date fromDate, Date untilDate) {
		return ticketDao.getNumberOfCreatedTicketsByUserBetweenTime(userName, fromDate, untilDate);
	}

	@Override
	public int getNumberOfCreatedTicketsByCompanyToday(CompanyVo company) {
		Date now = new Date();
		
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		Date fromDate = c.getTime();
		return ticketDao.getNumberOfCreatedTicketsByCompanyBetweenTime(
				GenericVoMappers.COMPANY_VO_MAPPER.toEntity(company), fromDate, now);
	}

	@Override
	public int getNumberOfCreatedTicketsByCompanyThisWeek(CompanyVo company) {
		Date now = new Date();
		
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		
		Date fromDate = c.getTime();
		return ticketDao.getNumberOfCreatedTicketsByCompanyBetweenTime(
				GenericVoMappers.COMPANY_VO_MAPPER.toEntity(company), fromDate, now);
	}

	@Override
	public int getNumberOfCreatedTicketsByCompanyThisMonth(CompanyVo company) {
		Date now = new Date();
		
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		
		Date fromDate = c.getTime();
		return ticketDao.getNumberOfCreatedTicketsByCompanyBetweenTime(
				GenericVoMappers.COMPANY_VO_MAPPER.toEntity(company), fromDate, now);
	}

}
