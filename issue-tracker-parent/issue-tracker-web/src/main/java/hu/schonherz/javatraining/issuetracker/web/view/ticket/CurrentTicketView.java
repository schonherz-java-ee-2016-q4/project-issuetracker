package hu.schonherz.javatraining.issuetracker.web.view.ticket;

import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@Data
@ManagedBean(name = "currentTicketView")
@ViewScoped
public class CurrentTicketView {

    @EJB
    private TicketServiceRemote ticketService;

    private Long currentTicketId;
    private TicketVo currentTicket;
    private Long id;

    @PostConstruct
    public void init() {
//        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        currentTicketId = Long.valueOf(req.getParameter("id"));
        currentTicket = ticketService.findById(currentTicketId);
    }

    public void goToTicketDetails() {
        currentTicketId = id;
    }
}
