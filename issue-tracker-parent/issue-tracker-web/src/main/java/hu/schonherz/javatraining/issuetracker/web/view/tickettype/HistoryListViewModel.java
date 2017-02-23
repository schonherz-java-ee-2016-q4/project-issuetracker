package hu.schonherz.javatraining.issuetracker.web.view.tickettype;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoryListViewModel {
	private String change;
	private Date recDate;
}
