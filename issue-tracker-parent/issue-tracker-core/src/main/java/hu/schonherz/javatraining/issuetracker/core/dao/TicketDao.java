package hu.schonherz.javatraining.issuetracker.core.dao;

import java.sql.Date;
import java.util.List;

import hu.schonherz.javatraining.issuetracker.core.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import hu.schonherz.javatraining.issuetracker.core.entities.TicketEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.TypeEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.UserEntity;

public interface TicketDao extends JpaRepository<TicketEntity, Long> {
	TicketEntity findById(Long id);
	TicketEntity findByUid(String uid);
	List<TicketEntity> findByUser(UserEntity user);
	List<TicketEntity> findByType(TypeEntity type);
	List<TicketEntity> findByCompany(CompanyEntity company);
	List<TicketEntity> findByDateWhereRecdateIsLowerThanDate(Date date);

}
