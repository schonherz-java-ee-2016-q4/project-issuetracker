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
public class TicketVo extends BaseVo implements Serializable {
	private static final long serialVersionUID = -3081483409135655664L;

	private String uid;
	private String title;
	private String description;
	private String clientMail;
	private CompanyVo company;
	
	private TypeVo type;
	private UserVo user;
	private StatusVo currentStatus;
	private List<CommentVo> comments;
	private List<HistoryVo> history;
}
