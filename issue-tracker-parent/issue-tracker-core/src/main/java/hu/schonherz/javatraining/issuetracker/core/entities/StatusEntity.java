package hu.schonherz.javatraining.issuetracker.core.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "status")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String description;

}
