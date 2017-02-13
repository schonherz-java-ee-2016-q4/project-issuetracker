package hu.schonherz.javatraining.issuetracker.core.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "status_order")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusOrderEntity extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long fromStatusId;
	private Long toStatusId;
}
