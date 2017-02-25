package hu.schonherz.javatraining.issuetracker.web.view.ticket;


import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.history.HistoryServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.*;
import hu.schonherz.javatraining.issuetracker.web.beans.UserSessionBean;
import lombok.extern.log4j.Log4j;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


@ManagedBean(name = "createTicketView")
@ViewScoped
@Log4j
public class CreateTicketView implements Serializable {

    private static final int DAILY_MAX_NUMBER_OF_TICKET_ON_COMPANY=2;
    private static final int WEEKLY_MAX_NUMBER_OF_TICKET_ON_COMPANY=5;
    private static final int MONTHLY_MAX_NUMBER_OF_TICKET_ON_COMPANY=60;

    private static final String TICKETS_PAGE = "tickets.xhtml";
    private String recUserName;
    private String uid;
    private String threeLetterCompanyID;
    private String title;
    private String description;
    private String clientMail;
    private Long companyId;
    private Long statusId;
    private Long typeId;
    private Long userId;
    private List<CommentVo> comments;
    private List<HistoryVo> history;
    private TicketVo ticketVo;

    private int numberOfTicketsOnCompanyDaily;
    private int numberOfTicketsOnCompanyWeekly;
    private int numberOfTicketsOnCompanyMonthly;

    private List<CompanyVo> companies;
    private List<TypeVo> types;
    private List<StatusVo> statuses;
    private List<UserVo> users;


    @EJB
    private TicketServiceRemote ticketServiceRemote;
    @EJB
    private CompanyServiceRemote companyServiceRemote;
    @EJB
    private StatusServiceRemote statusServiceRemote;
    @EJB
    private TypeServiceRemote typeServiceRemote;
    @EJB
    private HistoryServiceRemote historyServiceRemote;
    @EJB
    private UserServiceRemote userServiceRemote;


    @ManagedProperty("#{userSessionBean}")
    private UserSessionBean userSessionBean;


    @ManagedProperty("#{mes}")
    private ResourceBundle bundle;


    @PostConstruct
    public void init() {
        ticketVo = new TicketVo();
        history = new ArrayList<>();

        companies = companyServiceRemote.findAll();
        types = typeServiceRemote.findAll();




    }


    public void addTicket() {
        FacesContext context = FacesContext.getCurrentInstance();
        CompanyVo companyVo = companyServiceRemote.findById(companyId);

        numberOfTicketsOnCompanyDaily=ticketServiceRemote.getNumberOfCreatedTicketsByCompanyToday(companyVo);
        numberOfTicketsOnCompanyWeekly=ticketServiceRemote.getNumberOfCreatedTicketsByCompanyThisWeek(companyVo);
        numberOfTicketsOnCompanyMonthly=ticketServiceRemote.getNumberOfCreatedTicketsByCompanyThisMonth(companyVo);

        threeLetterCompanyID = companyServiceRemote.findById(companyId).getName().substring(0, 3);
        threeLetterCompanyID = threeLetterCompanyID.toUpperCase();
        uid = threeLetterCompanyID;
        HistoryVo startHistory = HistoryVo.builder()
                .modStatus(HistoryEnum.CREATED)
                .build();

        startHistory = historyServiceRemote.save(startHistory, userSessionBean.getUserName());
        history.add(startHistory);

        ticketVo = TicketVo.builder()
                .uid(uid)
                .clientMail(clientMail)
                .description(description)
                .title(title)
                .company(companyVo)
                .type(typeServiceRemote.findById(typeId))
                .currentStatus(statusServiceRemote.findById(statusId))
                .user(userServiceRemote.findById(userId))
                .history(history)
                .build();

        if(numberOfTicketsOnCompanyDaily<=DAILY_MAX_NUMBER_OF_TICKET_ON_COMPANY && numberOfTicketsOnCompanyMonthly<=MONTHLY_MAX_NUMBER_OF_TICKET_ON_COMPANY && numberOfTicketsOnCompanyWeekly<=WEEKLY_MAX_NUMBER_OF_TICKET_ON_COMPANY) {
            try {

                recUserName = userSessionBean.getUserName();
                ticketVo.setCurrentStatus(ticketVo.getType().getStartEntity());

                ticketServiceRemote.save(ticketVo, recUserName);

                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", bundle.getString("ticketcreate_savesucces")));
                log.info("success to save ticket");
            } catch (Exception e) {

                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", bundle.getString("ticketcreate_saveerror")));
                log.error("error to save ticket", e);

            }
        }else
        {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", bundle.getString("ticketcreate_saveerror")));
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

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public UserServiceRemote getUserServiceRemote() {
        return userServiceRemote;
    }

    public void setUserServiceRemote(UserServiceRemote userServiceRemote) {
        this.userServiceRemote = userServiceRemote;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<UserVo> getUsers() {
        return users;
    }

    public void setUsers(List<UserVo> users) {
        this.users = users;
    }

    public int getNumberOfTicketsOnCompanyDaily() {
        return numberOfTicketsOnCompanyDaily;
    }

    public void setNumberOfTicketsOnCompanyDaily(int numberOfTicketsOnCompanyDaily) {
        this.numberOfTicketsOnCompanyDaily = numberOfTicketsOnCompanyDaily;
    }

    public int getNumberOfTicketsOnCompanyWeekly() {
        return numberOfTicketsOnCompanyWeekly;
    }

    public void setNumberOfTicketsOnCompanyWeekly(int numberOfTicketsOnCompanyWeekly) {
        this.numberOfTicketsOnCompanyWeekly = numberOfTicketsOnCompanyWeekly;
    }

    public int getNumberOfTicketsOnCompanyMonthly() {
        return numberOfTicketsOnCompanyMonthly;
    }

    public void setNumberOfTicketsOnCompanyMonthly(int numberOfTicketsOnCompanyMonthly) {
        this.numberOfTicketsOnCompanyMonthly = numberOfTicketsOnCompanyMonthly;
    }




}
