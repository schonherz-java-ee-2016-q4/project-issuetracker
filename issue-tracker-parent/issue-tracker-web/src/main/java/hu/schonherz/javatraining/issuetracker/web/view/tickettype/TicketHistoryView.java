package hu.schonherz.javatraining.issuetracker.web.view.tickettype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryEnum;
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
    
    @ManagedProperty("#{mes}")
	private ResourceBundle bundle;
    
    @ManagedProperty(value = "#{searchTicketHistoryView}")
	private SearchTicketHistoryView searchTicketHistoryView;
	
    private HistoryVo hv;
    private List<HistoryVo> hvlist;
    
    private List<HistoryListViewModel> historyList;
    private HistoryListViewModel onehistory;
    
    
    @PostConstruct
    public void init() {
    	
    	ticketHistory = new TicketVo();
    	log.debug("ticketHistoryView.init()");
    	
    	FacesContext context = FacesContext.getCurrentInstance();
    	
    	if(searchTicketHistoryView.getId() != null) {
    	ticketHistoryId = searchTicketHistoryView.getId();
    	
    	log.debug("ticketHistoryId: "+ticketHistoryId);
		ticketHistory = ticketService.findById(ticketHistoryId);
		/*Erra a hibaüzenetre csak akkor van szükség ha külön felületen lesz megjelenítve. Ellenkező esetben a detailsnél már kiderül,
		 * hogy van-e olyan ticket vagy sem és már akkor dóbódik hibaüzenet ha nincs.*/
		if(ticketHistory == null) {
    		context.addMessage(null, 
    				new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("ticket_search_error")));	
    	
    	} else {
    		/*Ez a rész csak azért kell, hogy a TicketVo-nak legyen List<HistoryVo> history-ban adata. Így megtudjuk, hogy az adott
    		 * ticketen mi változott, és mikor. Ez alapján íródik ki a szöveg. Erre a részre nem lesz szükség, ha már mentődik adat a
    		 * TicketVo history listájába is. */
			hv = new HistoryVo();
			hvlist = new ArrayList<HistoryVo>();
			hv = new HistoryVo(ticketHistory, HistoryEnum.CREATED);
			hv.setRecUserName("testuser0");
			hv.setRecDate(ticketHistory.getRecDate());
			log.debug("hvlist: "+hv.getTicket().getTitle());
			hvlist.add(hv);
			TicketVo tv = new TicketVo();
			tv.setId(ticketHistoryId);
			tv.setTitle(ticketHistory.getTitle());
			tv.setDescription("uj leiras");
			tv.setCompany(ticketHistory.getCompany());
			tv.setType(ticketHistory.getType());
			tv.setRecUserName(ticketHistory.getRecUserName());
			tv.setRecDate(ticketHistory.getRecDate());
			tv.setModUserName("moduser");
			tv.setModDate(ticketHistory.getRecDate());
			tv.setClientMail("uj cim");
			tv.setComments(ticketHistory.getComments());
			hv = new HistoryVo(tv, HistoryEnum.UPDATED);
			hv.setRecUserName("testuser1");
			hv.setRecDate(tv.getRecDate());
			hvlist.add(hv);
	    	ticketHistory.setHistory(hvlist);
    	
	    	/*Itt kerül összevágásra a szöveg. A dátumot külön fűztem hozzá.*/
	    	String s = null;
	    	historyList = new ArrayList<HistoryListViewModel>();
		    	for(HistoryVo hvo : ticketHistory.getHistory()) {
			    	switch (hvo.getModStatus()) {
				    	case CREATED: s = bundle.getString("ticket_history_created")+hvo.getRecUserName()+" "+bundle.getString("ticket_history_message")+" ";
				    				  onehistory = new HistoryListViewModel(s,  hvo.getRecDate()); break;
				    	case UPDATED: s = bundle.getString("ticket_history_update")+hvo.getRecUserName()+" "+bundle.getString("ticket_history_message")+" ";
				    				  onehistory = new HistoryListViewModel(s,  hvo.getRecDate()); break;
				    	case COMMENTED: s = bundle.getString("ticket_history_commeted")+hvo.getRecUserName()+" "+bundle.getString("ticket_history_message")+" ";
				    				    onehistory = new HistoryListViewModel(s,  hvo.getRecDate()); break;
				    	case CHANGED_STATUS: s = bundle.getString("ticket_history_changed_status")+hvo.getRecUserName()+" "+bundle.getString("ticket_history_message")+" ";
				    						 onehistory = new HistoryListViewModel(s,  hvo.getRecDate()); break;
				    	default: ; break;
			    	}
			    	historyList.add(onehistory);
		    	}
	    	}
    	}
    	 
    }
    
    
}
