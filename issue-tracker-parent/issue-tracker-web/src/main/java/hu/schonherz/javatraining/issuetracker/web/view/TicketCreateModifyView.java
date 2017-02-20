package hu.schonherz.javatraining.issuetracker.web.view;


import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.*;
import lombok.extern.log4j.Log4j;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import static hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryEnum.CREATED;

@ManagedBean(name = "ticketCreateModifyView")
@ViewScoped
@Log4j
public class TicketCreateModifyView implements Serializable{



    private String recUserName;
    private String uid;
    private String threeLetterCompanyID;
    private String title;
    private String description;
    private String clientMail;


    private String companyName;
    private String statusName;
    private String typeName;

    private UserVo user;
    private List<CommentVo> comments;
    private List<HistoryVo> history;
    private TicketVo ticketVo;



    private List<CompanyVo> companies;
    private List<TypeVo> types;
    private List<StatusVo> statuses;


    @EJB
    private TicketServiceRemote ticketServiceRemote;
    @EJB
    private CompanyServiceRemote companyServiceRemote;
    @EJB
    private StatusServiceRemote statusServiceRemote;
    @EJB
    private TypeServiceRemote typeServiceRemote;



    @PostConstruct
    public void init() {
        ticketVo = new TicketVo();

        setCompanies(companyServiceRemote.findAll());
        setTypes(typeServiceRemote.findAll());
        setStatuses(statusServiceRemote.findAll());

        history = new ArrayList<>();
        history.add(HistoryVo.builder().ticket(ticketVo).modStatus(CREATED).build());

        comments = new ArrayList<>();
        comments.add(CommentVo.builder().commentText("ticket_no_comment").build());

    }

    public void addTicket()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        log.debug("mentes");



        threeLetterCompanyID=getCompanyName().substring(0,2);
        uid=threeLetterCompanyID+String.valueOf(ticketVo.getId());

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
                .uid(uid)
                .title(title)
                .description(description)
                .clientMail(clientMail)
                .company(companyServiceRemote.findByName(companyName))
                .type(typeServiceRemote.findByName(typeName))
                .currentStatus(statusServiceRemote.findByName(statusName))
                .comments(comments)
                .history(history)
                .build();

        try{
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            recUserName = request.getUserPrincipal().getName();
            ticketServiceRemote.save(ticketVo, recUserName);
        }
        catch (Exception e)
        {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "ticketerror!");
            context.addMessage(null, facesMessage);
            return;

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

    public UserVo getUser() {
        return user;
    }

    public void setUser(UserVo user) {
        this.user = user;
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

    public TicketServiceRemote getTicketServiceRemote() {
        return ticketServiceRemote;
    }

    public void setTicketServiceRemote(TicketServiceRemote ticketServiceRemote) {
        this.ticketServiceRemote = ticketServiceRemote;
    }


    public List<CompanyVo> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyVo> companies) {
        this.companies = companies;
    }

    public List<TypeVo> getTypes() {
        return types;
    }

    public void setTypes(List<TypeVo> types) {
        this.types = types;
    }

    public List<StatusVo> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusVo> statuses) {
        this.statuses = statuses;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }



}
