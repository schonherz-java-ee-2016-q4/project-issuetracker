package hu.schonherz.javatraining.issuetracker.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hu.schonherz.javatraining.issuetracker.core.entities.StatusEntity;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.SUPPORTS)
public interface StatusDao extends JpaRepository<StatusEntity, Long>{
	
	StatusEntity findByName(String name);
	StatusEntity findById(Long id);
	List<StatusEntity> findAll();
}
