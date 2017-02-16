package hu.schonherz.javatraining.issuetracker.web.view;


import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryEnum.CREATED;

@ManagedBean(name = "ticketCreateModifyView")
@ViewScoped
public class TicketCreateModifyView implements Serializable{

    @ManagedProperty("#{mes}")
    private ResourceBundle bundle;
    private String recUserName;
    private String uid;
    private String threeLetterCompanyID;
    private String title;
    private String description;
    private String clientMail;
    private String companyName;
    private String statusName;

    private TypeVo type;
    private UserVo user;
    private List<CommentVo> comments;
    private List<HistoryVo> history;
    private TicketVo ticketVo;


    @EJB
    private TicketServiceLocal ticketServiceLocal;
    @EJB
    private CompanyServiceLocal companyServiceLocal;
    @EJB
    private StatusServiceLocal statusServiceLocal;


    @PostConstruct
    public void init() {
       ticketVo = new TicketVo();

        history = new ArrayList<>();
        history.add(HistoryVo.builder().ticket(ticketVo).modStatus(CREATED).build());

        comments = new ArrayList<>();
        comments.add(CommentVo.builder().commentText(bundle.getString("ticket_no_comment")));

    }

    public void addTicket()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        log.debug("mentes");

        threeLetterCompanyID=CompanyVo.getName().substring(0,2);
        threeLetterCompanyID.toUpperCase();
        uid=threeLetterCompanyID+String.valueOf(user.getId());

        if ("".equals(uid)) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "longerroruid");
            context.addMessage(null, facesMessage);
            return;
        }

        if("".equals(companyName))
        {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "companyerror");
            context.addMessage(null, facesMessage);
            return;
        }


        if("".equals(statusName))
        {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "statuserror");
            context.addMessage(null, facesMessage);
            return;
        }

        ticketVo.builder()
                .uid(getUid())
                .title(getTitle())
                .description(getDescription())
                .clientMail(getClientMail())
                .companyVo(companyServiceLocal.findByName(companyName))
                .type(getType())
                .currentStatus(statusServiceLocal.findByName(statusName))
                .commets(getComments())
                .history(getHistory())
                .build();

        try{
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            recUserName = request.getUserPrincipal().getName();
            ticketServiceLocal.save(ticketVo, recUserName);
        }
        catch (Exception e)
        {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "ticketerror!");
            context.addMessage(null, facesMessage);
            return;
            log.error("Nem sikerült menteni a ticketet",e.printStackTrace());
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientMail() {
        return clientMail;
    }

    public void setClientMail(String clientMail) {
        this.clientMail = clientMail;
    }

    public TypeVo getType() {
        return type;
    }

    public void setType(TypeVo type) {
        this.type = type;
    }

    public UserVo getUser() {
        return user;
    }

    public void setUser(UserVo user) {
        this.user = user;
    }

    public StatusVo getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(StatusVo currentStatus) {
        this.currentStatus = currentStatus;
    }

    public List<CommentVo> getComments() {
        return comments;
    }

    public void setComments(List<CommentVo> comments) {
        this.comments = comments;
    }

    public List<HistoryVo> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryVo> history) {
        this.history = history;
    }

    public TicketServiceLocal getTicketServiceLocal() {
        return ticketServiceLocal;
    }

    public void setTicketServiceLocal(TicketServiceLocal ticketServiceLocal) {
        this.ticketServiceLocal = ticketServiceLocal;
    }

    public CompanyVo getCompanyVo() {
        return companyVo;
    }

    public void setCompanyVo(CompanyVo companyVo) {
        this.companyVo = companyVo;
    }

}
