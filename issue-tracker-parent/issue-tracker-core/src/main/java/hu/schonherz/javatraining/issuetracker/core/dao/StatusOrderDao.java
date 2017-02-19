package hu.schonherz.javatraining.issuetracker.core.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hu.schonherz.javatraining.issuetracker.core.entities.StatusOrderEntity;

@Repository
@Transactional(propagation = Propagation.SUPPORTS)
public interface StatusOrderDao extends JpaRepository<StatusOrderEntity, Long> {
	
	List<StatusOrderEntity> findByFromStatusId(Long fromStatusId);
	List<StatusOrderEntity> findByToStatusId(Long toStatusId);
}
