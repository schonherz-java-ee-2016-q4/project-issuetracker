package hu.schonherz.javatraining.issuetracker.web.view;

import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "userTicketsView")
@ViewScoped
public class UserTicketsView implements Serializable {

    @EJB
    private UserServiceRemote userService;

    @EJB
    private TicketServiceRemote ticketService;

    private String userName;
    private UserVo currentUser;
    private List<TicketVo> tickets;

    public List<TicketVo> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketVo> tickets) {
        this.tickets = tickets;
    }

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        userName = request.getUserPrincipal().getName();
        currentUser = userService.findByUsername(userName);
        tickets = ticketService.findByUser(currentUser);
    }


}
