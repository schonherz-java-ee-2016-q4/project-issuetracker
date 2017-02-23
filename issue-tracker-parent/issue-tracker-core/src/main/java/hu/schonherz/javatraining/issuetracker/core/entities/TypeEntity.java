package hu.schonherz.javatraining.issuetracker.core.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "type")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 7208968204790694623L;

	private String name;
	
	@Lob
	private String description;
	
	@ManyToOne
	private CompanyEntity company;
	
	@ManyToOne
	private StatusEntity startEntity;
}
