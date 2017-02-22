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
public class StatusVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private Boolean isEndStatus;
}
