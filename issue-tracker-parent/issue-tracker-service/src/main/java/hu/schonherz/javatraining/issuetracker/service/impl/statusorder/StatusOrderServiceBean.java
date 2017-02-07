package hu.schonherz.javatraining.issuetracker.service.impl.statusorder;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import hu.schonherz.javatraining.issuetracker.client.api.service.statusorder.StatusOrderServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.statusorder.StatusOrderServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusOrderVo;
import hu.schonherz.javatraining.issuetracker.core.dao.StatusOrderDao;
import hu.schonherz.javatraining.issuetracker.core.entities.StatusOrderEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.UserEntity;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMappers;

@Stateless(mappedName = "StatusOrderService")
@Local(StatusOrderServiceLocal.class)
@Remote(StatusOrderServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class StatusOrderServiceBean implements StatusOrderServiceLocal, StatusOrderServiceRemote {
	
	@Autowired
	private StatusOrderDao statusOrderDao;
	
	@Override
	public StatusOrderVo findByFromStatusId(Long id) {
		StatusOrderEntity statusOrder = statusOrderDao.findByFromStatusId(id);
        return GenericVoMappers.statusOrderVoMapper.toVo(statusOrder);
	}

	@Override
	public StatusOrderVo findByToStatusId(Long id) {
		StatusOrderEntity statusOrder = statusOrderDao.findByToStatusId(id);
        return GenericVoMappers.statusOrderVoMapper.toVo(statusOrder);
	}

	@Override
	public StatusOrderVo save(StatusOrderVo statusOrder) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        statusOrder.setRecUserId(user.getId());

        return GenericVoMappers.statusOrderVoMapper.toVo(statusOrderDao.save(GenericVoMappers.statusOrderVoMapper.toEntity(statusOrder)));
	}

	@Override
	public StatusOrderVo modify(StatusOrderVo statusOrder) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        statusOrder.setModUserId(user.getId());

        return GenericVoMappers.statusOrderVoMapper.toVo(statusOrderDao.save(GenericVoMappers.statusOrderVoMapper.toEntity(statusOrder)));
	}

}
