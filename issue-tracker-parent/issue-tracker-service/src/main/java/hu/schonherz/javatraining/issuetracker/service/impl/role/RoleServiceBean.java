package hu.schonherz.javatraining.issuetracker.service.impl.role;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
import hu.schonherz.javatraining.issuetracker.core.dao.RoleDao;
import hu.schonherz.javatraining.issuetracker.core.entities.RoleEntity;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMappers;

@Stateless(mappedName = "RoleService")
@Remote(RoleServiceRemote.class)
@Local(RoleServiceLocal.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Log4j
public class RoleServiceBean implements RoleServiceLocal, RoleServiceRemote {
	
	@Autowired
	RoleDao roleDao;

	@Override
	public RoleVo findByName(String name) {
		RoleEntity findByName = roleDao.findByName(name);
		log.debug(name + " - " + findByName);
		return GenericVoMappers.roleVoMapper.toVo(findByName);
	}

	@Override
	public RoleVo save(RoleVo role) {
		return GenericVoMappers.roleVoMapper.toVo(roleDao.save(GenericVoMappers.roleVoMapper.toEntity(role)));
	}

}
