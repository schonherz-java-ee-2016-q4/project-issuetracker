package hu.schonherz.javatraining.issuetracker.web.view.tickettype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import hu.schonherz.javatraining.issuetracker.client.api.service.history.HistoryServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@ManagedBean(name = "ticketHistoryView")
@ViewScoped
@Data
@Log4j
public class TicketHistoryView implements Serializable {
	
	@EJB
    private TicketServiceRemote ticketService;
	
    private Long ticketHistoryId;
    private TicketVo ticketHistory;
    
    @ManagedProperty(value = "#{searchTicketHistoryView}")
	private SearchTicketHistoryView searchTicketHistoryView;
	
    
    @PostConstruct
    public void init() {
    	
    	ticketHistory = new TicketVo();
    	log.debug("ticketHistoryView.init()");
    	ticketHistoryId = searchTicketHistoryView.getId();
		ticketHistory = ticketService.findById(ticketHistoryId);
    	
    }
    
    
}
