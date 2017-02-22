package hu.schonherz.javatraining.issuetracker.web.view.tickettype;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.statusorder.StatusOrderServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusOrderVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.web.beans.UserSessionBean;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@ManagedBean(name = "typeListView")
@ViewScoped
@Data
@Log4j
public class TypeListView implements Serializable {

	private final String MODIFY_TYPE_PAGE = "tickettype.xhtml";
	
	private String selected;
	private List<TypeVo> allType;
	
	@EJB
	private TypeServiceRemote typeService;
	
	@EJB
	private TicketServiceRemote ticketService;
	
	@ManagedProperty("#{userSessionBean}")
	private UserSessionBean userSessionBean;
	
	@ManagedProperty("#{mes}")
	private ResourceBundle bundle;
	
	@EJB
	private StatusOrderServiceRemote statusOrderService;
	
	@EJB
	private StatusServiceRemote statusService;
	
	@PostConstruct
	public void init() {
		allType = typeService.findByCompany(userSessionBean.getCurrentUser().getCompany());
		log.debug("loaded types: " + allType.size());
	}

	public List<String> completeText(String query) {
		List<String> filteredTypes = new ArrayList<>();
		
		String lowerQuery = query.toLowerCase();
		
		for (TypeVo type : allType) {
			if (type.getName().toLowerCase().contains(lowerQuery)) {
				filteredTypes.add(type.getName());
			}
		}
		return filteredTypes;
	}
	
	public void submit() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		if (selected == null) {
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("tickettype_list_invalid")));
			return;
		}
		
		TypeVo selectedType = null;
		for (TypeVo type : allType) {
			if (type.getName().contains(selected)) {
				selectedType = type;
				break;
			}
		}
		
		if (selectedType == null) {
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("tickettype_list_invalid")));
			return;
		}
		
		
		try {
			context.getExternalContext().redirect(String.format("%s?id=%s", MODIFY_TYPE_PAGE, selectedType.getId()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void delete() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		if (selected == null) {
			context.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("tickettype_list_invalid")));
			return;
		}
		
		TypeVo selectedType = null;
		for (TypeVo type : allType) {
			if (type.getName().contains(selected)) {
				selectedType = type;
				break;
			}
		}
		
		if (selectedType == null) {
			context.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("tickettype_list_invalid")));
			return;
		}
		
		List<TicketVo> findByType = ticketService.findByType(selectedType);
		if (findByType.size() != 0) {
			context.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("tickettype_list_typeinuse")));
			return;
		}
		
		deleteType(selectedType);
		selected = "";
		context.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("tickettype_list_delete_succes")));
	}
	
	private void deleteType(TypeVo type) {
		List<StatusVo> statuses = typeService.getStatuses(type);
		
		for (StatusVo status : statuses) {
			List<StatusOrderVo> fromStatuses = statusOrderService.findByFromStatusId(status.getId());
			
			for (StatusOrderVo statusOrder : fromStatuses) {
				statusOrderService.deleteById(statusOrder.getId());
			}
		}
		
		typeService.delete(type);
		
		for (StatusVo status : statuses) {
			statusService.delete(status);
		}
		
		allType.remove(type);
		
	}
}
