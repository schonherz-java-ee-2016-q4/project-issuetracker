package hu.schonherz.javatraining.issuetracker.core.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "history")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -7990882977612154982L;

	@ManyToOne
	private TicketEntity ticket;
	
	@Enumerated(EnumType.STRING)
	private HistoryEnum modStatus;
}
