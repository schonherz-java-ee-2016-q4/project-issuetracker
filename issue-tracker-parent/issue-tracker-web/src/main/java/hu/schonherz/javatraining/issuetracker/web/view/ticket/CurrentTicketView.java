package hu.schonherz.javatraining.issuetracker.web.view.ticket;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.javatraining.issuetracker.client.api.service.comment.CommentServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CommentVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.web.beans.UserSessionBean;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Data
@ManagedBean(name = "currentTicketView")
@ViewScoped
@Log4j
public class CurrentTicketView {

    @EJB
    private TicketServiceRemote ticketService;

    @EJB
    private CommentServiceRemote commentService;

    private Long currentTicketId;
    private TicketVo currentTicket;
    private HistoryListViewModel onehistory;
    private List<HistoryListViewModel> historyList;

    @ManagedProperty(value = "#{addCommentView}")
    private AddCommentView addCommentView;

    @ManagedProperty("#{userSessionBean}")
    private UserSessionBean userSessionBean;

    @ManagedProperty("#{mes}")
    private ResourceBundle bundle;


    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        currentTicketId = Long.valueOf(context.getExternalContext().getRequestParameterMap().get("currentTicketId"));
        currentTicket = ticketService.findById(currentTicketId);

        historyList = new ArrayList<>();
        String s = null;
        for(HistoryVo hvo : currentTicket.getHistory()) {
            switch (hvo.getModStatus()) {
                case CREATED: s = bundle.getString("ticket_history_created")+hvo.getRecUserName()+" "+bundle.getString("ticket_history_message")+" ";
                    onehistory = new HistoryListViewModel(s,  hvo.getRecDate()); break;
                case UPDATED: s = bundle.getString("ticket_history_update")+hvo.getRecUserName()+" "+bundle.getString("ticket_history_message")+" ";
                    onehistory = new HistoryListViewModel(s,  hvo.getRecDate()); break;
                case COMMENTED: s = bundle.getString("ticket_history_commeted")+hvo.getRecUserName()+" "+bundle.getString("ticket_history_message")+" ";
                    onehistory = new HistoryListViewModel(s,  hvo.getRecDate()); break;
                case CHANGED_STATUS: s = bundle.getString("ticket_history_changed_status")
                		+hvo.getRecUserName()+" "+bundle.getString("ticket_history_message")+" ";
                    onehistory = new HistoryListViewModel(s,  hvo.getRecDate()); break;
                default: break;
            }
            historyList.add(onehistory);
        }
    }

    public void addNewComment() {
        FacesContext context = FacesContext.getCurrentInstance();
        String recUserName = userSessionBean.getUserName();
        CommentVo newComment = CommentVo.builder()
        		.commentText(addCommentView.getCommentText())
        		.build();

        try {
        	newComment = commentService.save(newComment, recUserName);
        	log.debug(String.format("new comment saved: id: %s text: %s", newComment.getId(), newComment.getCommentText()));
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error in saving comment!");
            context.addMessage(null, facesMessage);
            return;
        }
        
        currentTicket.getComments().add(newComment);

        try {
        	currentTicket = ticketService.update(currentTicket, recUserName);
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error in saving ticket!");
            context.addMessage(null, facesMessage);
            return;
        }
        addCommentView.setCommentText(null);
    }
}
