package hu.schonherz.java.entities;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class Role extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;

	public Role() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
