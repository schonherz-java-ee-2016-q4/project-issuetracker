package hu.schonherz.javatraining.issuetracker.web.view.ticket;

import hu.schonherz.javatraining.issuetracker.client.api.service.comment.CommentServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CommentVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.web.beans.UserSessionBean;
import hu.schonherz.javatraining.issuetracker.web.view.tickettype.AddStatusView;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Data
@ManagedBean(name = "currentTicketView")
@ViewScoped
public class CurrentTicketView {

    @EJB
    private TicketServiceRemote ticketService;

    @EJB
    private CommentServiceRemote commentService;

    private Long currentTicketId;
    private TicketVo currentTicket;
    private Long id;

    @ManagedProperty(value = "#{addCommentView}")
    private AddCommentView addCommentView;

    @ManagedProperty("#{userSessionBean}")
    private UserSessionBean userSessionBean;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        currentTicketId = Long.valueOf(context.getExternalContext().getRequestParameterMap().get("currentTicketId"));
        currentTicket = ticketService.findById(currentTicketId);
    }

    public void addNewComment() {
        List<CommentVo> comments = currentTicket.getComments();
        comments.add(CommentVo.builder().commentText(addCommentView.getCommentText()).build());
        currentTicket.setComments(comments);
        ticketService.update(currentTicket, userSessionBean.getUserName());
    }
}
