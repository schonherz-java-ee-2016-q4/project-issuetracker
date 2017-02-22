package hu.schonherz.javatraining.issuetracker.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hu.schonherz.javatraining.issuetracker.core.entities.CompanyEntity;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.SUPPORTS)
public interface CompanyDao extends JpaRepository<CompanyEntity, Long> {

    CompanyEntity findById(Long id);
    CompanyEntity findByName(String name);
    List<CompanyEntity> findAll();
}
