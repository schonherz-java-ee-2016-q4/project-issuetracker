package hu.schonherz.javatraining.issuetracker.web.view.ticket;

import hu.schonherz.javatraining.issuetracker.client.api.service.comment.CommentService;
import hu.schonherz.javatraining.issuetracker.client.api.service.comment.CommentServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CommentVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.web.beans.UserSessionBean;
import hu.schonherz.javatraining.issuetracker.web.view.tickettype.AddStatusView;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
    private List<CommentVo> comments;
    private CommentVo newComment;

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
        FacesContext context = FacesContext.getCurrentInstance();
        String recUserName;

        comments = currentTicket.getComments();
        newComment = CommentVo.builder()
                .commentText(addCommentView.getCommentText())
                .build();

        try {
            recUserName = userSessionBean.getUserName();
            commentService.save(newComment, recUserName);
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error in saving comment!");
            context.addMessage(null, facesMessage);
            return;
        }

        comments.add(newComment);
        currentTicket.setComments(comments);

        try {
            ticketService.update(currentTicket, recUserName);
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error in saving ticket!");
            context.addMessage(null, facesMessage);
            return;
        }
    }
}
