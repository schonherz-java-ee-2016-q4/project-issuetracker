package hu.schonherz.javatraining.issuetracker.service.impl.ticket;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import hu.schonherz.javatraining.issuetracker.client.api.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.core.dao.TicketDao;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMapper;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.ejb.*;
import javax.interceptor.Interceptors;
import java.util.Date;
import java.util.List;

@Stateless(mappedName = "TicketService")
@Local(TicketServiceLocal.class)
@Remote(TicketServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TicketServiceBean implements TicketServiceLocal, TicketServiceRemote {

    @Autowired
    TicketDao ticketDao;

    @Override
    public TicketVo findById(Long id) {
        return GenericVoMappers.ticketVoMapper.toVo(ticketDao.findById(id));
    }

    @Override
    public TicketVo findByUid(String uid) {
        return GenericVoMappers.ticketVoMapper.toVo(ticketDao.findByUid(uid));
    }

    @Override
    public List<TicketVo> findByUser(UserVo user) {
        return GenericVoMappers.ticketVoMapper.toVo(ticketDao.findByUser(GenericVoMappers.userVoMapper.toEntity(user)));
    }

    @Override
    public List<TicketVo> findByType(TypeVo type) {
        return GenericVoMappers.ticketVoMapper.toVo(ticketDao.findByType(GenericVoMappers.typeVoMapper.toEntity(type)));
    }

    @Override
    public List<TicketVo> findByCompany(CompanyVo company) {
        return GenericVoMappers.ticketVoMapper.toVo(ticketDao.findByCompany(GenericVoMappers.companyVoMapper.toEntity(company)));
    }

    @Override
    public List<TicketVo> getTicketsByCompanyAndTime(CompanyVo company, Date date) {
        return GenericVoMappers.ticketVoMapper.toVo(ticketDao.getTicketsByCompanyAndTime(GenericVoMappers.companyVoMapper.toEntity(company), date));
    }

    @Override
    public int getNumberOfTicketsByTypeAndStatus(TypeVo type, StatusVo currentStatus) {
        return ticketDao.getNumberOfTicketsByTypeAndStatus(GenericVoMappers.typeVoMapper.toEntity(type),GenericVoMappers.statusVoMapper.toEntity(currentStatus));
    }

    @Override
    public List<TicketVo> findAll() {
        return GenericVoMappers.ticketVoMapper.toVo(ticketDao.findAll());
    }

    @Override
    public TicketVo save(TicketVo ticket, String username) {
        ticket.setRecUserName(username);
        return GenericVoMappers.ticketVoMapper.toVo(ticketDao.save(GenericVoMappers.ticketVoMapper.toEntity(ticket)));
    }

    @Override
    public TicketVo update(TicketVo ticket, String username) {
        ticket.setModUserName(username);
        return GenericVoMappers.ticketVoMapper.toVo(ticketDao.save(GenericVoMappers.ticketVoMapper.toEntity(ticket)));
    }

	@Override
	public int getNumberOfClosedTicketsByUser(UserVo user) {
		return ticketDao.getNumberOfClosedTicketsByUser(GenericVoMappers.userVoMapper.toEntity(user));
	}

	@Override
	public int getNumberOfOpenedTicketsByUser(UserVo user) {
		return ticketDao.getNumberOfOpenedTicketsByUser(GenericVoMappers.userVoMapper.toEntity(user));
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
	public int getNumberOfCreatedTicketsByCompanyBetweenTime(CompanyVo company, Date fromDate, Date untilDate) {
		return ticketDao.getNumberOfCreatedTicketsByCompanyBetweenTime(GenericVoMappers.companyVoMapper.toEntity(company), fromDate, untilDate);
	}

}
