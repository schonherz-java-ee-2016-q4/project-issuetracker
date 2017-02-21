package hu.schonherz.javatraining.issuetracker.web.view;


import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.*;
import hu.schonherz.javatraining.issuetracker.web.beans.UserSessionBean;
import lombok.extern.log4j.Log4j;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import java.io.Serializable;

import java.util.List;



@ManagedBean(name = "ticketCreateModifyView")
@ViewScoped
@Log4j
public class TicketCreateModifyView implements Serializable{


    private static final String SUCCESS_PAGE = "tickets.xhtml";
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



    @ManagedProperty("#{userSessionBean}")
    private UserSessionBean userSessionBean;



    @PostConstruct
    public void init() {
        ticketVo = new TicketVo();

        companies = companyServiceRemote.findAll();
        types=typeServiceRemote.findAll();




    }

    public void addTicket()
    {
        FacesContext context = FacesContext.getCurrentInstance();

        threeLetterCompanyID=companyName.substring(0,3);
        threeLetterCompanyID=threeLetterCompanyID.toUpperCase();
        uid=threeLetterCompanyID;



        ticketVo=ticketVo.builder()
                .uid(uid)
                .clientMail(clientMail)
                .description(description)
                .title(title)
                .company(companyServiceRemote.findByName(companyName))
                .type(typeServiceRemote.findByName(typeName))
                .build();

        try{

            recUserName = userSessionBean.getUserName();
            ticketVo.setCurrentStatus(ticketVo.getType().getStartEntity());

            ticketServiceRemote.save(ticketVo, recUserName);

        }
        catch (Exception e)
        {

         log.error("error to save", e);

        }

    }

    public String getRecUserName() {
        return recUserName;
    }

    public void setRecUserName(String recUserName) {
        this.recUserName = recUserName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getThreeLetterCompanyID() {
        return threeLetterCompanyID;
    }

    public void setThreeLetterCompanyID(String threeLetterCompanyID) {
        this.threeLetterCompanyID = threeLetterCompanyID;
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

    public TicketVo getTicketVo() {
        return ticketVo;
    }

    public void setTicketVo(TicketVo ticketVo) {
        this.ticketVo = ticketVo;
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

    public TicketServiceRemote getTicketServiceRemote() {
        return ticketServiceRemote;
    }

    public void setTicketServiceRemote(TicketServiceRemote ticketServiceRemote) {
        this.ticketServiceRemote = ticketServiceRemote;
    }

    public CompanyServiceRemote getCompanyServiceRemote() {
        return companyServiceRemote;
    }

    public void setCompanyServiceRemote(CompanyServiceRemote companyServiceRemote) {
        this.companyServiceRemote = companyServiceRemote;
    }

    public StatusServiceRemote getStatusServiceRemote() {
        return statusServiceRemote;
    }

    public void setStatusServiceRemote(StatusServiceRemote statusServiceRemote) {
        this.statusServiceRemote = statusServiceRemote;
    }

    public TypeServiceRemote getTypeServiceRemote() {
        return typeServiceRemote;
    }

    public void setTypeServiceRemote(TypeServiceRemote typeServiceRemote) {
        this.typeServiceRemote = typeServiceRemote;
    }

    public UserSessionBean getUserSessionBean() {
        return userSessionBean;
    }

    public void setUserSessionBean(UserSessionBean userSessionBean) {
        this.userSessionBean = userSessionBean;
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



}
