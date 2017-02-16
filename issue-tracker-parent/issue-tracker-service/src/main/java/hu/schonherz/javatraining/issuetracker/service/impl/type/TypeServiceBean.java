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

import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.core.dao.TypeDao;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMappers;

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
		return GenericVoMappers.typeVoMapper.toVo(typeDao.findById(id));
	}

	@Override
	public TypeVo findByName(String name) {
        return GenericVoMappers.typeVoMapper.toVo(typeDao.findByName(name));
	}

	@Override
	public List<TypeVo> findByCompany(CompanyVo company) {
		return GenericVoMappers.typeVoMapper.toVo(typeDao.findByCompany(GenericVoMappers.companyVoMapper.toEntity(company)));
	}

	@Override
	public TypeVo save(TypeVo type, String username) {
		type.setRecUserName(username);
		return GenericVoMappers.typeVoMapper.toVo(typeDao.save(GenericVoMappers.typeVoMapper.toEntity(type)));
	}

	@Override
	public TypeVo update(TypeVo type, String username) {
		type.setModUserName(username);
		return GenericVoMappers.typeVoMapper.toVo(typeDao.save(GenericVoMappers.typeVoMapper.toEntity(type)));
	}
	@Override
	public List<TypeVo> findAll(){
		return GenericVoMappers.typeVoMapper.toVo(typeDao.findAll());
	}

}
