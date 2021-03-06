package hu.schonherz.javatraining.issuetracker.core.entities;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@MappedSuperclass
@Data
public class BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String recUserName;
    private String modUserName;
    
	@Temporal(TemporalType.TIMESTAMP)
	private Date recDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modDate;

	@PrePersist
	public void prePersist() {
		recDate = new Date();

	}

	@PreUpdate
	public void preUpdate() {
		modDate = new Date();

	}
}
