package hu.schonherz.javatraining.issuetracker.client.api.vo;

import java.io.Serializable;

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
public class RoleVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = -5102285725479454910L;
	
	private String name;

}
