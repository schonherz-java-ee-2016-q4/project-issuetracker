package hu.schonherz.javatraining.issuetracker.service.impl.user;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import hu.schonherz.javatraining.issuetracker.core.dao.UserDao;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMappers;

import java.util.List;


@Stateless(mappedName = "UserService")
@Local(UserServiceLocal.class)
@Remote(UserServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserServiceBean implements UserServiceRemote, UserServiceLocal {

	@Autowired
	private UserDao userDao;

	@Override
	public List<UserVo> findAll(){

        return GenericVoMappers.userVoMapper.toVo(userDao.findAll());

	}

	@Override
	public List<UserVo> findAllByCompany(CompanyVo companyVo){
        return GenericVoMappers.userVoMapper.toVo(userDao.findAllByCompany(GenericVoMappers.companyVoMapper.toEntity(companyVo)));

	}

	@Override
	public UserVo findById(Long id)
	{
		return GenericVoMappers.userVoMapper.toVo(userDao.findById(id));
	}

	@Override
	public UserVo findByUsername(String username) {
		return GenericVoMappers.userVoMapper.toVo(userDao.findByUsername(username));
	}

	@Override
	public UserVo save(UserVo user) {
		return GenericVoMappers.userVoMapper.toVo(userDao.save(GenericVoMappers.userVoMapper.toEntity(user)));
	}

}
