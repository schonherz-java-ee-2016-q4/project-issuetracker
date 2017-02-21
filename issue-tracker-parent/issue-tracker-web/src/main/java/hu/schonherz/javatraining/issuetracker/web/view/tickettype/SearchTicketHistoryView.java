package hu.schonherz.javatraining.issuetracker.web.view.tickettype;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Data;

@ManagedBean(name = "searchTicketHistoryView")
@ViewScoped
@Data
public class SearchTicketHistoryView implements Serializable {
	private Long id;

}
