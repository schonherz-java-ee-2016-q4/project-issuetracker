package hu.schonherz.javatraining.issuetracker.core.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.schonherz.javatraining.issuetracker.core.entities.TicketEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.TypeEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.UserEntity;

public interface TicketDao extends JpaRepository<TicketEntity, Long> {
	TicketEntity findById(Long id);
	TicketEntity findByUid(String uid);
	List<TicketEntity> findByUser(UserEntity user);
	List<TicketEntity> findByType(TypeEntity type);
	
	@Query("SELECT COUNT(ticket.id) FROM TicketEntity ticket WHERE ticket.user = :user AND ticket.currentStatus.isEndStatus = true")
	int getNumberOfClosedTicketsByUser(@Param("user") UserEntity user);
	
	@Query("SELECT COUNT(ticket.id) FROM TicketEntity ticket WHERE ticket.user = :user AND ticket.currentStatus.isEndStatus = false")
	int getNumberOfOpenedTicketsByUser(@Param("user") UserEntity user);
}
