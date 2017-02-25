package hu.schonherz.javatraining.issuetracker.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.schonherz.javatraining.issuetracker.core.entities.HistoryEntity;

public interface HistoryDao extends JpaRepository<HistoryEntity, Long> {
	HistoryEntity findById(Long id);
}
