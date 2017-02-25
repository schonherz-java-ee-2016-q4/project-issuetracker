package hu.schonherz.javatraining.issuetracker.web.view.ticket;

import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import hu.schonherz.javatraining.issuetracker.web.beans.UserSessionBean;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@Data
@ManagedBean(name = "userTicketsView")
@ViewScoped
public class UserTicketsView implements Serializable {

    @ManagedProperty(value = "#{userSessionBean}")
    private UserSessionBean userSessionBean;

    @EJB
    private UserServiceRemote userService;

    @EJB
    private TicketServiceRemote ticketService;

    private UserVo currentUser;
    private List<TicketVo> tickets;

    @PostConstruct
    public void init() {
        if (userSessionBean.isAdmin() || userSessionBean.isManager()) {
            tickets = ticketService.findAll();
        } else {
            tickets = ticketService.findByUser(userSessionBean.getCurrentUser());
        }
    }
}
