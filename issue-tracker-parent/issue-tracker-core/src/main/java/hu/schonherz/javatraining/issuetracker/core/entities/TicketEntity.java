package hu.schonherz.javatraining.issuetracker.core.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -3385748756482915829L;

	private String uid;
	private String title;
	private String description;
	private String clientMail;
	
	@ManyToOne
	private TypeEntity type;
	
	@ManyToOne
	private StatusEntity currentStatus;
	
	@ManyToOne
	private UserEntity user;
	
	@ManyToMany
	private List<CommentEntity> comments;
	
	@ManyToMany
	private List<HistoryEntity> history;
}
