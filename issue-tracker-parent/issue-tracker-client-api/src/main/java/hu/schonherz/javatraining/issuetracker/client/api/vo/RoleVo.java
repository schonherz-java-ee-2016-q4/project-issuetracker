package hu.schonherz.javatraining.issuetracker.client.api.vo;

import java.io.Serializable;

public class RoleVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = -5102285725479454910L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
