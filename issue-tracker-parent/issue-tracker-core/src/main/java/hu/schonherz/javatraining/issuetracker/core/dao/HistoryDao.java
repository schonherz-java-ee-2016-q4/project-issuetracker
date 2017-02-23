package hu.schonherz.javatraining.issuetracker.core.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.schonherz.javatraining.issuetracker.core.entities.HistoryEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.TicketEntity;

public interface HistoryDao extends JpaRepository<HistoryEntity, Long> {
	HistoryEntity findById(Long id);
}
