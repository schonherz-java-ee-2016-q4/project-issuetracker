package hu.schonherz.javatraining.issuetracker.service.impl.role;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
import hu.schonherz.javatraining.issuetracker.core.dao.RoleDao;
import hu.schonherz.javatraining.issuetracker.core.entities.RoleEntity;
import hu.schonherz.javatraining.issuetracker.service.mapper.role.RoleVoMapper;

@Stateless(mappedName = "RoleService")
@Remote(RoleServiceRemote.class)
@Local(RoleServiceLocal.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RoleServiceBean implements RoleServiceLocal, RoleServiceRemote {
	
	@Autowired
	RoleDao roleDao;

	@Override
	public RoleVo findByName(String name) {
		RoleEntity findByName = roleDao.findByName(name);
		return RoleVoMapper.toVo(findByName);
	}

}