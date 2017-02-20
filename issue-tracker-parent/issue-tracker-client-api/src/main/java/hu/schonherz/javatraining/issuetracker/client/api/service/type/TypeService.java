package hu.schonherz.javatraining.issuetracker.client.api.service.type;

import java.util.List;

import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;

public interface TypeService {
	TypeVo findById(Long id);
	TypeVo findByName(String name);
	TypeVo findByNameAndCompany(String name, CompanyVo company);
	List<TypeVo> findAll();
	List<TypeVo> findByCompany(CompanyVo company);
	TypeVo save(TypeVo type, String username);
	TypeVo update(TypeVo type, String username);
}
