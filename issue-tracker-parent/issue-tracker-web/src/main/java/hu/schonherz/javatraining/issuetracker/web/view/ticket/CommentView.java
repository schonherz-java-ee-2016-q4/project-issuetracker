package hu.schonherz.javatraining.issuetracker.web.view.ticket;

import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CommentVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.web.beans.UserSessionBean;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "commentView")
@ViewScoped
@Data
public class CommentView {
    private List<CommentVo> comments;

    @EJB
    private TicketServiceRemote ticketService;

    @ManagedProperty(value = "#{currentTicketView}")
    private CurrentTicketView currentTicketView;

    @PostConstruct
    public void init() {
        comments = new ArrayList<>();
        comments = currentTicketView.getCurrentTicket().getComments();
    }
}
