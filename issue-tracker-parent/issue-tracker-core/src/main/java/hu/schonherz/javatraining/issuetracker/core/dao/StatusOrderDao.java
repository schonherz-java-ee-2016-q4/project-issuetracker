package hu.schonherz.javatraining.issuetracker.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hu.schonherz.javatraining.issuetracker.core.entities.StatusOrderEntity;

@Repository
@Transactional(propagation = Propagation.SUPPORTS)
public interface StatusOrderDao extends JpaRepository<StatusOrderEntity, Long> {
	
	StatusOrderEntity findByFromStatusId(Long fromStatusId);
	StatusOrderEntity findByToStatusId(Long toStatusId);
	StatusOrderEntity findByFromStatusIdAndToStatusId(Long fromStatusId, Long toStatusId);
}
