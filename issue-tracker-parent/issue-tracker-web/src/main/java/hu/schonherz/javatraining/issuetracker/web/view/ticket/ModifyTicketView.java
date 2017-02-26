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
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@ManagedBean(name = "modifyTicketView")
@ViewScoped
@Log4j
public class ModifyTicketView implements Serializable {

    private static final String TICKETS_PAGE = "tickets.xhtml";
    private String title;
    private String description;
    private String clientMail;
    private Long companyId;
    private Long statusId;
    private Long typeId;
    private TicketVo ticketVo;
    private Long TicketId;
    private Long userId;
    private HistoryVo history;
    private List<HistoryVo> histories;

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
        FacesContext context = FacesContext.getCurrentInstance();
        Long ticketId = Long.valueOf(context.getExternalContext().getRequestParameterMap().get("ticketId"));
        ticketVo = ticketServiceRemote.findById(ticketId);

        users = userServiceRemote.findAll();
        if (users.contains(ticketVo.getUser())) {
            users.remove(ticketVo.getUser());
        }

        types = typeServiceRemote.findAll();
        if (types.contains(ticketVo.getType())) {
            types.remove(ticketVo.getType());
        }

        statuses = typeServiceRemote.getStatuses(ticketVo.getType());
        if (statuses.contains(ticketVo.getCurrentStatus())) {
            statuses.remove(ticketVo.getCurrentStatus());
        }

        histories = ticketVo.getHistory();
    }


    public void updateTicket(){
        FacesContext context = FacesContext.getCurrentInstance();

        history = HistoryVo.builder()
                .modStatus(HistoryEnum.UPDATED)
                .build();

        history = historyServiceRemote.save(history, userSessionBean.getUserName());
        histories.add(history);

        if(!Objects.equals(ticketVo.getCurrentStatus().getId(), statusId))
        {
            history = HistoryVo.builder()
                    .modStatus(HistoryEnum.CHANGED_STATUS)
                    .build();

            history = historyServiceRemote.save(history, userSessionBean.getUserName());
            histories.add(history);
        }

        ticketVo = TicketVo.builder()
                .currentStatus(statusServiceRemote.findById(statusId))
                .clientMail(clientMail)
                .description(ticketVo.getDescription())
                .title(ticketVo.getTitle())
                .company(companyServiceRemote.findById(companyId))
                .type(typeServiceRemote.findById(typeId))
                .user(userServiceRemote.findById(userId))
                .history(histories)
                .build();


        ticketVo.setId(TicketId);

        try {

            String modUserName = userSessionBean.getUserName();
            ticketServiceRemote.update(ticketVo, modUserName);

            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", bundle.getString("ticketcreate_savesucces")));
            log.info("success to save ticket");
        } catch (Exception e) {

            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", bundle.getString("ticketcreate_saveerror")));
            log.error("error to save ticket",e);

        }


    }


    public TicketVo getTicketVo() {
        return ticketVo;
    }

    public void setTicketVo(TicketVo ticketVo) {
        this.ticketVo = ticketVo;
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

    public List<UserVo> getUsers() {
        return users;
    }

    public void setUsers(List<UserVo> users) {
        this.users = users;
    }
    public Long getTicketId() {
        return TicketId;
    }

    public void setTicketId(Long ticketId) {
        TicketId = ticketId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public HistoryVo getHistory() {
        return history;
    }

    public void setHistory(HistoryVo history) {
        this.history = history;
    }

    public List<HistoryVo> getHistories() {
        return histories;
    }

    public void setHistories(List<HistoryVo> histories) {
        this.histories = histories;
    }






}
