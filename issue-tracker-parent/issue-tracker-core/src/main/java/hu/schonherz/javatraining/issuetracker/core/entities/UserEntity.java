package hu.schonherz.javatraining.issuetracker.core.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	
	@ManyToOne
	private CompanyEntity company;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<RoleEntity> roles;

}
