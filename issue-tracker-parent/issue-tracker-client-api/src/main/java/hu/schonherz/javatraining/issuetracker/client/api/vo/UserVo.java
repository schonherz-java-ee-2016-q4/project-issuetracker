package hu.schonherz.javatraining.issuetracker.client.api.vo;

import java.io.Serializable;
import java.util.List;

public class UserVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = -2296591055910294732L;
	
	private String username;
	
	private String password;

	private List<RoleVo> roles;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<RoleVo> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleVo> roles) {
		this.roles = roles;
	}
	
}
