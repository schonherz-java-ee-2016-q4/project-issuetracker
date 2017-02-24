package hu.schonherz.javatraining.issuetracker.core.dao;

import hu.schonherz.javatraining.issuetracker.core.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hu.schonherz.javatraining.issuetracker.core.entities.UserEntity;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.SUPPORTS)
public interface UserDao extends JpaRepository<UserEntity, Long> {

	UserEntity findByUsername(String username);
	List<UserEntity> findAll();
	List<UserEntity> findAllByCompany(CompanyEntity companyEntity);
	UserEntity findById(Long id);
}
