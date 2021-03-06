package hu.schonherz.javatraining.issuetracker.service.impl.history;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.history.HistoryServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.history.HistoryServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryVo;
import hu.schonherz.javatraining.issuetracker.core.dao.HistoryDao;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMappers;

@Stateless(mappedName = "HistoryService")
@Local(HistoryServiceLocal.class)
@Remote(HistoryServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class HistoryServiceBean implements HistoryServiceRemote, HistoryServiceLocal {

	@Autowired
	private HistoryDao historyDao;
	
	@Override
	public HistoryVo findById(Long id) {
		return GenericVoMappers.HISTORY_VO_MAPPER.toVo(historyDao.findById(id));
	}


	@Override
	public HistoryVo save(HistoryVo history, String username) {
		history.setRecUserName(username);
		return GenericVoMappers.HISTORY_VO_MAPPER.toVo(historyDao.save(GenericVoMappers.HISTORY_VO_MAPPER.toEntity(history)));
	}

	@Override
	public HistoryVo update(HistoryVo history, String username) {
		history.setModUserName(username);
		return GenericVoMappers.HISTORY_VO_MAPPER.toVo(historyDao.save(GenericVoMappers.HISTORY_VO_MAPPER.toEntity(history)));
	}

	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public HistoryVo saveInNewTransaction(HistoryVo history, String username) {
		history.setRecUserName(username);
		return GenericVoMappers.HISTORY_VO_MAPPER.toVo(historyDao.save(GenericVoMappers.HISTORY_VO_MAPPER.toEntity(history)));
	}

}
