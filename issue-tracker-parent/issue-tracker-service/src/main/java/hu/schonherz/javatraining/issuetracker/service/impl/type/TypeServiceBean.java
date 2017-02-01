package hu.schonherz.javatraining.issuetracker.service.impl.type;

import java.util.List;

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

import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.core.dao.TypeDao;
import hu.schonherz.javatraining.issuetracker.core.entities.UserEntity;
import hu.schonherz.javatraining.issuetracker.service.mapper.company.CompanyVoMapper;
import hu.schonherz.javatraining.issuetracker.service.mapper.type.TypeVoMapper;

@Stateless(mappedName = "TypeService")
@Local(TypeServiceLocal.class)
@Remote(TypeServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TypeServiceBean implements TypeServiceLocal, TypeServiceRemote {

	@Autowired
	private TypeDao typeDao;

	@Override
	public TypeVo findById(Long id) {
		return TypeVoMapper.toVo(typeDao.findById(id));
	}

	@Override
	public TypeVo findByName(String name) {
        return TypeVoMapper.toVo(typeDao.findByName(name));
	}

	@Override
	public List<TypeVo> findByCompany(CompanyVo company) {
		return TypeVoMapper.toVo(typeDao.findByCompany(CompanyVoMapper.toEntity(company)));
	}

	@Override
	public TypeVo save(TypeVo type) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserEntity user = (UserEntity) auth.getPrincipal();
		type.setRecUserId(user.getId());

		return TypeVoMapper.toVo(typeDao.save(TypeVoMapper.toEntity(type)));
	}

	@Override
	public TypeVo update(TypeVo type) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserEntity user = (UserEntity) auth.getPrincipal();
		type.setModUserId(user.getId());

		return TypeVoMapper.toVo(typeDao.save(TypeVoMapper.toEntity(type)));
	}

}
