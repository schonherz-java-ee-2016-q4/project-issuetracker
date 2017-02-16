package hu.schonherz.javatraining.issuetracker.core.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.schonherz.javatraining.issuetracker.core.entities.CompanyEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.TypeEntity;

public interface TypeDao extends JpaRepository<TypeEntity, Long> {
	TypeEntity findById(Long id);
	TypeEntity findByName(String name);
	List<TypeEntity> findAll();
	List<TypeEntity> findByCompany(CompanyEntity company);
}
