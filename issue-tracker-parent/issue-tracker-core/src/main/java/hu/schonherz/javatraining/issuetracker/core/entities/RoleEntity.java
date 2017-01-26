package hu.schonherz.javatraining.issuetracker.core.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class RoleEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;

	public RoleEntity() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
