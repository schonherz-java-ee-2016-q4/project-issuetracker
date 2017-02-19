package hu.schonherz.javatraining.issuetracker.service.impl.statusorder;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.statusorder.StatusOrderServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.statusorder.StatusOrderServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusOrderVo;
import hu.schonherz.javatraining.issuetracker.core.dao.StatusOrderDao;
import hu.schonherz.javatraining.issuetracker.core.entities.StatusOrderEntity;
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
	public List<StatusOrderVo> findByFromStatusId(Long id) {
		List<StatusOrderEntity> statusOrder = statusOrderDao.findByFromStatusId(id);
        return GenericVoMappers.statusOrderVoMapper.toVo(statusOrder);
	}

	@Override
	public List<StatusOrderVo> findByToStatusId(Long id) {
		List<StatusOrderEntity> statusOrder = statusOrderDao.findByToStatusId(id);
        return GenericVoMappers.statusOrderVoMapper.toVo(statusOrder);
	}

	@Override
	public StatusOrderVo save(StatusOrderVo statusOrder, String username) {
        statusOrder.setRecUserName(username);
        return GenericVoMappers.statusOrderVoMapper.toVo(statusOrderDao.save(GenericVoMappers.statusOrderVoMapper.toEntity(statusOrder)));
	}

	@Override
	public StatusOrderVo update(StatusOrderVo statusOrder, String username) {
        statusOrder.setModUserName(username);
        return GenericVoMappers.statusOrderVoMapper.toVo(statusOrderDao.save(GenericVoMappers.statusOrderVoMapper.toEntity(statusOrder)));
	}

	@Override
	public StatusOrderVo findByFromStatusIdAndToStatusId(Long fromId, Long toId) {
		StatusOrderEntity statusOrder = statusOrderDao.findByFromStatusIdAndToStatusId(fromId, toId);
        return GenericVoMappers.statusOrderVoMapper.toVo(statusOrder);
	}

}
