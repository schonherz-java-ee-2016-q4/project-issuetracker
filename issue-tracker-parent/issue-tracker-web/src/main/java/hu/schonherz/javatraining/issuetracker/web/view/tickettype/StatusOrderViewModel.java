package hu.schonherz.javatraining.issuetracker.web.view.tickettype;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusOrderViewModel {
	private String from;
	private String to;
}
