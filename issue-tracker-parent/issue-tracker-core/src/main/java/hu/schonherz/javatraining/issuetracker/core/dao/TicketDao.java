package hu.schonherz.javatraining.issuetracker.core.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.schonherz.javatraining.issuetracker.core.entities.CompanyEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.StatusEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.TicketEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.TypeEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.UserEntity;

public interface TicketDao extends JpaRepository<TicketEntity, Long> {
	TicketEntity findById(Long id);
	List<TicketEntity> findByUser(UserEntity user);
	List<TicketEntity> findByType(TypeEntity type);
	List<TicketEntity> findByCompany(CompanyEntity company);

	@Query("SELECT ticket FROM TicketEntity ticket WHERE ticket.company = :company AND ticket.recDate <= :date")
	List<TicketEntity> getTicketsByCompanyAndTime(
			@Param("company") CompanyEntity company,
			@Param("date") Date date);

	@Query("SELECT COUNT(ticket.id) FROM TicketEntity ticket WHERE ticket.type = :type AND ticket.currentStatus = :currentStatus")
	int getNumberOfTicketsByTypeAndStatus(
			@Param("type") TypeEntity type,
			@Param("currentStatus") StatusEntity currentStatus);

	
	@Query("SELECT COUNT(ticket.id) FROM TicketEntity ticket WHERE ticket.user = :user AND ticket.currentStatus.isEndStatus = true")
	int getNumberOfClosedTicketsByUser(@Param("user") UserEntity user);
	
	@Query("SELECT COUNT(ticket.id) FROM TicketEntity ticket WHERE ticket.user = :user AND ticket.currentStatus.isEndStatus = false")
	int getNumberOfOpenedTicketsByUser(@Param("user") UserEntity user);
	
	@Query("SELECT COUNT(ticket.id) FROM TicketEntity ticket WHERE ticket.recUserName = :recUserName")
	int getNumberOfCreatedTicketsByUser(@Param("recUserName") String recUserName);
	
	@Query("SELECT COUNT(ticket.id) FROM TicketEntity ticket WHERE ticket.recUserName = :recUserName AND ticket.recDate BETWEEN :fromDate AND :untilDate")
	int getNumberOfCreatedTicketsByUserBetweenTime(
			@Param("recUserName") String recUserName,
			@Param("fromDate") Date fromDate,
			@Param("untilDate") Date untilDate);
	
	@Query("SELECT COUNT(ticket.id) FROM TicketEntity ticket WHERE ticket.company = :company AND ticket.recDate BETWEEN :fromDate AND :untilDate")
	int getNumberOfCreatedTicketsByCompanyBetweenTime(
			@Param("company") CompanyEntity company,
			@Param("fromDate") Date fromDate,
			@Param("untilDate") Date untilDate);
}

