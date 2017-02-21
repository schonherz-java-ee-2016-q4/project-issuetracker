package hu.schonherz.javatraining.issuetracker.service.impl.status;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.core.dao.StatusDao;
import hu.schonherz.javatraining.issuetracker.core.entities.StatusEntity;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMappers;

@Stateless(mappedName = "StatusService")
@Local(StatusServiceLocal.class)
@Remote(StatusServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class StatusServiceBean implements StatusServiceLocal, StatusServiceRemote {

	@Autowired
	private StatusDao statusDao;

	@Override
	public StatusVo findById(Long id) {
		StatusEntity status = statusDao.findById(id);
        return GenericVoMappers.statusVoMapper.toVo(status);
	}
	
	@Override
	public StatusVo findByName(String name) {
		StatusEntity status = statusDao.findByName(name);
		return GenericVoMappers.statusVoMapper.toVo(status);
	}

	@Override
	public StatusVo save(StatusVo status, String username) {
        status.setRecUserName(username);
        return GenericVoMappers.statusVoMapper.toVo(statusDao.save(GenericVoMappers.statusVoMapper.toEntity(status)));
	}

	@Override
	public StatusVo update(StatusVo status, String username) {
        status.setModUserName(username);
        return GenericVoMappers.statusVoMapper.toVo(statusDao.save(GenericVoMappers.statusVoMapper.toEntity(status)));
	}

	@Override
	public List<StatusVo> findAll(){
		return GenericVoMappers.statusVoMapper.toVo(statusDao.findAll());
	}

	

}
