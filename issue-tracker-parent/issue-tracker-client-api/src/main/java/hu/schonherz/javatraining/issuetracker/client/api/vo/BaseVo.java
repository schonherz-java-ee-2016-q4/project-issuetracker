package hu.schonherz.javatraining.issuetracker.client.api.vo;

import java.io.Serializable;

public class BaseVo implements Serializable {
	
	private static final long serialVersionUID = -6214960303946037900L;
	
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
