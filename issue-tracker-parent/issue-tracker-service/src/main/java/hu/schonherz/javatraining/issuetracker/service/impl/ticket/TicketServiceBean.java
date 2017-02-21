package hu.schonherz.javatraining.issuetracker.service.impl.ticket;

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
	public TicketVo save(TicketVo ticket, String username) {
		ticket.setRecUserName(username);
		return GenericVoMappers.ticketVoMapper.toVo(ticketDao.save(GenericVoMappers.ticketVoMapper.toEntity(ticket)));
	}

	@Override
	public TicketVo update(TicketVo ticket, String username) {
		ticket.setModUserName(username);
		return GenericVoMappers.ticketVoMapper.toVo(ticketDao.save(GenericVoMappers.ticketVoMapper.toEntity(ticket)));
	}

}
