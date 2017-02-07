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
public class TypeVo extends BaseVo implements Serializable {
	private static final long serialVersionUID = -9038194221865828685L;
	
	private String name;
	private String description;
	private CompanyVo company;
	private StatusVo startEntity;
}
