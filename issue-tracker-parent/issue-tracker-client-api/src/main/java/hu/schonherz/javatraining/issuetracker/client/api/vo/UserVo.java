package hu.schonherz.javatraining.issuetracker.client.api.vo;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = -2296591055910294732L;
	
	private String username;
	
	private String password;

	private List<RoleVo> roles;
}
