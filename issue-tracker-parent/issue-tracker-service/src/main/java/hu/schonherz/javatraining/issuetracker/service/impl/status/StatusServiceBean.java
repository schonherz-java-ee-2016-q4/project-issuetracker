package hu.schonherz.javatraining.issuetracker.service.impl.status;

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

import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.core.dao.StatusDao;
import hu.schonherz.javatraining.issuetracker.core.entities.StatusEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.UserEntity;
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
	public StatusVo save(StatusVo status) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        status.setRecUserId(user.getId());

        return GenericVoMappers.statusVoMapper.toVo(statusDao.save(GenericVoMappers.statusVoMapper.toEntity(status)));
	}

	@Override
	public StatusVo modify(StatusVo status) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        status.setModUserId(user.getId());

        return GenericVoMappers.statusVoMapper.toVo(statusDao.save(GenericVoMappers.statusVoMapper.toEntity(status)));
	}

	

}
