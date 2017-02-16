package hu.schonherz.javatraining.issuetracker.web.view;

import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "currentTicketView")
@ViewScoped
public class CurrentTicketView {

    @EJB
    private TicketServiceRemote ticketService;

    private Long currentTicketId;
    private TicketVo currentTicket;

    public TicketVo getCurrentTicket() {
        return currentTicket;
    }

    public void setCurrentTicket(TicketVo currentTicket) {
        this.currentTicket = currentTicket;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        currentTicketId = Long.valueOf(req.getParameter("id"));
        currentTicket = ticketService.findById(currentTicketId);
    }
}
