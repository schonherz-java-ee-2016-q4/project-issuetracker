package hu.schonherz.javatraining.issuetracker.service.impl.type;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.statusorder.StatusOrderServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusOrderVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.core.dao.TypeDao;
import hu.schonherz.javatraining.issuetracker.core.entities.CompanyEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.TypeEntity;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMappers;

@Stateless(mappedName = "TypeService")
@Local(TypeServiceLocal.class)
@Remote(TypeServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TypeServiceBean implements TypeServiceLocal, TypeServiceRemote {

	@Autowired
	private TypeDao typeDao;

	@EJB
	private StatusOrderServiceRemote statusOrderService;

	@EJB
	private StatusServiceRemote statusService;

	@Override
	public TypeVo findById(Long id) {
		return GenericVoMappers.TYPE_VO_MAPPER.toVo(typeDao.findById(id));
	}

	@Override
	public TypeVo findByNameAndCompany(String name, CompanyVo company) {
		CompanyEntity companyEntity = GenericVoMappers.COMPANY_VO_MAPPER.toEntity(company);
		return GenericVoMappers.TYPE_VO_MAPPER.toVo(typeDao.findByNameAndCompany(name, companyEntity));
	}

	@Override
	public TypeVo findByName(String name) {
		return GenericVoMappers.TYPE_VO_MAPPER.toVo(typeDao.findByName(name));
	}

	@Override
	public List<TypeVo> findByCompany(CompanyVo company) {
		return GenericVoMappers.TYPE_VO_MAPPER
				.toVo(typeDao.findByCompany(GenericVoMappers.COMPANY_VO_MAPPER.toEntity(company)));
	}

	@Override
	public List<TypeVo> findAll() {
		return GenericVoMappers.TYPE_VO_MAPPER.toVo(typeDao.findAll());
	}

	@Override
	public TypeVo save(TypeVo type, String username) {
		type.setRecUserName(username);
		TypeEntity typeEntity = GenericVoMappers.TYPE_VO_MAPPER.toEntity(type);
		return GenericVoMappers.TYPE_VO_MAPPER.toVo(typeDao.save(typeEntity));
	}
	
	@Override
	public void delete(TypeVo type) {
		typeDao.delete(GenericVoMappers.TYPE_VO_MAPPER.toEntity(type));
	}

	@Override
	public TypeVo update(TypeVo type, String username) {
		type.setModUserName(username);
		return GenericVoMappers.TYPE_VO_MAPPER.toVo(typeDao.save(GenericVoMappers.TYPE_VO_MAPPER.toEntity(type)));
	}

	@Override
	public List<StatusVo> getStatuses(TypeVo type) {
		if (type.getId() == null) {
			return null;
		}
		List<StatusVo> statuses = new ArrayList<>();

		statuses.add(type.getStartEntity());
		getStatusesFrom(type.getStartEntity(), statuses);

		return statuses;
	}

	private void getStatusesFrom(StatusVo status, List<StatusVo> statuses) {
		List<StatusOrderVo> fromStatuses = statusOrderService.findByFromStatusId(status.getId());
		boolean isNew;

		for (StatusOrderVo statusOrder : fromStatuses) {
			isNew = true;

			// check if already in our scope
			for (StatusVo statusInStatuses : statuses) {
				if (statusInStatuses.getId() == statusOrder.getToStatusId()) {
					isNew = false;
					break;
				}
			}

			if (isNew) {
				StatusVo newStatus = statusService.findById(statusOrder.getToStatusId());
				statuses.add(newStatus);
				getStatusesFrom(newStatus, statuses);
			}
		}

	}
}
