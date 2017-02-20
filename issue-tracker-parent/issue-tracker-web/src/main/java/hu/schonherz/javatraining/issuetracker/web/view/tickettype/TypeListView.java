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

import org.primefaces.event.SelectEvent;

import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
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
	
	@ManagedProperty("#{userSessionBean}")
	private UserSessionBean userSessionBean;
	
	@ManagedProperty("#{mes}")
	private ResourceBundle bundle;
	
	
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

	public void onItemSelect(SelectEvent event) {
		submit();
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
}