package hu.schonherz.javatraining.issuetracker.core.dao;

import hu.schonherz.javatraining.issuetracker.core.entities.CommentEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS)
public interface CompanyDao extends JpaRepository<CompanyEntity, Long> {

    CompanyEntity findById(Long id);
    CompanyEntity findByName(String name);
}
