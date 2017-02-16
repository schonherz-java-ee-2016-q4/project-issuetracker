package hu.schonherz.javatraining.issuetracker.web.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@ManagedBean(name = "typeCreateModifyView")
@ViewScoped
@Data
@Log4j
public class TypeCreateModifyView implements Serializable {

	private TypeVo typevo;
	private List<StatusVo> statuses;

	@ManagedProperty("#{mes}")
	private ResourceBundle bundle;

	@ManagedProperty(value = "#{addStatusView}")
	private AddStatusView addStatusView;

	@ManagedProperty(value = "#{modifyStatusView}")
	private ModifyStatusView modifyStatusView;

	@ManagedProperty(value = "#{modifyStatusOrderView}")
	private ModifyStatusOrderView modifyStatusOrderView;
	
	@PostConstruct
	public void init() {

		typevo = new TypeVo();

		statuses = new ArrayList<>();
		statuses.add(StatusVo.builder().name("asd").description("").build());
		statuses.add(StatusVo.builder().name("asd2").description("").build());
		
		List<StatusOrderViewModel> statusOrders = new ArrayList<>();
		statusOrders.add(StatusOrderViewModel.builder()
				.from("asd")
				.to("asd2")
				.build());
		statusOrders.add(StatusOrderViewModel.builder()
				.from("asd2")
				.to("asd")
				.build());
		
		modifyStatusOrderView.init();
		modifyStatusOrderView.generateDiagram(statuses, statusOrders);
	}

	public void save() {
		FacesContext context = FacesContext.getCurrentInstance();
		log.debug("mentes");

		if ("".equals(typevo.getName())) {
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("tickettype_empty_name")));
			return;
		}
		if ("".equals(typevo.getDescription())) {
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("tickettype_empty_desc")));
			return;
		}

		logCurrentStatus();
	}

	public void addNewStatus() {
		FacesContext context = FacesContext.getCurrentInstance();
		log.debug("status add");

		if ("".equals(addStatusView.getName()) || "".equals(addStatusView.getDescription())) {
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("tickettype_status_add_empty")));
			return;
		}

		String newStatusName = addStatusView.getName();
		for (StatusVo statusVo : statuses) {
			if (statusVo.getName().equals(newStatusName)) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						bundle.getString("tickettype_status_add_alreadyinuser")));
				return;
			}
		}

		statuses.add(
				StatusVo.builder().name(addStatusView.getName()).description(addStatusView.getDescription()).build());
		modifyStatusOrderView.addStatus(addStatusView.getName());

		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
				newStatusName + " " + bundle.getString("tickettype_status_add_succes")));

		addStatusView.setName("");
		addStatusView.setDescription("");
		logCurrentStatus();
	}
	
	public void modifyStatus() {
		FacesContext context = FacesContext.getCurrentInstance();
		log.debug("modify status");

		if ("".equals(modifyStatusView.getNewName()) || "".equals(modifyStatusView.getNewDescription())) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					bundle.getString("tickettype_status_modify_empty")));
			return;
		}

		for (StatusVo statusVo : statuses) {
			if (statusVo.equals(modifyStatusView.getSelectedStatus())) {
				String modStatName = statusVo.getName();
				statusVo.setName(modifyStatusView.getNewName());
				statusVo.setDescription(modifyStatusView.getNewDescription());
				modifyStatusOrderView.modifyStatus(modStatName, modifyStatusView.getNewName());
				
				modifyStatusView.setSelectedStatus(null);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						modStatName + " " + bundle.getString("tickettype_status_modify_succes")));
			}
		}
	}

	public void deleteStatus() {
		FacesContext context = FacesContext.getCurrentInstance();
		log.debug("delete status");

		String modStatName = modifyStatusView.getSelectedStatus().getName();
		statuses.remove(modifyStatusView.getSelectedStatus());
		modifyStatusOrderView.deleteStatus(modStatName);
		
		modifyStatusView.setSelectedStatus(null);
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
				modStatName + " " + bundle.getString("tickettype_status_modify_deleted")));
	}

	public void cancelModifyingStatus() {
		log.debug("cancel modifying status");

		modifyStatusView.setSelectedStatus(null);
	}
	
	private void logCurrentStatus() {
		log.debug("Name: " + typevo.getName());
		log.debug("Description:" + typevo.getDescription());
		log.debug("Statuses:");
		for (StatusVo statusVo : statuses) {
			log.debug(statusVo.getName() + " - " + statusVo.getDescription());
		}
		log.debug("Connections:");
		for (StatusOrderViewModel statusOrderViewModel : modifyStatusOrderView.getStatusOrders()) {
			log.debug(statusOrderViewModel.getFrom() + " - " + statusOrderViewModel.getTo());
		}
	}
}
