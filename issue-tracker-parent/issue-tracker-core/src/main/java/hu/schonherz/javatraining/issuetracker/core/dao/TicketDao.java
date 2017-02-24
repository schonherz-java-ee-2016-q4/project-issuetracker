package hu.schonherz.javatraining.issuetracker.core.dao;

import hu.schonherz.javatraining.issuetracker.core.entities.CompanyEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.TicketEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.TypeEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TicketDao extends JpaRepository<TicketEntity, Long> {
	TicketEntity findById(Long id);
	TicketEntity findByUid(String uid);
	List<TicketEntity> findByUser(UserEntity user);
	List<TicketEntity> findByType(TypeEntity type);
	List<TicketEntity> findByCompany(CompanyEntity company);

	@Query("SELECT ticket FROM TicketEntity ticket WHERE ticket.company = :company AND ticket.recDate <= :date")
	List<TicketEntity> getTicketsByCompanyAndTime(
			@Param("company") CompanyEntity company,
			@Param("date") Date date);
}

